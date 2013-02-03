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

package ubc.cs317.xmpp.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.SwingUtilities;

import ubc.cs317.xmpp.model.Contact;
import ubc.cs317.xmpp.model.Session;
import ubc.cs317.xmpp.model.listener.ContactListener;

public class ContactListModel extends AbstractListModel implements
		ContactListener {

	private List<Contact> contactList;

	public ContactListModel(Session session) {
		this.setSession(session);
	}

	@Override
	public synchronized int getSize() {
		return contactList.size();
	}

	@Override
	public synchronized Object getElementAt(int index) {
		return contactList.get(index);
	}

	public void setSession(Session session) {
		this.contactList = Collections.emptyList();
		if (session != null)
			this.contactList = new ArrayList<Contact>(session.getContacts());
		fireContentsChanged(this, 0, getSize());

		if (session != null) {
			session.addContactListener(this);
		}
	}

	@Override
	public synchronized void contactAdded(Contact contact) {
		contactList.add(contact);
		Collections.sort(contactList);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				fireContentsChanged(this, 0, getSize());
			}
		});
	}

	@Override
	public synchronized void contactChanged(Contact contact) {
		int pos = contactList.indexOf(contact);
		fireContentsChanged(ContactListModel.this, pos, pos);
	}

	@Override
	public synchronized void contactRemoved(Contact contact) {
		int pos = contactList.indexOf(contact);
		contactList.remove(pos);
		fireIntervalRemoved(ContactListModel.this, pos, pos);
	}
}