package com.chinawiserv.deepone.manager.core.exception;

/**
 * 从自定义异常派生的消息异常派生的无SQL语句异常
 * <pre>
 * 当无SQL语句时，将抛此异常
 * </pre>
 * @author zengpzh
 * @version 0.1
 */
public class OffsetErrorException extends MsgException {

	private static final long serialVersionUID = -1530680152575807626L;
	
	/**
	 * 构造详细消息为 指定消息 的新异常
	 * @author zengpzh
	 */
	public OffsetErrorException() {
		super("温馨提示：查询错误，起始索引必须大于或等于零！");
	}

	/**
	 * 构造详细消息为 message 的新异常
	 * @author zengpzh
	 */
	public OffsetErrorException(String message) {
		super(message);
	}
	
	/**
	 * 构造详细消息为 message 原因为 cause 的新异常
	 * @author zengpzh
	 */
	public OffsetErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造原因为 cause 的新异常
	 * @author zengpzh
	 */
	public OffsetErrorException(Throwable cause) {
		super(cause);
	}		
}