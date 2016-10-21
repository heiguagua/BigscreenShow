package com.chinawiserv.deepone.manager.core.exception;

/**
 * 从自定义异常派生的消息异常
 * @author zengpzh
 * @version 0.1
 */
public class ForbidMethodException extends BaseException {

	private static final long serialVersionUID = -1530680152575807626L;
	
	/**
	 * 构造详细消息为 指定消息 的新异常
	 * @author zengpzh
	 */
	public ForbidMethodException() {
		super("温馨提示：访问被禁止，为了系统相关更安全，本系统只允许以post方法访问！");
	}

	/**
	 * 构造详细消息为 message 的新异常
	 * @author zengpzh
	 */
	public ForbidMethodException(String message) {
		super(message);
	}
	
	/**
	 * 构造详细消息为 message 原因为 cause 的新异常
	 * @author zengpzh
	 */
	public ForbidMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造原因为 cause 的新异常
	 * @author zengpzh
	 */
	public ForbidMethodException(Throwable cause) {
		super(cause);
	}		
}