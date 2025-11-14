package co.com.wen.model.exception;

import co.com.wen.model.util.MessageError;

public class BusinessException extends FranchiseException{

	public BusinessException(MessageError messageErrors) {
		super(messageErrors);
	}

}
