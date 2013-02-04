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
import java.util.Date;

/**
 * Class that represents chat messages sent and received between the user and
 * one of its contacts.
 */
public class Message {

	/**
	 * Contact that sent the message. It is null if the message was sent by the
	 * local user.
	 */
	private Contact from;
	/**
	 * Contact to whom the message was intended. It is null if the message was
	 * sent to the local user.
	 */
	private Contact to;
	/**
	 * Body of the sent message.
	 */
	private String textMessage;
	/**
	 * Date/time when the message was sent or received.
	 */
	private Date timestamp;

	/**
	 * Creates a new message with the provided contacts, text message and
	 * timestamp.
	 * 
	 * @param from
	 *            Contact that sent the message. Should be null if the local
	 *            user sent the message.
	 * @param to
	 *            Contact to whom the message was intended. Should be null if
	 *            the message was sent to the local user.
	 * @param textMessage
	 *            Body of the message.
	 * @param timestamp
	 *            Date/time when the message was sent or received.
	 */
	public Message(Contact from, Contact to, String textMessage, Date timestamp) {
		if (from == null && to == null)
			throw new InvalidParameterException(
					"At least one contact should be informed.");
		if (textMessage == null)
			throw new NullPointerException();
		this.from = from;
		this.to = to;
		this.textMessage = textMessage;
		this.timestamp = timestamp;
	}

	/**
	 * Creates a new message with the provided contacts and text message, using
	 * the current date and time as a timestamp.
	 * 
	 * @param from
	 *            Contact that sent the message. Should be null if the local
	 *            user sent the message.
	 * @param to
	 *            Contact to whom the message was intended. Should be null if
	 *            the message was sent to the local user.
	 * @param textMessage
	 *            Body of the message.
	 */
	public Message(Contact from, Contact to, String textMessage) {
		this(from, to, textMessage, new Date());
	}

	/**
	 * Returns the contact that sent the message.
	 * 
	 * @return Contact that sent the message, or null if the local user sent the
	 *         message.
	 */
	public Contact getFrom() {
		return from;
	}

	/**
	 * Returns the contact to whom the message was sent.
	 * 
	 * @return Contact to whom the message was intended, or null if the message
	 *         was sent to the local user.
	 */
	public Contact getTo() {
		return to;
	}

	/**
	 * Returns the body of the message.
	 * 
	 * @return Body of the message.
	 */
	public String getTextMessage() {
		return textMessage;
	}

	/**
	 * Returns the date/time when the message was sent or received.
	 * 
	 * @return Date/time when message was sent or received.
	 */
	public Date getTimestamp() {
		return timestamp;
	}
}
