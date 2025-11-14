package co.com.wen.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
public class RestUtil {

	public static Mono<ServerResponse> buildGenericResponse(HttpStatus httpStatus, Object object) {
		logInfo(object);
		return Objects.nonNull(object)
				? ServerResponse.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(object)
				: ServerResponse.status(httpStatus).contentType(MediaType.APPLICATION_JSON).build();
	}

	public static void logInfoError (Throwable error) {
		log.error("Error: {}", error.getMessage());
	}

	public static void logInfoDetails (String detail, Object object) {
		log.info("Mapper: {} -> {}", detail, object);
	}

	public static void logInfo (Object object) {
		log.info("Info: {}", object);
	}
}
