package co.com.wen.api.util;

import lombok.Getter;

@Getter
public enum MessageStatus {

	SUCCESS ("200", "Success");

	private String code;
	private String message;

	MessageStatus(String code, String message) {
		this.code = code;
		this.message = message;
	}
};
