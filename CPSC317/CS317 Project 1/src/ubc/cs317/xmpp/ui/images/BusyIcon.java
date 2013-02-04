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

package ubc.cs317.xmpp.ui.images;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class BusyIcon implements Icon {

	private int size;

	public BusyIcon(int size) {
		this.size = size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// Red circle
		g.setColor(Color.RED);
		g.fillOval(x, y, size, size);
		// White rectangle in the middle
		g.setColor(Color.WHITE);
		g.fillRect(x + size / 10, y + size * 2 / 5, size * 8 / 10, size / 5);
	}

	@Override
	public int getIconWidth() {
		return size;
	}

	@Override
	public int getIconHeight() {
		return size;
	}
}
