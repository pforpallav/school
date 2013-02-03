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

/**
 * Enumeration of possible contact presence status.
 */
public enum ContactStatus {
	/**
	 * User/contact is actively willing to chat.
	 */
	CHAT(true, "chat", "Available for chat"),
	/**
	 * User/contact is available for chat.
	 */
	AVAILABLE(true, null, "Available"),
	/**
	 * User/contact is temporarily away and may not respond to chat.
	 */
	AWAY(true, "away", "Away"),
	/**
	 * User/contact is busy and requested not to be disturbed.
	 */
	DND(true, "dnd", "Busy"),
	/**
	 * User/contact is away for an extended period of time.
	 */
	XA(true, "xa", "Extended Away"),
	/**
	 * User/contact is not available for chat.
	 */
	OFFLINE(false, null, "Offline");

	private boolean online;
	private String xmppShow;
	private String userFriendlyName;

	private ContactStatus(boolean online, String xmppShow,
			String userFriendlyName) {
		this.online = online;
		this.xmppShow = xmppShow;
		this.userFriendlyName = userFriendlyName;
	}

	/**
	 * Returns the contact status corresponding to a specific show string.
	 * 
	 * @param the show string to be found.
	 * @return a ContactStatus with the specified show string.
	 */
	public static ContactStatus getContactStatus(String xmppShow) {
		if (xmppShow == null)
			return AVAILABLE;
		else if (ContactStatus.valueOf(xmppShow.toUpperCase()) != null)
			return ContactStatus.valueOf(xmppShow.toUpperCase());
		else
			return null;
	}
	
	/**
	 * Indicates if the current status is an online status, i.e., if the
	 * user/contact is actually logged in.
	 * 
	 * @return <code>true</code> if the status indicates that the user/contact
	 *         is online, <code>false</code> if the user/contact is offline.
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * Indicates the XMPP string used for the show tag in XMPP for this status.
	 * 
	 * @return Text content of show tag in XMPP, or null if status should not
	 *         include a show tag.
	 */
	public String getXmppShow() {
		return xmppShow;
	}

	/**
	 * Returns a user-friendly name for the contact status.
	 * @return A user-friendly name for the status.
	 */
	public String getUserFriendlyName() {
		return userFriendlyName;
	}

	@Override
	public String toString() {
		return this.userFriendlyName;
	}
}