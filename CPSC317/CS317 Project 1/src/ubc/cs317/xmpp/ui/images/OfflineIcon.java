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

public class OfflineIcon implements Icon {

	private int size;

	public OfflineIcon(int size) {
		this.size = size;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		// Gray circumference
		g.setColor(Color.DARK_GRAY);
		g.fillOval(x, y, size, size);
		g.setColor(Color.WHITE);
		g.fillOval(x + size / 10, y + size / 10, size * 8 / 10, size * 8 / 10);
		
		// Prohibition strip
		g.setColor(Color.DARK_GRAY);
		int midx = x + size / 2, midy = y + size / 2;
		int diff = (int) (Math.PI * size / 10);
		int width = size / 20;
		g.fillPolygon(new int[] { midx + diff - width, midx + diff + width,
				midx - diff + width, midx - diff - width }, new int[] {
				midy - diff - width, midy - diff + width, midy + diff + width,
				midy + diff - width }, 4);
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
