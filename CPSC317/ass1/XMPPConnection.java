/*
 * University of British Columbia
 * Department of Computer Science
 * CPSC317 - Internet Programming
 * Assignment 1
 * 
 * Author: 
 * January 2012
 * 
 * This code may not be used without written consent of the authors, except for 
 * current and future projects and assignments of the CPSC317 course at UBC.
 */

package ubc.cs317.xmpp.net;

import java.net.Socket;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.Contact;
import ubc.cs317.xmpp.model.ContactStatus;
import ubc.cs317.xmpp.model.Conversation;
import ubc.cs317.xmpp.model.Message;
import ubc.cs317.xmpp.model.Session;

/**
 * This class describes the XMPP connection handler. A socket connection is
 * created when an instance of this handler is created, and methods are provided
 * for most common operations in XMPP.
 * 
 * This class will not in any case make a direct reference to any class or
 * method that represents a specific UI library.
 */
/**
 * @author Evan Louie/72210099/m6d7
 * 
 */
public class XMPPConnection {

	/**
	 * Default TCP port for client-server communication in XMPP.
	 */
	public static final int XMPP_DEFAULT_PORT = 5222;

	/**
	 * Session object for communication between the network component and the
	 * chat model and UI.
	 */
	private Session session;

	/**
	 * Socket object associated to the communication between this client and the
	 * XMPP server.
	 */
	private Socket socket;
	private boolean debug = false;
	// Features sent from the server
	private Element features;

	private boolean connectionOpen = false;
	/**
	 * XMPP reader helper, used to obtain XML nodes from the XMPP stream.
	 */
	private XMPPStreamReader xmppReader;

	/**
	 * XMPP writer helper, used to write XML nodes to the XMPP stream.
	 */
	private XMPPStreamWriter xmppWriter;

	/**
	 * Creates a new instance of the connection handler. This constructor will
	 * creating the socket, initialise the reader and writer helpers, send
	 * initial tags, authenticate the user and bind to a resource.
	 * 
	 * @param jidUser
	 *            User part of the Jabber ID.
	 * @param jidDomain
	 *            Domain part of the Jabber ID.
	 * @param resource
	 *            Resource to bind once authenticated. If null or empty, a new
	 *            resource will be generated.
	 * @param password
	 *            Password for authentication.
	 * @param session
	 *            Instance of the session to communicate with other parts of the
	 *            system.
	 * @throws XMPPException
	 *             If there is an error establishing the connection, sending or
	 *             receiving necessary data, or while authenticating.
	 */
	public XMPPConnection(String jidUser, String jidDomain, String resource,
			String password, Session session) throws XMPPException {

		this.session = session;

		initializeConnection(jidDomain);

		try {
			xmppReader = new XMPPStreamReader(socket.getInputStream());
			xmppWriter = new XMPPStreamWriter(socket.getOutputStream());
		} catch (XMPPException e) {
			throw e;
		} catch (Exception e) {
			throw new XMPPException("Could not obtain socket I/O channels ("
					+ e.getMessage() + ")", e);
		}

		initializeStreamAndFeatures(jidUser, jidDomain);

		login(jidUser, password);

		bindResource(resource);

		startListeningThread();
	}

	/**
	 * Initialises the connection with the specified domain. This method sets
	 * the socket field with an initialised socket.
	 * 
	 * @param domain
	 *            DNS name (or IP string) of the server to connect to.
	 * @throws XMPPException
	 *             If there is a problem connecting to the server.
	 */
	private void initializeConnection(String domain) throws XMPPException {

		/* YOUR CODE HERE */
		try {
			socket = new Socket(domain, XMPP_DEFAULT_PORT);

		} catch (Exception e) {
			throw new XMPPException("Could not initialize connection:"
					+ e.getMessage(), e);
		}
	}

	/**
	 * Sends the initial data to establish an XMPP connection stream with the
	 * XMPP server. This method also retrieves the set of features from the
	 * server, saving it in a field for future use.
	 * 
	 * @param jidUser
	 *            User part of the Jabber ID.
	 * @param jidDomain
	 *            Domain part of the Jabber ID.
	 * @throws XMPPException
	 *             If there is a problem sending or receiving the data.
	 */
	private void initializeStreamAndFeatures(String jidUser, String jidDomain)
			throws XMPPException {

		/* YOUR CODE HERE */

		// Write XML root element
		Element stream = xmppWriter.createRootElement("stream:stream");
		stream.setAttribute("from", jidUser);
		stream.setAttribute("to", jidDomain);
		stream.setAttribute("version", "1.0");
		stream.setAttribute("xml:lang", "en");
		stream.setAttribute("xmlns", "jabber:client");
		stream.setAttribute("xmlns:stream", "http://etherx.jabber.org/streams");

		// Send element & receive features
		xmppWriter.writeRootElementWithoutClosingTag();
		features = xmppReader.readSecondLevelElement();

		if (debug) {
			xmppWriter.debugElement(System.out, features);
		}

		if (features == null
				|| !features.getTagName().toLowerCase()
						.equals("stream:features")) {
			throw new XMPPException("Stream features not returned");
		}

	}

	/**
	 * Attempts to authenticate the user name with the provided password at the
	 * connected server. This method will verify if the server supports the
	 * implemented authentication mechanism(s) and send the user and password
	 * based on the first mechanism it finds. In case authentication is not
	 * successful, this function will close the connection and throw an
	 * XMPPException. This function also retrieves the new set of features
	 * available after authentication.
	 * 
	 * @param username
	 *            User name to use for authentication.
	 * @param password
	 *            Password to use for authentication.
	 * @throws XMPPException
	 *             If authentication is not successful, or if authentication
	 *             methods supported by the server are not implemented, or if
	 *             there was a problem sending authentication data.
	 */
	private void login(String username, String password) throws XMPPException {

		/* YOUR CODE HERE */

		// Initialize stream
		Element authentication = xmppWriter.createElement("auth");
		authentication
				.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-sasl");
		authentication.setAttribute("mechanism", "PLAIN");

		// Convert username/password 64bit
		String string = "\0" + username + "\0" + password;
		String message = DatatypeConverter.printBase64Binary(string.getBytes());
		authentication.setTextContent(message);

		if (debug) {
			xmppWriter.debugElement(System.out, authentication);
		}

		xmppWriter.writeIndividualElement(authentication);

		Element callback = xmppReader.readSecondLevelElement();
		if (callback == null
				|| callback.getTagName().toLowerCase().equals("failure")) {
			closeConnection();
			throw new XMPPException("Incorrect username or password");
		}
		if (debug) {
			xmppWriter.debugElement(System.out, callback);
			System.out.println();
		}

		// Reconnect to stream
		xmppWriter.writeRootElementWithoutClosingTag();
		features = xmppReader.readSecondLevelElement();
		if(debug) {
			xmppWriter.debugElement(System.out, features);
			System.out.println();
		}
		connectionOpen = true;
	}

	/**
	 * Binds the connection to a specific resource, or retrieves a
	 * server-generated resource if one is not provided. This function will wait
	 * until a resource is sent by the server.
	 * 
	 * @param resource
	 *            Name of the user-specified resource. If resource is null or
	 *            empty, retrieves a server-generated one.
	 * @throws XMPPException
	 *             If there is an error sending or receiving the data.
	 */
	private void bindResource(String resource) throws XMPPException {

		/* YOUR CODE HERE */
		if (resource.equals("")) {
			resource = "";
		}
		// Create Elements
		Element bindReq = xmppWriter.createElement("iq");
		Element bind = xmppWriter.createElement("bind");
		Element name = xmppWriter.createElement("resource");

		// Set Attributes
		bindReq.setAttribute("type", "set");
		bindReq.setAttribute("id", "bind_1");
		bind.setAttribute("xmlns", "urn:ietf:params:xml:ns:xmpp-bind");
		name.setTextContent(resource);

		// Append Children
		bindReq.appendChild(bind);
		bind.appendChild(name);

		// Send/Receive RequestInfo/Response
		xmppWriter.writeIndividualElement(bindReq);
		Element callback = xmppReader.readSecondLevelElement();

		if (debug) {
			xmppWriter.debugElement(System.out, callback);
		}

		// Error Handling
		if (callback.getAttribute("type").toLowerCase().equals("error")) {
			NodeList resultElements = callback.getChildNodes();

			if (((Element) resultElements.item(1)).getTagName().toLowerCase()
					.equals("not-allowed")) {
				closeConnection();
				throw new XMPPException("Binding to this resouce = not allowed");
			} else {
				closeConnection();
				throw new XMPPException("Simultaneous connection limit reached");
			}

		} else {
			NodeList jidNodes = callback.getElementsByTagName("jid");
			Element jid = (Element) jidNodes.item(0);
			session.setUserJid(jid.getTextContent());
		}

	}

	/**
	 * Starts a thread that will keep listening for new messages asynchronously
	 * from the main thread.
	 */
	private void startListeningThread() {
		Thread listeningThread = new Thread(new Runnable() {

			@Override
			public void run() {
				listeningProcess();
			}
		});
		listeningThread.start();
	}

	/**
	 * Keeps listening for new XML elements in a loop until a closing tag is
	 * found or an exception happens. If an exception happens, calls
	 * <code>session.processReceivedException</code> and closes the connection.
	 * If the closing tag is found, sends a closing tag back and closes the
	 * connection as well. In both cases, the connection is closed at the
	 * session level, so that the model and UI can handle the connection being
	 * closed. For each received element, processes the element and handles it
	 * accordingly.
	 */
	@SuppressWarnings("deprecation")
	private void listeningProcess() {

		/* YOUR CODE HERE */

		// loop while connection is open
		while (connectionOpen) {

			// read from socket
			try {
				Element message = xmppReader.readSecondLevelElement();

				if (debug) {
					xmppWriter.debugElement(System.out, message);
					System.out.println();
				}

				// Message Handling & Contact Population
				if (message.getTagName().toLowerCase().equals("iq")
						&& message.getAttribute("type").equals("result")) {

					NodeList contacts = message.getElementsByTagName("item");

					// Iterate through contacts
					for (int i = 0; i < contacts.getLength(); i++) {
						Element currentContact = (Element) contacts.item(i);

						// Only add non present contacts
						String jid = currentContact.getAttribute("jid");
						if (session.getContact(jid) == null
								&& currentContact.getAttribute("subscription")
										.equals("both")) {
							String alias = currentContact.getAttribute("name");
							Contact contact;
							// if alias not set
							if (alias.equals("")) {
								contact = new Contact(jid, null);
							} else {
								contact = new Contact(jid, alias);
							}
							session.addReceivedContact(contact);
						}
						// if contact unsubscribed
						else if (currentContact.getAttribute("subscription")
								.equals("none")) {
							// Create Elements
							Element remove = xmppWriter.createElement("iq");
							Element query = xmppWriter.createElement("query");
							Element item = xmppWriter.createElement("item");

							// Set Attributes
							remove.setAttribute("id", "rem1");
							remove.setAttribute("type", "set");
							remove.setAttribute("from", session.getUserJid());
							query.setAttribute("xmlns", "jabber:iq:roster");
							item.setAttribute("jid", jid);
							item.setAttribute("subscription", "remove");

							// Append Children
							query.appendChild(item);
							remove.appendChild(query);

							// Send Removal
							xmppWriter.writeIndividualElement(remove);
						}

					}

				}
				// Contact list change
				else if (message.getTagName().toLowerCase().equals("iq")
						&& message.getAttribute("type").equals("set")) {
					// Get contact info
					NodeList contactInfo = message.getElementsByTagName("item");

					for (int i = 0; i < contactInfo.getLength(); i++) {
						Element currentContact = (Element) contactInfo.item(i);
						// add contact if not present
						String jid = currentContact.getAttribute("jid");

						// If contact removed
						if (currentContact.getAttribute("subscription").equals(
								"remove")) {
							if (session.getContact(jid) != null) {
								session.removeContact(session.getContact(jid));
							}

						}
						// If contacted added
						else if (session.getContact(jid) == null
								&& !currentContact.getAttribute("subscription")
										.equals("unsubscribe")) {
							String alias = currentContact.getAttribute("name");
							Contact contact = new Contact(jid, alias);
							session.addReceivedContact(contact);
						}
					}
				}
				// Message Handling
				else if (message.getTagName().toLowerCase().equals("message")
						&& message.getAttribute("type").equals("chat")) {

					// get the message body
					NodeList messageBody = message.getElementsByTagName("body");

					if (messageBody.getLength() < 1) {
//						System.out.println("User is typing");
					} else {
						// get the body text and current date
						String messageText = ((Element) messageBody.item(0))
								.getTextContent();
						Date current = new Date();
						current.getDate();

						// get contacts
						String jidString = message.getAttribute("from");
						String jid[] = jidString.split("/");
						Contact contact = session.getContact(jid[0]);

						// Add message to conversation
						Message newMessage = new Message(contact, null,
								messageText, current);
						Conversation conv = session.getConversation(contact);
						conv.addIncomingMessage(newMessage, jid[0]);
					}
				}
				// Presence || Status
				else if (message.getTagName().toLowerCase().equals("presence")) {

					// show element
					NodeList xmppList = message.getElementsByTagName("show");
					Element showInit = null;
					if (xmppList.getLength() > 0) {
						showInit = (Element) xmppList.item(0);
					}

					// Explode JID String
					String jidString = message.getAttribute("from");
					String[] jid = jidString.split("/");

					if (debug) {
						xmppWriter.debugElement(System.out, showInit);
						System.out.println();
					}

					// Status Updates
					if (showInit != null) {

						String status = showInit.getTagName();

						if (session.getContact(jid[0]) == null) {
						} else if (status.equals("chat")) {
							Contact contact = session.getContact(jid[0]);
							contact.setStatus(jid[1], ContactStatus.CHAT);
						} else if (status.equals("away")) {
							Contact contact = session.getContact(jid[0]);
							contact.setStatus(jid[1], ContactStatus.AWAY);
						} else if (status.equals("dnd")) {
							Contact contact = session.getContact(jid[0]);
							contact.setStatus(jid[1], ContactStatus.DND);
						} else {
							Contact contact = session.getContact(jid[0]);
							contact.setStatus(jid[1], ContactStatus.XA);
						}

					}

					// Available/Offline/SubscriptionRequest
					else {
						String presenceType = message.getAttribute("type");

						// Message from non contact
						if (session.getContact(jid[0]) == null) {
							if (jid[0].equals(session.getUserBareJid())) {
							}
							// non contact adds user JID
							else if (presenceType.equals("subscribe")) {
								String buddyJid = message.getAttribute("from");
								session.handleReceivedSubscriptionRequest(buddyJid);
							} else if (presenceType.equals("unsubscribe")) {
								System.out
										.println("Contact does not exist in contact list");
							}
						} else if (presenceType.equals("")) {
							Contact contact = session.getContact(jid[0]);
							contact.setStatus(jid[1], ContactStatus.AVAILABLE);
						} else if (presenceType.equals("unavailable")) {
							Contact contact = session.getContact(jid[0]);
							contact.setStatus(null, ContactStatus.OFFLINE);
						}
						// reply to subscriptions after adding contact
						else if (presenceType.equals("subscribe")) {
							String buddyJid = message.getAttribute("from");
							session.handleReceivedSubscriptionRequest(buddyJid);

						}
						// send current status if subscribed
						else if (presenceType.equals("subscribed")) {
							sendCurrentStatus();
						}
						// contact removes this user
						else if (presenceType.equals("unsubscribed")) {
							if (session.getContact(jid[0]) != null) {
								removeAndUnsubscribeContact(session
										.getContact(jid[0]));
							}
						} else {
							if (debug) {
								xmppWriter.debugElement(System.out, message);
								System.out.println();
							}
						}

					}

				} else {
					if (debug) {
						xmppWriter.debugElement(System.out, message);
						System.out.println();
					}
				}

			} catch (XMPPException e) {
				session.processReceivedException(e);
				closeConnection();
			}

		}
	}

	/**
	 * Closes the connection. If the connection was already closed before this
	 * method is called nothing is done, otherwise sends all necessary closing
	 * data and waits for the server to send the closing data as well. Once this
	 * happens the socket connection is closed. This method does not throw any
	 * exception, choosing instead to ignore them. However, even if an exception
	 * happens while sending the final data, the socket connection will be
	 * closed.
	 */
	public synchronized void closeConnection() {

		/* YOUR CODE HERE */
		// Wait for thread sending Close Tag to finish
		while (connectionOpen) {
			try {

				// Send Close Tag
				xmppWriter.writeCloseTagRootElement();
				connectionOpen = false;
			} catch (XMPPException e) {
				System.out.println(e);
			}
		}
		// Once Close Tage sent:
		try {
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Sends a request for the contact list. The result is not expected to be
	 * received in this function, but it should come in a message that will be
	 * handled by the listening process.
	 * 
	 * @throws XMPPException
	 *             If there was a problem sending the request.
	 */
	public void sendRequestForContactList() throws XMPPException {

		/* YOUR CODE HERE */
		Element request = xmppWriter.createElement("iq");
		request.setAttribute("from", session.getUserJid());
		request.setAttribute("id", "bind_1");
		request.setAttribute("type", "get");

		Element query = xmppWriter.createElement("query");
		query.setAttribute("xmlns", "jabber:iq:roster");
		request.appendChild(query);
		if (debug == true) {
			xmppWriter.debugElement(System.out, request);
		}
		xmppWriter.writeIndividualElement(request);

	}

	/**
	 * Sends an updated status information to the server, based on the status
	 * currently attributed to the session.
	 * 
	 * @throws XMPPException
	 *             If there was a problem sending the status.
	 */
	public void sendCurrentStatus() throws XMPPException {
		sendStatus(session.getCurrentStatus());
	}

	/**
	 * Sends a specific status information to the server.
	 * 
	 * @param status
	 *            Status to send to the server.
	 * @throws XMPPException
	 *             If there was a problem sending the status.
	 */
	private void sendStatus(ContactStatus status) throws XMPPException {

		/* YOUR CODE HERE */
		// Make nodes
		Element presence = xmppWriter.createElement("presence");
		Element show = xmppWriter.createElement("show");
		Element stat = xmppWriter.createElement("status");

		// Set Attributes
		presence.setAttribute("from", session.getUserJid());
		presence.setAttribute("id", "pres1");
		stat.setTextContent(status.getUserFriendlyName());
		show.setTextContent(status.getXmppShow());

		// Append children
		presence.appendChild(show);
		presence.appendChild(stat);

		if (debug == true) {
			xmppWriter.debugElement(System.out, presence);
		}

		// Send presence
		xmppWriter.writeIndividualElement(presence);
	}

	/**
	 * Sends a request that a new contact be added to the list of contacts.
	 * Additionally, requests authorization from that contact to receive updates
	 * any time the contact changes its status. This function does not add the
	 * user to the local list of contacts, which happens at the listening
	 * process once the server sends an update to the list of contacts as a
	 * result of this request.
	 * 
	 * @param contact
	 *            Contact that should be requested.
	 * @throws XMPPException
	 *             If there is a problem sending the request.
	 */
	public void sendNewContactRequest(Contact contact) throws XMPPException {

		/* YOUR CODE HERE */
		// Create Elements
		Element contactReq = xmppWriter.createElement("iq");
		Element query = xmppWriter.createElement("query");
		Element item = xmppWriter.createElement("item");

		// Set Attributes
		contactReq.setAttribute("id", "bind_1");
		contactReq.setAttribute("type", "set");
		contactReq.setAttribute("from", session.getUserJid());
		query.setAttribute("xmlns", "jabber:iq:roster");
		item.setAttribute("jid", contact.getBareJid());
		item.setAttribute("name", contact.getAlias());

		// Append Children
		query.appendChild(item);
		contactReq.appendChild(query);

		// Send Request
		xmppWriter.writeIndividualElement(contactReq);

		// Create Subscription Message
		Element subReq = xmppWriter.createElement("presence");
		subReq.setAttribute("id", "bind_1");
		subReq.setAttribute("to", contact.getBareJid());
		subReq.setAttribute("type", "subscribe");

		// Send SM
		xmppWriter.writeIndividualElement(subReq);
	}

	/**
	 * Sends a response message to a contact that requested authorization to
	 * receive updates when the local user changes its status.
	 * 
	 * @param jid
	 *            Jabber ID of the contact that requested authorization.
	 * @param accepted
	 *            <code>true</code> if the request was accepted by the user,
	 *            <code>false</code> otherwise.
	 * @throws XMPPException
	 *             If there was an error sending the response.
	 */
	public void respondContactRequest(String jid, boolean accepted)
			throws XMPPException {

		/* YOUR CODE HERE */
		// Create presence response
		Element response = xmppWriter.createElement("presence");
		response.setAttribute("id", "res1");
		response.setAttribute("to", jid);

		if (accepted) {
			response.setAttribute("type", "subscribed");
		} else {
			response.setAttribute("type", "unsubscribed");
		}

		// Send response
		xmppWriter.writeIndividualElement(response);
	}

	/**
	 * Request that the server remove a specific contact from the list of
	 * contacts. Additionally, requests that no further status updates be sent
	 * regarding that contact, as well as that no further status updates about
	 * the local user be sent to that contact. This function does not remove the
	 * user from the local list of contacts, which happens at the listening
	 * process once the server sends an update to the list of contacts as a
	 * result of this request.
	 * 
	 * @param contact
	 *            Contact to be removed from the list of contacts.
	 * @throws XMPPException
	 *             If there was an error sending the request.
	 */
	public void removeAndUnsubscribeContact(Contact contact)
			throws XMPPException {

		/* YOUR CODE HERE */
		// Create Elements
		Element remove = xmppWriter.createElement("iq");
		Element query = xmppWriter.createElement("query");
		Element item = xmppWriter.createElement("item");

		// Set Attributes
		remove.setAttribute("id", "rem1");
		remove.setAttribute("type", "set");
		remove.setAttribute("from", session.getUserJid());
		query.setAttribute("xmlns", "jabber:iq:roster");
		item.setAttribute("jid", contact.getBareJid());
		item.setAttribute("subscription", "remove");

		// Append Children
		query.appendChild(item);
		remove.appendChild(query);

		// Send removal
		xmppWriter.writeIndividualElement(remove);

		// Create unsubscribe presence
		Element unsub = xmppWriter.createElement("presence");
		unsub.setAttribute("id", "unsub1");
		unsub.setAttribute("to", contact.getBareJid());
		unsub.setAttribute("type", "unsubscribe");

		// Send ubsubscription
		xmppWriter.writeIndividualElement(unsub);
	}

	/**
	 * Send a chat message to a specific contact.
	 * 
	 * @param message
	 *            Message to be sent.
	 * @throws XMPPException
	 *             If there was a problem sending the message.
	 */
	public void sendMessage(Message message) throws XMPPException {

		/* YOUR CODE HERE */
		// Create Elements
		Element m = xmppWriter.createElement("message");
		Element b = xmppWriter.createElement("body");

		// Set Attributes
		m.setAttribute("from", session.getUserJid());
		m.setAttribute("id", message.getTimestamp().toString());
		m.setAttribute("to", message.getTo().getBareJid());
		m.setAttribute("type", "chat");
		m.setAttribute("xml:lang", "en");
		b.setTextContent(message.getTextMessage());

		// Append Children
		m.appendChild(b);

		// Send message
		xmppWriter.writeIndividualElement(m);
	}
}
