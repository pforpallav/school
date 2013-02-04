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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.Contact;

public class AddContactDialog extends JDialog implements ActionListener {

	private MainWindow mainWindow;

	private JLabel jidFirstLabel, jidAtLabel, aliasLabel;
	private JTextField jidUsernameField, jidDomainField, aliasField;
	private JButton addButton;
	private JButton cancelButton;

	private GenericFormPanel formPanel;

	public AddContactDialog(MainWindow mainWindow, String initialJid) {

		super(mainWindow, Dialog.ModalityType.APPLICATION_MODAL);

		this.mainWindow = mainWindow;

		String[] jid = initialJid.split("@");
		if (jid.length != 2)
			jid = new String[] { "", "" };

		jidFirstLabel = new JLabel("Contact ID (JID): ");
		jidUsernameField = new JTextField(jid[0], 10);
		jidFirstLabel.setLabelFor(jidUsernameField);
		jidUsernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '@') {
					e.consume();
					jidDomainField.grabFocus();
					jidDomainField.setCaretPosition(0);
				}
			}
		});

		jidAtLabel = new JLabel("@");
		jidDomainField = new JTextField(jid[1], 10);
		jidAtLabel.setLabelFor(jidDomainField);

		aliasLabel = new JLabel("Alias: ");
		aliasField = new JTextField(10);
		aliasLabel.setLabelFor(aliasField);

		addButton = new JButton("Add");
		addButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddContactDialog.this.dispose();
			}
		});

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		formPanel = new GenericFormPanel();
		// formPanel.setLabelSizeBasedOnMaxLabel(jidFirstLabel, resourceLabel,
		// passwordLabel);

		formPanel.addLineOfFields(jidFirstLabel, jidUsernameField, jidAtLabel,
				jidDomainField);
		formPanel.addLineOfFields(aliasLabel, aliasField);

		formPanel.addButton(addButton);
		formPanel.addButton(cancelButton);

		this.add(formPanel);

		this.getRootPane().setDefaultButton(addButton);

		this.setSize(600, 200);
		this.setLocation((mainWindow.getWidth() - this.getWidth()) / 2,
				(mainWindow.getHeight() - this.getHeight()) / 2);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		try {

			Contact contact = new Contact(jidUsernameField.getText() + "@"
					+ jidDomainField.getText(), aliasField.getText());
			mainWindow.getSession().sendNewContactRequest(contact);
			this.setVisible(false);

		} catch (XMPPException e) {
			JOptionPane.showMessageDialog(mainWindow, e);
		}
	}
}
