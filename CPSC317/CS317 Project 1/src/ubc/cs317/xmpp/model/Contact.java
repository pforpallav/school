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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ubc.cs317.xmpp.model.listener.ContactListener;

/**
 * This class represents contacts in the user's list of contacts.
 */
public class Contact implements Comparable<Contact> {

	/**
	 * Bare Jabber ID, including the local part (user) and the domain of the
	 * contact, but not the resource.
	 */
	private String bareJid;
	/**
	 * Alias used to represent this contact to the user.
	 */
	private String alias;
	/**
	 * A mapping between different resources associated to the user and their
	 * last advertised status.
	 */
	private Map<String, ContactStatus> resourceStatus = new HashMap<String, ContactStatus>();
	/**
	 * The last resource used by the contact to send a message to the local
	 * user. This resource is used in future messages to target a single
	 * resource instead of the bare JID.
	 */
	private String lockedResource;

	/**
	 * Set of contact listeners to be called when this contact is changed.
	 */
	private Set<ContactListener> contactListeners = new HashSet<ContactListener>();

	/**
	 * Creates a contact with the provided JID and alias.
	 * 
	 * @param bareJid
	 *            Bare JID (without resource) of the contact.
	 * @param alias
	 *            Alias for the contact.
	 */
	public Contact(String bareJid, String alias) {
		if (bareJid == null)
			throw new NullPointerException();
		if (!bareJid.contains("@"))
			throw new InvalidParameterException("Invalid JID");
		this.bareJid = bareJid;
		setAlias(alias);
	}

	/**
	 * Returns the alias for the contact.
	 * 
	 * @return Alias of the contact.
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets a new alias for the contact.
	 * 
	 * @param alias
	 *            New alias of the contact.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
		triggerContactChanged();
	}

	/**
	 * Returns the bare JID (without resource) of the contact.
	 * 
	 * @return Bare JID of the contact.
	 */
	public String getBareJid() {
		return bareJid;
	}

	/**
	 * Returns the JID of the contact, including the last resource used by this
	 * contact to send a message. If no message was yet sent by the contact, or
	 * if a change in status happened since the last message was received, the
	 * bare JID is returned instead.
	 * 
	 * @return Full JID of the contact, or bare JID if there is no locked
	 *         resource.
	 */
	public synchronized String getFullJid() {
		if (lockedResource == null)
			return getBareJid();
		else
			return bareJid + "/" + lockedResource;
	}

	/**
	 * Locks the current resource of the contact. This method is called when a
	 * new message is received from this contact. Any future communication to
	 * this contact should be sent to that resource, unless the contact's status
	 * changes.
	 * 
	 * @param resource
	 *            The new resource to be locked, or null if the resource should
	 *            be unlocked.
	 */
	public synchronized void lockResource(String resource) {
		this.lockedResource = resource;
	}

	/**
	 * Returns the current status of the contact. This status is computed based
	 * on the "most available" status of any resource that advertised its
	 * status.
	 * 
	 * @return The contact's computed status.
	 */
	public ContactStatus getStatus() {
		ContactStatus status = ContactStatus.OFFLINE;
		for (ContactStatus individualStatus : this.resourceStatus.values())
			if (individualStatus.compareTo(status) < 0)
				status = individualStatus;
		return status;
	}

	/**
	 * Sets the status of a specific resource of this contact. If the status if
	 * <code>OFFLINE</code> and the resource is null, removes all information
	 * about contacts and sets the status to <code>OFFLINE</code>.
	 * 
	 * @param resource
	 *            The resource whose status should be set/changed.
	 * @param status
	 *            The new status of the specified resource.
	 */
	public void setStatus(String resource, ContactStatus status) {
		if (status != ContactStatus.OFFLINE)
			this.resourceStatus.put(resource, status);
		else if (resource == null)
			this.resourceStatus.clear();
		else
			this.resourceStatus.remove(resource);
		this.lockResource(null);
		triggerContactChanged();
	}

	/**
	 * Returns a string representation of this contact. If the alias is neither
	 * null nor empty, it is used, otherwise the bare JID is used.
	 */
	@Override
	public String toString() {
		if (this.alias != null && !this.alias.trim().equals(""))
			return this.alias;
		else if (this.bareJid != null)
			return this.bareJid;
		else
			return "";
	}

	@Override
	public int hashCode() {
		return this.bareJid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (bareJid == null)
			return other.bareJid == null;
		else
			return bareJid.equals(other.bareJid);
	}

	@Override
	public int compareTo(Contact other) {
		return this.toString().compareToIgnoreCase(other.toString());
	}

	/**
	 * Adds a listener to be called every time this contact is changed.
	 * 
	 * @param listener
	 *            Listener to be called when there is a contact change event.
	 */
	public void addContactListener(ContactListener listener) {
		this.contactListeners.add(listener);
	}

	private void triggerContactChanged() {
		for (ContactListener listener : contactListeners) {
			listener.contactChanged(this);
		}
	}
}
