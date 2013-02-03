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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DateFormat;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import ubc.cs317.xmpp.exception.XMPPException;
import ubc.cs317.xmpp.model.Contact;
import ubc.cs317.xmpp.model.Conversation;
import ubc.cs317.xmpp.model.Message;
import ubc.cs317.xmpp.model.listener.MessageListener;

public class ChatPanel extends JPanel implements MessageListener {

	public static final DateFormat TIMESTAMP_FORMAT = DateFormat
			.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);

	private MainWindow main;

	private Conversation conversation;

	private JEditorPane chatArea;
	private JPanel sendMessagePanel;
	private JTextField sendMessageText;
	private JButton sendMessageButton;

	public ChatPanel(MainWindow mainWindow, Contact contact) {

		this.main = mainWindow;

		conversation = main.getSession().getConversation(contact);
		conversation.addMessageListener(this);

		chatArea = new JEditorPane();
		chatArea.setAutoscrolls(true);
		chatArea.setEditable(false);
		chatArea.setContentType("text/html");
		chatArea.setText("<html><body style='margin: 0'></body></html>");
		chatArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				sendMessageText.grabFocus();
				sendMessageText.dispatchEvent(e);
			}
		});

		sendMessagePanel = new JPanel();

		sendMessageText = new JTextField();
		sendMessageText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n')
					sendMessageButton.doClick();
			}
		});

		sendMessageButton = new JButton("Send");
		sendMessageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!sendMessageText.getText().trim().equals("")) {
					try {
						sendMessage(new Message(null,
								conversation.getContact(), sendMessageText
										.getText()));
						sendMessageText.setText("");
					} catch (XMPPException e) {
						JOptionPane.showMessageDialog(main, e.getMessage());
					}
				}
				sendMessageText.grabFocus();
			}
		});

		sendMessagePanel.setLayout(new BorderLayout());
		sendMessagePanel.add(sendMessageText, BorderLayout.CENTER);
		sendMessagePanel.add(sendMessageButton, BorderLayout.EAST);

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(chatArea), BorderLayout.CENTER);
		this.add(sendMessagePanel, BorderLayout.SOUTH);

		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				sendMessageText.grabFocus();
				main.updateChatPanelTitle(ChatPanel.this, false);
			}
		});

		updateChatArea();
	}

	public Contact getContact() {
		return conversation.getContact();
	}

	public synchronized void updateChatArea() {
		HTMLDocument doc = (HTMLDocument) new HTMLEditorKit()
				.createDefaultDocument();
		Element body = doc.getElement(doc.getDefaultRootElement(),
				StyleConstants.NameAttribute, HTML.Tag.BODY);
		for (Message message : conversation.getMessageList()) {
			try {
				doc.insertBeforeEnd(
						body,
						"<P style='margin: 0'>("
								+ TIMESTAMP_FORMAT.format(message
										.getTimestamp())
								+ ") <b>"
								+ (message.getFrom() == null ? "me" : message
										.getFrom()) + "</b>: "
								+ message.getTextMessage() + "</P>");
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chatArea.setDocument(doc);
		chatArea.setCaretPosition(doc.getLength());
	}

	private synchronized void sendMessage(Message message) throws XMPPException {
		conversation.addOutgoingMessage(message);
	}

	@Override
	public void messageReceived(Message message) {
		updateChatArea();
		main.updateChatPanelTitle(this, true);
	}

	@Override
	public void messageSent(Message message) {
		updateChatArea();
	}

	public Conversation getConversation() {
		return this.conversation;
	}
}
