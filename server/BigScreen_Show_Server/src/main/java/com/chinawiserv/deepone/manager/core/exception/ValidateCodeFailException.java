package com.chinawiserv.deepone.manager.core.exception;

/**
 * 从自定义异常派生的消息异常派生的效验码错误异常
 * <pre>
 * 当效验码错误时，将抛此异常
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class ValidateCodeFailException extends LoginFailException {

	private static final long serialVersionUID = -1530680152575807626L;
	
	/**
	 * 构造详细消息为 指定消息 的新异常
	 * @author zengpzh
	 */
	public ValidateCodeFailException() {
		super("温馨提示：效验码错误，请检查效验码是否正确后再试！");
	}

	/**
	 * 构造详细消息为 message 的新异常
	 * @author zengpzh
	 */
	public ValidateCodeFailException(String message) {
		super(message);
	}
	
	/**
	 * 构造详细消息为 message 原因为 cause 的新异常
	 * @author zengpzh
	 */
	public ValidateCodeFailException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造原因为 cause 的新异常
	 * @author zengpzh
	 */
	public ValidateCodeFailException(Throwable cause) {
		super(cause);
	}		
}