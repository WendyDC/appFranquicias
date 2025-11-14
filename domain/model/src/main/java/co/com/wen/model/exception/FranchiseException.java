package co.com.wen.model.exception;

import co.com.wen.model.util.MessageError;
import lombok.Getter;

@Getter
public class FranchiseException extends RuntimeException{

	private final MessageError messageErrors;

	public FranchiseException(MessageError messageErrors) {
		super(messageErrors.getMessage());
		this.messageErrors = messageErrors;
	}

}
