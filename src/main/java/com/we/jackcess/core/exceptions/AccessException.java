package com.we.jackcess.core.exceptions;

public abstract class AccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7536260403262956629L;

	public AccessException(String message, Throwable cause){
		super(message, cause);
	}
	
	public AccessException(String message){
		super(message);
	}

}
