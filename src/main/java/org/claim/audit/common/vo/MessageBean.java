package org.claim.audit.common.vo;

public class MessageBean {
	public MessageBean(String code, String message) {
		this.code = code;
		this.message = message;
	}
	String code;
	String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
