package com.chinawiserv.deepone.manager.core.exception;

/**
 * 从自定义异常派生的消息异常派生的登录失败异常
 * <pre>
 * 登录失败时，将抛此异常
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class LoginFailException extends MsgException {

	private static final long serialVersionUID = -1530680152575807626L;
	
	/**
	 * 构造详细消息为 指定消息 的新异常
	 * @author zengpzh
	 */
	public LoginFailException() {
		super("温馨提示：登录失败，请检查登录帐号与登录密码是否正确后再试！");
	}

	/**
	 * 构造详细消息为 message 的新异常
	 * @author zengpzh
	 */
	public LoginFailException(String message) {
		super(message);
	}
	
	/**
	 * 构造详细消息为 message 原因为 cause 的新异常
	 * @author zengpzh
	 */
	public LoginFailException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造原因为 cause 的新异常
	 * @author zengpzh
	 */
	public LoginFailException(Throwable cause) {
		super(cause);
	}		

}
