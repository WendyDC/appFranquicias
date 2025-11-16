package co.com.wen.r2dbc.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class R2dbUtil {
	public static void logInfoError (String detail, String messageError, String traceId) {
		log.error("Error: {} -> {} [traceId: {}]", detail, messageError, traceId);
	}
}
