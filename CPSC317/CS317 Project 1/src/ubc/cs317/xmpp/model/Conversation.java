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

package ubc.cs317.xmpp.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.listener.MessageListener;

/**
 * This class represents a conversation thread with a specific contact.
 */
public class Conversation {

	/**
	 * Session object used for communication with the server.
	 */
	private Session session;
	/**
	 * Contact associated to this conversation.
	 */
	private Contact contact;
	/**
	 * List of messages included in this conversation.
	 */
	private List<Message> messageList;

	/**
	 * Message listeners that should be called when message events happen in
	 * this specific conversation. This list will include any conversation
	 * specified for the session as well.
	 */
	private Set<MessageListener> messageListeners = new HashSet<MessageListener>();

	/**
	 * Creates a conversation for a specific contact in the specified session.
	 * 
	 * @param session
	 *            Session object used for communication with the server.
	 * @param contact
	 *            Contact to be associated to this conversation.
	 */
	public Conversation(Session session, Contact contact) {
		if (session == null || contact == null)
			throw new NullPointerException();
		this.session = session;
		this.contact = contact;
		this.messageList = new ArrayList<Message>();
	}

	/**
	 * Adds a MessageListener to be called when there is a new message in this
	 * conversation.
	 * 
	 * @param listener
	 *            Listener to be called for new message events.
	 */
	public void addMessageListener(MessageListener listener) {
		this.messageListeners.add(listener);
	}

	/**
	 * Adds a message sent from the user to the contact to the list of messages,
	 * and sends it to the server.
	 * 
	 * @param message
	 *            Message to be added.
	 * @throws XMPPException
	 *             If there is a problem sending the message to the server.
	 */
	public void addOutgoingMessage(Message message) throws XMPPException {
		if (message.getFrom() != null || !contact.equals(message.getTo()))
			throw new InvalidParameterException(
					"Message not associated to this conversation.");
		session.sendMessage(message);
		this.messageList.add(message);

		for (MessageListener listener : messageListeners)
			listener.messageSent(message);
	}

	/**
	 * Adds a message received from the contact to the user to the list of
	 * messages.
	 * 
	 * @param message
	 *            Message to be added.
	 * @param resource
	 *            Resource used by the contact to send this message.
	 */
	public void addIncomingMessage(Message message, String resource) {

		if (message.getTo() != null || !contact.equals(message.getFrom()))
			throw new InvalidParameterException(
					"Message not associated to this conversation.");

		this.messageList.add(message);
		message.getFrom().lockResource(resource);

		for (MessageListener listener : messageListeners)
			listener.messageReceived(message);
	}

	/**
	 * Returns the contact associated to this conversation.
	 * 
	 * @return Contact associated to this conversation.
	 */
	public Contact getContact() {
		return this.contact;
	}

	/**
	 * Returns the list of messages. An unmodifiable list is returned.
	 * 
	 * @return List of messages in this conversation.
	 */
	public List<Message> getMessageList() {
		return Collections.unmodifiableList(this.messageList);
	}
}
