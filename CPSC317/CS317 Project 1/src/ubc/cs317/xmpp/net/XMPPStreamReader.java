/*
 * University of British Columbia
 * Department of Computer Science
 * CPSC317 - Internet Programming
 * Assignment 1
 * 
 * Author: Jonatan Schroeder
 * January 2012
 * 
 * This code may not be used without written consent of the authors, except for 
 * current and future projects and assignments of the CPSC317 course at UBC.
 */

package ubc.cs317.xmpp.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ubc.cs317.xmpp.exception.XMPPException;

/**
 * This class implements the reading part of an XML stream, focused specifically
 * on receiving for the XMPP protocol. The class is implemented as a SAX parser,
 * however relevant methods return DOM elements.
 */
public class XMPPStreamReader {

	private static final int MARK_READLIMIT = 10000;

	private Document baseDocument;
	private Stack<Element> currentElementContainerStack = new Stack<Element>();
	private Queue<Element> firstOrSecondLevelQueue = new LinkedList<Element>();
	private XMPPException outstandingException = null;
	private boolean receivedOneCompleteElement;

	private InputStream inputStream;
	private Thread readingThread;

	/**
	 * Creates an instance of the XMPP stream reader. Starts a new thread that
	 * reads on the specified input stream.
	 * 
	 * @param stream
	 *            InputStream corresponding to the source of the XMPP stream.
	 */
	public XMPPStreamReader(InputStream stream) {

		this.inputStream = new BufferedInputStream(stream);

		readingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				saxReadingProcess();
			}
		});
		readingThread.start();
	}

	/**
	 * Waits for an reads a direct child element of the root element in the XMPP
	 * stream. This function waits until a complete element is received,
	 * including the ending tag. A document root element may be returned in this
	 * function if the document ended.
	 * 
	 * @return An Element object corresponding to the read object. It will
	 *         include any text content, attributes or containing elements of
	 *         the element.
	 * @throws XMPPException
	 *             If there was a problem reading or parsing the element, or if
	 *             there was an IO exception.
	 */
	public Element readSecondLevelElement() throws XMPPException {

		synchronized (this) {
			// TODO If exception happens during parsing, or if document is
			// closed, throws an exception
			while (firstOrSecondLevelQueue.isEmpty()) {
				checkOutstandingException();
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
			return firstOrSecondLevelQueue.poll();
		}
	}

	/**
	 * Waits until the streamed document is closed, i.e. until a closing tag for
	 * the root element of the streamed XML document is received.
	 * 
	 * @throws XMPPException
	 *             If there was a problem reading or parsing the element, or if
	 *             there was an IO exception.
	 */
	public void waitForCloseDocument() throws XMPPException {
		synchronized (this) {
			while (!currentElementContainerStack.isEmpty()) {
				checkOutstandingException();
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * Checks if a given element corresponds to the base document. This method
	 * is useful to identify if a element retrieved from the stream corresponds
	 * to the end of the stream, based on XML structure.
	 * 
	 * @param element
	 *            Element to compare against the base document.
	 * @return <code>true</code> if the informed element is the base document of
	 *         the XML structure, <code>false</code> otherwise.
	 */
	public boolean isBaseDocument(Element element) {
		return baseDocument.equals(element);
	}

	/**
	 * Verifies if the stream has returned the entire document, including the
	 * closing tag for the base document, and that all elements have been
	 * properly retrieved from the stream.
	 * 
	 * @return <code>true</code> if there are no more elements to read and the
	 *         last element retrieved was the base document, <code>false</code>
	 *         otherwise.
	 */
	public boolean isDocumentComplete() {
		return currentElementContainerStack.isEmpty()
				&& firstOrSecondLevelQueue.isEmpty();
	}

	private class RestartProcessingException extends SAXException {
		public RestartProcessingException() {
			super();
		}
	}

	private class XMPPHandler extends DefaultHandler {

		@Override
		public void startDocument() throws SAXException {

			synchronized (this) {

				try {
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder parser = factory.newDocumentBuilder();
					baseDocument = parser.newDocument();
				} catch (ParserConfigurationException e) {
					throw new SAXException(e);
				}

				currentElementContainerStack = new Stack<Element>();
				receivedOneCompleteElement = false;
			}
		}

		@Override
		public void endDocument() throws SAXException {
			synchronized (XMPPStreamReader.this) {
				assert (currentElementContainerStack.isEmpty());
				XMPPStreamReader.this.notifyAll();
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			currentElementContainerStack.peek().appendChild(
					baseDocument.createTextNode(new String(ch, start, length)));
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			Element currentElement = baseDocument.createElement(qName);
			for (int i = 0; i < attributes.getLength(); i++) {
				currentElement.setAttribute(attributes.getQName(i),
						attributes.getValue(i));
			}

			if (currentElementContainerStack.isEmpty())
				baseDocument.appendChild(currentElement);
			else
				currentElementContainerStack.peek().appendChild(currentElement);

			currentElementContainerStack.push(currentElement);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			Element currentElement = currentElementContainerStack.pop();

			if (currentElementContainerStack.size() < 2) {

				synchronized (XMPPStreamReader.this) {
					firstOrSecondLevelQueue.add(currentElement);
					XMPPStreamReader.this.notifyAll();
				}
			}

			inputStream.mark(MARK_READLIMIT);
			receivedOneCompleteElement = true;
		}

		@Override
		public void fatalError(SAXParseException parseEx) throws SAXException {

			if (receivedOneCompleteElement) {
				try {
					inputStream.reset();
				} catch (IOException e) {
					throw new SAXException(e);
				}
				// RestartProcessingException is used to inform the function
				// that
				// called the parser that parsing should be restarted.
				throw new RestartProcessingException();
			} else {
				throw parseEx;
			}
		}
	}

	private void setOutstandingException(XMPPException exception) {
		synchronized (this) {
			this.outstandingException = exception;
			this.notifyAll();
		}
	}

	private void checkOutstandingException() throws XMPPException {
		if (this.outstandingException != null)
			throw this.outstandingException;
	}

	private void saxReadingProcess() {
		try {
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxFactory.newSAXParser();
			saxParser.parse(inputStream, new XMPPHandler());
		} catch (RestartProcessingException ex) {
			// This exception is used only to restart the XML
			// processing.
			// It is not an error.
			System.out.println("Processing restart requested.");
			saxReadingProcess();
		} catch (Exception ex) {
			setOutstandingException(new XMPPException(
					"Communication interrupted."
							+ ((ex.getMessage() != null && !ex.getMessage()
									.equals("")) ? " Reason: "
									+ ex.getMessage() : ""), ex));
		}
	}
}
