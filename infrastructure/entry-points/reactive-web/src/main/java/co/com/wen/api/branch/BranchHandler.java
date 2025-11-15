package co.com.wen.api.branch;

import co.com.wen.api.branch.model.AddProductRequest;
import co.com.wen.api.branch.model.DeleteProductRequest;
import co.com.wen.api.branch.model.QueryRequest;
import co.com.wen.api.branch.model.UpdateBranchRequest;
import co.com.wen.api.branch.model.UpdateStockRequest;
import co.com.wen.api.util.RestUtil;
import co.com.wen.api.util.ValidationUtil;
import co.com.wen.model.exception.FranchiseException;
import co.com.wen.model.util.MessageError;
import co.com.wen.usecase.branch.BranchUseCase;
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
public class BranchHandler {
	
	BranchUseCase branchUseCase;
	
	public Mono<ServerResponse> listenPOSTAddProductUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(AddProductRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request AddProduct", request, traceId))
				.flatMap(request -> addProductUseCase(request, traceId));
	}

	private Mono<ServerResponse> addProductUseCase(AddProductRequest request, String traceId) {
		return validateAddProductRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> branchUseCase.addProduct(
								ValidationUtil.convertStringToInt(request.getIdBranch()),
								request.getNameProduct(),
								ValidationUtil.convertStringToInt(request.getStockProduct()))
						.doOnSuccess(response -> RestUtil.logInfoDetails("response AddProduct", response, traceId))
						.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse("")))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("AddProduct", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateAddProductRequest(AddProductRequest  request, String traceId){
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getIdBranch())
			|| !ValidationUtil.isNumeric(request.getIdBranch())
			|| ValidationUtil.isNullOrEmpty(request.getNameProduct())
			|| ValidationUtil.isNullOrEmpty(request.getStockProduct())) {
			RestUtil.logInfoError("AddProduct", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenDELETEProductUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(DeleteProductRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request DeleteProduct", request, traceId))
				.flatMap(request -> deleteProductUseCase(request, traceId));
	}

	private Mono<ServerResponse> deleteProductUseCase (DeleteProductRequest request, String traceId) {
		return validateDeleteProductRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> branchUseCase.deleteProduct(
								ValidationUtil.convertStringToInt(request.getIdBranch()),
								ValidationUtil.convertStringToInt(request.getIdProduct()))
						.doOnSuccess(response -> RestUtil.logInfoDetails("response DeleteProduct", response, traceId))
						.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse("")))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("DeleteProduct", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateDeleteProductRequest(DeleteProductRequest request, String traceId){
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getIdBranch())
			|| !ValidationUtil.isNumeric(request.getIdBranch())
			|| ValidationUtil.isNullOrEmpty(request.getIdProduct())
			|| !ValidationUtil.isNumeric(request.getIdProduct())) {
			RestUtil.logInfoError("DeleteProduct", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenPUTStockUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(UpdateStockRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request UpdateStock", request, traceId))
				.flatMap(request -> updateStockUseCase(request, traceId));
	}

	private Mono<ServerResponse> updateStockUseCase(UpdateStockRequest request, String traceId) {
		return validateUpdateStockRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> branchUseCase.updateStock(
								ValidationUtil.convertStringToInt(request.getIdBranch()),
								ValidationUtil.convertStringToInt(request.getIdProduct()),
								ValidationUtil.convertStringToInt(request.getNewStock()))
						.doOnSuccess(response -> RestUtil.logInfoDetails("response UpdateStock", response, traceId))
						.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse("")))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("UpdateStock", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateUpdateStockRequest(UpdateStockRequest request, String traceId){
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getIdBranch())
			|| !ValidationUtil.isNumeric(request.getIdBranch())
			|| ValidationUtil.isNullOrEmpty(request.getIdProduct())
			|| !ValidationUtil.isNumeric(request.getIdProduct())
			|| ValidationUtil.isNullOrEmpty(request.getNewStock())
			|| !ValidationUtil.isNumeric(request.getNewStock())) {
			RestUtil.logInfoError("UpdateStock", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenGETQueryUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(QueryRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request Query", request, traceId))
				.flatMap(request -> getQueryUseCase(request, traceId));
	}

	private Mono<ServerResponse> getQueryUseCase(QueryRequest request, String traceId) {
		return validateQueryRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> branchUseCase.query(request.getType())
						.doOnSuccess(response -> RestUtil.logInfoDetails("response Query", response, traceId))
						.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse("")))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("Query", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateQueryRequest(QueryRequest request, String traceId) {
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getType())) {
			RestUtil.logInfoError("Query", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenPUTBranchUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(UpdateBranchRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request UpdateBranch", request, traceId))
				.flatMap(request -> updateBranchUseCase(request, traceId));
	}

	private Mono<ServerResponse> updateBranchUseCase(UpdateBranchRequest request, String traceId) {
		return validateUpdateBranchRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> branchUseCase.updateBranch(
								ValidationUtil.convertStringToInt(request.getIdBranch()),
								request.getNewNameBranch())
						.doOnSuccess(response -> RestUtil.logInfoDetails("response UpdateBranch", response, traceId))
						.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse("")))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("UpdateBranch", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateUpdateBranchRequest(UpdateBranchRequest request, String traceId){
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getIdBranch())
			|| !ValidationUtil.isNumeric(request.getIdBranch())
			|| ValidationUtil.isNullOrEmpty(request.getNewNameBranch())) {
			RestUtil.logInfoError("UpdateBranch", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}
}
