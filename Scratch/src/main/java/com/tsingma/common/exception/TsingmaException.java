package com.tsingma.common.exception;

/**
 * 自定义异常
 * @author TSM
 *
 */
public class TsingmaException extends Exception {

	private static final long serialVersionUID = 1847276904268331715L;

	public TsingmaException () {
		
	}
	
	public TsingmaException(String message){
		super(message);
	}
}
