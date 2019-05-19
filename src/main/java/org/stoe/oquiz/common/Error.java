package org.stoe.oquiz.common;

public class Error {
	private int codeError;
	private String msg;
	
	public Error(int codeError, String msg) {
		this.codeError = codeError;
		this.msg = msg;
	}

	public int getCodeError() {
		return codeError;
	}

	public void setCodeError(int codeError) {
		this.codeError = codeError;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
