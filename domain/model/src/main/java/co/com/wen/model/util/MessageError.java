package co.com.wen.model.util;

import lombok.Getter;

@Getter
public enum MessageError {

	GENERIC_ERROR ("500", "Error tecnico interno"),
	NOT_SAVE_RECORD ("500", "No se ejecuto el guardado del registro"),
	INVALID_REQUEST ("500", "Solicitud invalida"),
	DUPLICATED_RECORD ("500", "Registro duplicado");

	private String code;
	private String message;

	MessageError(String code, String message) {
		this.code = code;
		this.message = message;
	}

};

