package com.finstack.projectlog.exception;

public class UnauthorizedException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizedException(){
		super();
	}

	public UnauthorizedException(String message){
		super(message);
	}
}
