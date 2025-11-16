package co.com.wen.model.exception;

import co.com.wen.model.util.MessageError;
import lombok.Getter;

@Getter
public class TechnicalException extends FranchiseException{

	public TechnicalException(MessageError messageErrors) {
		super(messageErrors);
	}
}
