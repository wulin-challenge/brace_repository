package cn.wulin.brace.common.exception;

/**
 * 支撑统一异常
 * @author wubo
 */
public class BraceException extends Exception{
	private static final long serialVersionUID = 1L;

	public BraceException() {
		super();
	}

	public BraceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BraceException(String message, Throwable cause) {
		super(message, cause);
	}

	public BraceException(String message) {
		super(message);
	}

	public BraceException(Throwable cause) {
		super(cause);
	}
}
