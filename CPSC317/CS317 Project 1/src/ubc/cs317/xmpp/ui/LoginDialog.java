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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.Session;

public class LoginDialog extends JDialog implements ActionListener {

	private MainWindow mainWindow;

	private JLabel jidFirstLabel, jidAtLabel, resourceLabel, passwordLabel;
	private JTextField jidUsernameField, jidDomainField, resourceField;
	private JPasswordField passwordField;
	private JCheckBox savePasswordCheckbox;
	private JButton connectButton;
	private JButton cancelButton;

	private GenericFormPanel formPanel;

	public LoginDialog(MainWindow mainWindow) {

		super(mainWindow, Dialog.ModalityType.APPLICATION_MODAL);

		this.mainWindow = mainWindow;

		jidFirstLabel = new JLabel("XMMP User ID (JID): ");
		jidUsernameField = new JTextField(10);
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
		jidDomainField = new JTextField(10);
		jidAtLabel.setLabelFor(jidDomainField);
		jidDomainField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '/') {
					e.consume();
					resourceField.grabFocus();
					resourceField.setCaretPosition(0);
				}
			}
		});

		resourceLabel = new JLabel("Resource (optional): ");
		resourceField = new JTextField(10);
		resourceLabel.setLabelFor(resourceField);

		passwordLabel = new JLabel("Password: ");
		passwordField = new JPasswordField(10);
		passwordLabel.setLabelFor(passwordField);

		savePasswordCheckbox = new JCheckBox("Save password");

		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		formPanel = new GenericFormPanel();
		// formPanel.setLabelSizeBasedOnMaxLabel(jidFirstLabel, resourceLabel,
		// passwordLabel);

		formPanel.addLineOfFields(jidFirstLabel, jidUsernameField, jidAtLabel,
				jidDomainField);
		formPanel.addLineOfFields(resourceLabel, resourceField);
		formPanel.addLineOfFields(passwordLabel, passwordField);
		formPanel.addLineOfFields(null, savePasswordCheckbox);

		formPanel.addButton(connectButton);
		formPanel.addButton(cancelButton);

		this.add(formPanel);

		this.getRootPane().setDefaultButton(connectButton);

		this.setSize(600, 200);
		this.setLocation((mainWindow.getWidth() - this.getWidth()) / 2,
				(mainWindow.getHeight() - this.getHeight()) / 2);

		loadInfo();

		mainWindow.setVisible(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		try {

			saveInfo();

			Session session = new Session(jidUsernameField.getText(),
					jidDomainField.getText(), resourceField.getText(),
					new String(passwordField.getPassword()),
					mainWindow.getSelectedStatus());
			mainWindow.setSession(session);
			this.setVisible(false);
			mainWindow.setVisible(true);

		} catch (XMPPException e) {
			JOptionPane.showMessageDialog(mainWindow, e);
		}
	}

	private File savedInfoFile = new File(System.getProperty("user.home"),
			".simplechat.txt");

	private void saveInfo() {
		try {

			PrintStream writer = new PrintStream(new FileOutputStream(
					savedInfoFile));
			writer.println(jidUsernameField.getText());
			writer.println(jidDomainField.getText());
			writer.println(resourceField.getText());
			writer.println(savePasswordCheckbox.isSelected() ? "t" : "f");
			if (savePasswordCheckbox.isSelected())
				writer.println(new String(passwordField.getPassword()));
			writer.close();

		} catch (Exception e) {
			// Ignore
			e.printStackTrace();
		}
	}

	private void loadInfo() {
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(savedInfoFile)));
			jidUsernameField.setText(reader.readLine());
			jidDomainField.setText(reader.readLine());
			resourceField.setText(reader.readLine());
			if ("t".equals(reader.readLine())) {
				savePasswordCheckbox.setSelected(true);
				passwordField.setText(reader.readLine());
			} else
				savePasswordCheckbox.setSelected(false);
			reader.close();

		} catch (Exception e) {
			// Ignore
			e.printStackTrace();
		}
	}
}
