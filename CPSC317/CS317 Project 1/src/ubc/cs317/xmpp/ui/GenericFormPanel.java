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

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

public class GenericFormPanel extends JPanel {

	public static final int WEST_BORDER_SIZE = 10;
	public static final int EAST_BORDER_SIZE = 10;
	public static final int NORTH_BORDER_SIZE = 10;
	public static final int SOUTH_BORDER_SIZE = 10;
	public static final int HORIZONTAL_DISTANCE_LABEL_FIELD = 5;
	public static final int HORIZONTAL_DISTANCE_BETWEEN_FIELDS = 5;
	public static final int VERTICAL_DISTANCE_BETWEEN_FIELDS = 5;
	public static final int HORIZONTAL_DISTANCE_BETWEEN_BUTTONS = 10;

	private SpringLayout layout;
	private Spring alignedFieldsWestSpring = Spring.constant(WEST_BORDER_SIZE
			+ HORIZONTAL_DISTANCE_LABEL_FIELD);
	private Component firstAlignedField = null;
	private Component previousAlignedField = null;
	private Component previousButton = null;

	public GenericFormPanel() {
		layout = new SpringLayout();
		this.setLayout(layout);
	}

	public void addLineOfFields(Component label, Component alignedField,
			Component... additionalFields) {

		this.add(alignedField);
		if (previousAlignedField == null) {

			firstAlignedField = alignedField;

			layout.putConstraint(SpringLayout.NORTH, alignedField,
					NORTH_BORDER_SIZE, SpringLayout.NORTH, this);
		} else {

			layout.putConstraint(SpringLayout.NORTH, alignedField,
					VERTICAL_DISTANCE_BETWEEN_FIELDS, SpringLayout.SOUTH,
					previousAlignedField);
			layout.putConstraint(SpringLayout.WEST, alignedField, 0,
					SpringLayout.WEST, previousAlignedField);
		}

		if (label != null)
			alignedFieldsWestSpring = Spring.max(
					alignedFieldsWestSpring,
					Spring.sum(
							Spring.width(label),
							Spring.constant(WEST_BORDER_SIZE
									+ HORIZONTAL_DISTANCE_LABEL_FIELD)));
		layout.putConstraint(SpringLayout.WEST, firstAlignedField,
				alignedFieldsWestSpring, SpringLayout.WEST, this);

		previousAlignedField = alignedField;

		if (label != null) {
			this.add(label);
			layout.putConstraint(SpringLayout.EAST, label,
					-HORIZONTAL_DISTANCE_LABEL_FIELD, SpringLayout.WEST,
					alignedField);
			layout.putConstraint(SpringLayout.BASELINE, label, 0,
					SpringLayout.BASELINE, alignedField);
		}

		Component previousField = alignedField;
		for (Component field : additionalFields) {
			this.add(field);
			layout.putConstraint(SpringLayout.WEST, field,
					HORIZONTAL_DISTANCE_BETWEEN_FIELDS, SpringLayout.EAST,
					previousField);
			layout.putConstraint(SpringLayout.BASELINE, field, 0,
					SpringLayout.BASELINE, previousField);
			previousField = field;
		}

		layout.putConstraint(SpringLayout.EAST, previousField,
				-EAST_BORDER_SIZE, SpringLayout.EAST, this);

	}

	public void addButton(Component button) {

		this.add(button);
		layout.putConstraint(SpringLayout.SOUTH, button, -SOUTH_BORDER_SIZE,
				SpringLayout.SOUTH, this);
		if (previousButton == null)
			layout.putConstraint(SpringLayout.EAST, button, -EAST_BORDER_SIZE,
					SpringLayout.EAST, this);
		else
			layout.putConstraint(SpringLayout.EAST, button,
					-HORIZONTAL_DISTANCE_BETWEEN_BUTTONS, SpringLayout.WEST,
					previousButton);

		previousButton = button;
	}
}
