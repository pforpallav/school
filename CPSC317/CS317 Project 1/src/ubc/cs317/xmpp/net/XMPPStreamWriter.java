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

import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import ubc.cs317.xmpp.exception.XMPPException;

/**
 * This class implements the writing part of an XML stream, focused specifically
 * on sending for the XMPP protocol.
 * 
 */
public class XMPPStreamWriter {

	private Document baseDocument;
	private Transformer transformerNoDeclaration;
	private Transformer transformerWithDeclaration;

	private OutputStream stream;

	/**
	 * Creates a new instance of the XMPP stream writer. Initialises the DOM
	 * parsers and transformers.
	 * 
	 * @param stream
	 *            OutputStream corresponding to the destination of the XMPP
	 *            stream.
	 * @throws XMPPException
	 *             If there is any problem creating or initializing transformers
	 *             and parsers.
	 */
	public XMPPStreamWriter(OutputStream stream) throws XMPPException {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			baseDocument = parser.newDocument();

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();

			transformerNoDeclaration = transformerFactory.newTransformer();
			transformerNoDeclaration.setOutputProperty(
					OutputKeys.OMIT_XML_DECLARATION, "yes");

			transformerWithDeclaration = transformerFactory.newTransformer();

			this.stream = stream;

		} catch (TransformerConfigurationException e) {
			throw new XMPPException(e);
		} catch (ParserConfigurationException e) {
			throw new XMPPException(e);
		}
	}

	/**
	 * Creates a new element.
	 * 
	 * @param tagName
	 *            Element tag name.
	 * @return The newly created element.
	 */
	public Element createElement(String tagName) {

		return baseDocument.createElement(tagName);
	}

	/**
	 * Creates a new element and sets it as the root element of the stream.
	 * 
	 * @param tagName
	 *            Element tag name.
	 * @return The newly created element.
	 */
	public Element createRootElement(String tagName) {

		Element root = createElement(tagName);
		baseDocument.appendChild(root);
		return root;
	}

	/**
	 * Writes the root element to the output stream, but does not include the
	 * closing tag of the element. Since there is no direct way to implement
	 * this writing through the regular DOM API, this function adds a bogus
	 * child to the element, writes it to a temporary buffer, then removes the
	 * bogus child and ending tag from the buffer before writing it to the final
	 * destination.
	 * 
	 * @throws XMPPException
	 *             If there was a problem trying to write the element.
	 */
	public void writeRootElementWithoutClosingTag() throws XMPPException {

		try {
			StringWriter writer = new StringWriter();
			Element root = baseDocument.getDocumentElement();
			Text bogusTextNode = baseDocument.createTextNode("bogus");

			System.out.printf("Sending opening tag for <%s>\n",
					root.getTagName());

			// Adds bogus text node to end so that it has a child and an
			// end-tag.
			root.appendChild(bogusTextNode);

			Source src = new DOMSource(baseDocument);
			Result dest = new StreamResult(writer);
			transformerWithDeclaration.transform(src, dest);

			root.removeChild(bogusTextNode);

			String result = writer.toString().replaceFirst(
					bogusTextNode.getTextContent()
							+ "[ \n\r\t\f]*</[ \n\r\t\f]*" + root.getTagName()
							+ "[^>]*>", "");

			stream.write(result.getBytes());

		} catch (Exception e) {
			throw new XMPPException("Could not send opening tag: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * Writes an individual element to the output stream. This element does not
	 * need to be the root element. The entire element is printed, including the
	 * closing tag, if needed.
	 * 
	 * @param element
	 *            The element to be written.
	 * @throws XMPPException
	 *             If there was a problem writing the element.
	 */
	public void writeIndividualElement(Element element) throws XMPPException {

		try {
			Source src = new DOMSource(element);
			Result dest = new StreamResult(stream);
			transformerNoDeclaration.transform(src, dest);

		} catch (Exception e) {
			throw new XMPPException(
					"Could not send element: " + e.getMessage(), e);
		}
	}

	/**
	 * Writes the closing tag for the root element of the XML stream document.
	 * 
	 * @throws XMPPException
	 *             If there was a problem writing the element closing tag.
	 */
	public void writeCloseTagRootElement() throws XMPPException {

		try {
			Element root = baseDocument.getDocumentElement();
			stream.write(("</" + root.getTagName() + ">").getBytes());

		} catch (Exception e) {
			throw new XMPPException("Could not send closing tag: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * This function is provided as a bonus for testing purposes. Prints the
	 * provided element to a specific stream.
	 * 
	 * @param debugStream
	 *            The stream where the element should be written to. Usually
	 *            <code>System.out</code>.
	 * @param element
	 *            The element to be written.
	 */
	public void debugElement(OutputStream debugStream, Element element) {

		try {
			Source src = new DOMSource(element);
			Result dest = new StreamResult(debugStream);
			transformerNoDeclaration.transform(src, dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
