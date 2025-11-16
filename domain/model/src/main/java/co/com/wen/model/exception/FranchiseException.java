package co.com.wen.model.exception;

import co.com.wen.model.util.MessageError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FranchiseException extends RuntimeException{

	private final MessageError messageErrors;
}
