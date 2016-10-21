package com.chinawiserv.deepone.manager.core.exception;

/**
 * 从自定义异常派生的消息异常
 * @author zengpzh
 * @version 0.1
 */
public class MsgException extends BaseException {

	private static final long serialVersionUID = -1530680152575807626L;
	
	/**
	 * 构造详细消息为 指定消息 的新异常
	 * @author zengpzh
	 */
	public MsgException() {
		super();
	}

	/**
	 * 构造详细消息为 message 的新异常
	 * @author zengpzh
	 */
	public MsgException(String message) {
		super(message);
	}
	
	/**
	 * 构造详细消息为 message 原因为 cause 的新异常
	 * @author zengpzh
	 */
	public MsgException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造原因为 cause 的新异常
	 * @author zengpzh
	 */
	public MsgException(Throwable cause) {
		super(cause);
	}		
}