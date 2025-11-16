package co.com.wen.model.exception;

import co.com.wen.model.util.MessageError;
import lombok.Getter;

@Getter
public class BusinessException extends FranchiseException{

	public BusinessException(MessageError messageErrors) {
		super(messageErrors);
	}

}
