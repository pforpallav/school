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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.Contact;
import ubc.cs317.xmpp.model.ContactStatus;
import ubc.cs317.xmpp.model.Session;

public class ContactListPanel extends JPanel implements ListCellRenderer {

	private static final int STATUS_ICON_SIZE = 40;

	private MainWindow main;

	private JList contactList;
	private JScrollPane contactListScrollPane;
	private JComboBox statusSelect;
	private ContactListModel contactListModel;
	private JPopupMenu contactPopup;
	private JMenuItem contactRemoveMenuItem;

	public ContactListPanel(MainWindow mainWindow) {

		this.main = mainWindow;

		contactListModel = new ContactListModel(mainWindow.getSession());

		contactList = new JList(contactListModel);
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactList.setCellRenderer(this);
		contactList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent x) {
				if (x.getClickCount() == 2
						&& x.getButton() == MouseEvent.BUTTON1) {
					Contact contact = (Contact) contactList.getSelectedValue();
					main.createChatPanel(contact, true);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showContactPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showContactPopup(e);
			}

			private void showContactPopup(MouseEvent e) {
				contactList.setSelectedIndex(contactList.locationToIndex(e.getPoint()));
				contactPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		});

		contactPopup = new JPopupMenu();
		contactRemoveMenuItem = new JMenuItem("Remove contact");
		contactPopup.add(contactRemoveMenuItem);
		contactRemoveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (contactList.getSelectedValue() != null)
					try {
						main.getSession().sendRequestToRemoveContact(
								(Contact) contactList.getSelectedValue());
					} catch (XMPPException e) {
						JOptionPane.showMessageDialog(main,
								"Contact could not be removed. Cause: " + e);
					}
			}
		});

		contactListScrollPane = new JScrollPane(contactList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contactListScrollPane.setPreferredSize(new Dimension(200, 1));

		statusSelect = new JComboBox(
				new ContactStatus[] { ContactStatus.AVAILABLE,
						ContactStatus.AWAY, ContactStatus.DND });
		statusSelect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					ContactListPanel.this.main.getSession().setAndSendCurrentStatus(
							(ContactStatus) statusSelect.getSelectedItem());
				} catch (XMPPException e) {
					JOptionPane.showMessageDialog(main, e.getMessage());
				}
			}
		});

		this.setLayout(new BorderLayout());
		this.add(contactListScrollPane);
		this.add(statusSelect, BorderLayout.NORTH);
	}

	@Override
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean hasFocus) {

		Contact contact = (Contact) value;
		JLabel label = new JLabel(contact.toString(), main.getStatusIcon(
				contact.getStatus(), STATUS_ICON_SIZE), SwingConstants.LEFT);
		label.setOpaque(true);
		label.setBackground(isSelected ? Color.BLUE : Color.WHITE);
		label.setForeground(isSelected ? Color.LIGHT_GRAY : Color.BLACK);
		return label;
	}

	public void setSession(Session session) {
		contactListModel.setSession(session);
	}

	public ContactStatus getSelectedStatus() {
		return (ContactStatus) statusSelect.getSelectedItem();
	}
}
