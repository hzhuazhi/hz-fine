package com.hz.fine.master.core.common.exception;

/**
 * Service层公用的Exception.
 * 
 * 继承自Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 * 
 * @author yoko
 * 2018-07-06
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 3583566093089790852L;

	private String code;
	private String message;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
		this.message = message;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}


	/**
	 *
	 * Creates a new instance of AccessActivityException.
	 *
	 * @param code
	 * @param message
	 */
	public ServiceException(String code, String message){
		super(message);
		this.code = code;
		this.message = message;
	}


	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}