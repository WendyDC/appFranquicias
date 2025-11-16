package co.com.wen.api.util;

import co.com.wen.api.model.RestResponse;
import co.com.wen.api.model.RestStatus;
import co.com.wen.model.exception.FranchiseException;
import co.com.wen.model.util.MessageError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
public class RestUtil {

	public static Mono<ServerResponse> buildGenericResponse(HttpStatus httpStatus, Object object) {
		return Objects.nonNull(object)
				? ServerResponse.status(httpStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(object)
				: ServerResponse.status(httpStatus).contentType(MediaType.APPLICATION_JSON).build();
	}

	public static RestResponse buildSuccessResponse(Object object) {
		return RestResponse.builder()
				.status(
						RestStatus.builder()
								.code(MessageStatus.SUCCESS.getCode())
								.message(MessageStatus.SUCCESS.getMessage()).build()
				)
				.data(Objects.nonNull(object)?object:"").build();
	}

	public static RestResponse buildErrorResponse(FranchiseException exception) {
		String code = exception.getMessageErrors().getCode();
		String message = exception.getMessageErrors().getMessage();
		return RestResponse.builder()
				.status(
						RestStatus.builder()
								.code(code)
								.message(message).build()
				).data("").build();
	}

	public static void logInfoError (String detail, MessageError messageError, String traceId) {
		log.error("Error: {} -> {} [traceId: {}]", detail, messageError.getMessage(), traceId);
	}

	public static void logInfoDetails (String detail, Object object, String traceId) {
		log.info("Info: {} -> {} [traceId: {}]", detail, Objects.nonNull(object)?object:"", traceId);
	}

	public static String getTraceId(ServerRequest serverRequest) {
		try {
			return serverRequest.headers().header("traceId").get(0);
		} catch (Exception e) {
			return RestConstants.NOT_TRACE_ID;
		}
	}
}
