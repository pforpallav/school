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

package ubc.cs317.xmpp.model.listener;

import ubc.cs317.xmpp.exception.XMPPException;

public interface SessionListener {

	public void readingExceptionThrown(XMPPException exception);

	public void sessionClosed();
}
