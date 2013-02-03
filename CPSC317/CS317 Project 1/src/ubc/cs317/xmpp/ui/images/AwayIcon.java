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

public class AwayIcon implements Icon {

	private int size;

	public AwayIcon(int size) {
		this.size = size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// Blue circumference (around clock)
		g.setColor(Color.BLUE);
		g.fillOval(x, y, size, size);
		g.setColor(Color.WHITE);
		g.fillOval(x + size / 10, y + size / 10, size * 8 / 10, size * 8 / 10);
		// Clock pointers (hours and minutes)
		g.setColor(Color.BLACK);
		g.fillRoundRect(x + (int) Math.ceil(size * .45), y + size / 4,
				size / 10, size / 4, size / 10, size / 10);
		g.fillRoundRect(x + size / 2, y + (int) Math.ceil(size * .45),
				size * 2 / 5, size / 10, size / 10, size / 10);
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
