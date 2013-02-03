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

public class AvailableIcon implements Icon {

	private int size;

	public AvailableIcon(int size) {
		this.size = size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int shadowDelta = size/20;
		// Shadow
		g.setColor(Color.DARK_GRAY);
		g.fillOval(x + shadowDelta, y + shadowDelta, size - shadowDelta, size - shadowDelta);
		// Green circle
		g.setColor(Color.GREEN);
		g.fillOval(x, y, size - shadowDelta, size - shadowDelta);
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
