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

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.Contact;
import ubc.cs317.xmpp.model.ContactStatus;
import ubc.cs317.xmpp.model.Message;
import ubc.cs317.xmpp.model.Session;
import ubc.cs317.xmpp.model.listener.ContactListener;
import ubc.cs317.xmpp.model.listener.MessageListener;
import ubc.cs317.xmpp.model.listener.SessionListener;
import ubc.cs317.xmpp.model.listener.SubscriptionRequestListener;
import ubc.cs317.xmpp.ui.images.AvailableIcon;
import ubc.cs317.xmpp.ui.images.AwayIcon;
import ubc.cs317.xmpp.ui.images.BusyIcon;
import ubc.cs317.xmpp.ui.images.OfflineIcon;

public class MainWindow extends JFrame implements MessageListener,
		ContactListener, SubscriptionRequestListener, SessionListener {

	private Session session;

	private ContactListPanel contactListPanel;

	private JTabbedPane chatTabbedPanel = null;
	private Map<Contact, ChatPanel> chatPanels = new HashMap<Contact, ChatPanel>();

	private ChatToolbar chatToolbar;

	public MainWindow() {

		super("Simple Chat Client");

		contactListPanel = new ContactListPanel(this);
		chatTabbedPanel = new JTabbedPane();
		chatToolbar = new ChatToolbar(this);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (session == null
						|| JOptionPane
								.showConfirmDialog(
										MainWindow.this,
										"This will disconnect your current session. Are you sure?",
										"Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					disconnect(false);
				}
			}
		});
		chatTabbedPanel.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				ChatPanel panel = (ChatPanel) chatTabbedPanel
						.getSelectedComponent();
				if (panel != null)
					panel.grabFocus();
			}
		});

		this.setLayout(new BorderLayout(1, 1));
		this.add(chatTabbedPanel, BorderLayout.CENTER);
		this.add(contactListPanel, BorderLayout.WEST);
		this.add(chatToolbar, BorderLayout.PAGE_START);

		this.setSize(1024, 600);

		showLoginDialog();
	}

	/**
	 * 
	 */
	private void showLoginDialog() {
		new LoginDialog(this);
	}

	public static void main(String[] args) {

		new MainWindow();
	}

	public ChatPanel createChatPanel(Contact contact, boolean bringToFront) {

		ChatPanel chatPanel = chatPanels.get(contact);

		if (chatPanel == null) {
			chatPanel = new ChatPanel(this, contact);
			chatPanels.put(contact, chatPanel);
			chatTabbedPanel.addTab(contact.toString(), chatPanel);
		}

		if (bringToFront) {
			chatTabbedPanel.setSelectedComponent(chatPanel);
			chatPanel.grabFocus();
			updateChatPanelTitle(chatPanel, false);
		}

		this.contactChanged(contact);
		return chatPanel;
	}

	public Session getSession() {
		return this.session;
	}

	public void setSession(Session session) {
		if (this.session == session)
			return;
		this.session = session;
		contactListPanel.setSession(session);
		chatTabbedPanel.removeAll();
		chatPanels.clear();
		
		if (session == null)
			this.setTitle("Simple Chat Client");
		else {
			this.setTitle("Simple Chat Client - " + session.getUserJid());
			session.addContactListener(this);
			session.addSubscriptionRequestListener(this);
			session.addMessageListener(this);
			session.addSessionListener(this);
		}
	}

	public ContactStatus getSelectedStatus() {
		return contactListPanel.getSelectedStatus();
	}

	public void showAddContactDialog() {
		new AddContactDialog(this, "");
	}

	public synchronized void disconnect(boolean showNewLoginDialog) {
		if (session != null) {
			// Removes the session listener so that the confirmation dialog and
			// disconnect call are not executed again.
			session.removeSessionListener(this);
			session.closeConnection();
		}
		setSession(null);
		if (showNewLoginDialog)
			showLoginDialog();
		else
			System.exit(0);
	}

	public void updateChatPanelTitle(ChatPanel chatPanel, boolean unread) {

		Contact contact = chatPanel.getConversation().getContact();
		int index = chatTabbedPanel.indexOfComponent(chatPanel);

		if (unread)
			this.toFront();

		if (unread && chatTabbedPanel.getSelectedIndex() != index)
			chatTabbedPanel.setTitleAt(index, "* " + contact.toString());
		else
			chatTabbedPanel.setTitleAt(index, contact.toString());
	}

	public Icon getStatusIcon(ContactStatus status, int size) {

		switch (status) {
		case AVAILABLE:
		case CHAT:
			return new AvailableIcon(size);
		case AWAY:
			return new AwayIcon(size);
		case DND:
		case XA:
			return new BusyIcon(size);
		case OFFLINE:
		default:
			return new OfflineIcon(size);
		}
	}

	@Override
	public void messageReceived(Message message) {
		if (chatPanels.get(message.getFrom()) == null) {
			createChatPanel(message.getFrom(), false);
			updateChatPanelTitle(chatPanels.get(message.getFrom()), true);
		}
	}

	@Override
	public void messageSent(Message message) {
		// Nothing to do
	}

	@Override
	public void contactAdded(Contact contact) {
		// Nothing to do
	}

	@Override
	public void contactRemoved(Contact contact) {
		// Nothing to do
	}

	@Override
	public void contactChanged(Contact contact) {
		ChatPanel chatPanel = chatPanels.get(contact);
		if (chatPanel != null) {
			int index = chatTabbedPanel.indexOfComponent(chatPanel);
			if (index >= 0)
				chatTabbedPanel.setIconAt(index,
						getStatusIcon(contact.getStatus(), 20));
		}
	}

	@Override
	public void subscriptionRequested(String jid) {

		int result = JOptionPane.showConfirmDialog(this, "The user " + jid
				+ " wants to know when you are online. Do you want to accept?",
				"Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION)
			return;

		try {
			session.respondContactRequest(jid, result == JOptionPane.YES_OPTION);

			if (session.getContact(jid) == null)
				new AddContactDialog(this, jid);

		} catch (XMPPException e) {
			JOptionPane.showMessageDialog(this, e);
		}

	}

	@Override
	public void readingExceptionThrown(XMPPException exception) {
		JOptionPane.showMessageDialog(this, exception.getMessage());
	}

	@Override
	public void sessionClosed() {
		disconnect(JOptionPane
				.showConfirmDialog(
						this,
						"The connection with the server was closed. Do you want to start a new connection?",
						"Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}
}
