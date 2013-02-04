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

package ubc.cs317.xmpp.exception;

/**
 * Generic exception for XMPP problems and errors.
 */
public class XMPPException extends Exception {

	public XMPPException(String message, Throwable cause) {
		super(message, cause);
	}

	public XMPPException(String message) {
		super(message);
	}

	public XMPPException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
