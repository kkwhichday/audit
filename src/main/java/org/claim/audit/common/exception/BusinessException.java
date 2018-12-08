package org.claim.audit.common.exception;

import org.claim.audit.common.vo.ResponseData;


@SuppressWarnings("serial")
public class BusinessException extends RuntimeException{
	ResponseData resMsg;
	public BusinessException(ResponseData responseData){
		this.resMsg = responseData;
	}
	public ResponseData getResMsg() {
		return resMsg;
	}
	public void setResMsg(ResponseData resMsg) {
		this.resMsg = resMsg;
	}
	public BusinessException(String message){
		super(message);
	}

}
