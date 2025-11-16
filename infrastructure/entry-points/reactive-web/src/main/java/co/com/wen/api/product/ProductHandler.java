package co.com.wen.api.product;

import co.com.wen.api.product.model.UpdateProductRequest;
import co.com.wen.api.util.RestUtil;
import co.com.wen.api.util.ValidationUtil;
import co.com.wen.model.exception.FranchiseException;
import co.com.wen.model.util.MessageError;
import co.com.wen.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {

	private final ProductUseCase productUseCase;

	public Mono<ServerResponse> listenPUTUpdateProductUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(UpdateProductRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request UpdateProduct", request, traceId))
				.flatMap(request -> updateProductUseCase(request, traceId));
	}

	private Mono<ServerResponse> updateProductUseCase(UpdateProductRequest request, String traceId) {
		return validateUpdateProductRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> productUseCase.updateProduct(
								ValidationUtil.convertStringToInt(request.getIdProduct()),
								request.getNewNameProduct(), traceId)
						.doOnSuccess(response -> RestUtil.logInfoDetails("response UpdateProduct", response, traceId))
						.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse("")))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("UpdateProduct", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateUpdateProductRequest(UpdateProductRequest request, String traceId){
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getIdProduct())
				|| !ValidationUtil.isNumeric(request.getIdProduct())
				|| ValidationUtil.isNullOrEmpty(request.getNewNameProduct())) {
			RestUtil.logInfoError("UpdateProduct", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}
}
