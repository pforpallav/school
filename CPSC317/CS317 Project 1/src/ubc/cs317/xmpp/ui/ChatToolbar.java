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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class ChatToolbar extends JToolBar {

	private MainWindow main;
	private JButton addContactButton;
	private JButton disconnectButton;

	public ChatToolbar(MainWindow mainWindow) {
		
		this.main = mainWindow;
		
		setFloatable(false);
		
		disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.disconnect(true);
			}
		});
		this.add(disconnectButton);
		
		addContactButton = new JButton("Add contact");
		addContactButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				main.showAddContactDialog();
			}
		});
		this.add(addContactButton);
	}
	
}
