package co.com.wen.api.franchise;

import co.com.wen.api.franchise.model.QueryFranchiseRequest;
import co.com.wen.api.franchise.model.AddBranchRequest;
import co.com.wen.api.franchise.model.CreateFranchiseRequest;
import co.com.wen.api.franchise.model.UpdateFranchiseRequest;
import co.com.wen.api.util.RestUtil;
import co.com.wen.api.util.ValidationUtil;
import co.com.wen.model.exception.FranchiseException;
import co.com.wen.model.util.MessageError;
import co.com.wen.usecase.franchise.FranchiseUseCase;
import co.com.wen.usecase.query.QueryUseCase;
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
public class FranchiseHandler {

	private final FranchiseUseCase franchiseUseCase;
	private final QueryUseCase queryUseCase;

    public Mono<ServerResponse> listenPOSTCreateFranchiseUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
	    return serverRequest
			    .bodyToMono(CreateFranchiseRequest.class)
			    .doOnSuccess(request -> RestUtil.logInfoDetails("request CreateFranchise", request, traceId))
			    .flatMap(request -> createFranchiseUseCase(request, traceId))
			    .switchIfEmpty(
					    RestUtil.buildGenericResponse(
							    HttpStatus.BAD_REQUEST,
							    RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST))
					    )
			    );
	}

	private Mono<ServerResponse> createFranchiseUseCase(CreateFranchiseRequest request, String traceId) {
		return validateCreateFranchiseRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() ->
								franchiseUseCase.createFranchise(request.getName(), traceId)
										.doOnSuccess(response -> RestUtil.logInfoDetails("response CreateFranchise", response, traceId))
										.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK,
												RestUtil.buildSuccessResponse(franchise)))
										.onErrorResume(
												FranchiseException.class,
												exception -> {
													RestUtil.logInfoError("CreateFranchise", exception.getMessageErrors(), traceId);
													return RestUtil.buildGenericResponse(
															HttpStatus.OK,	RestUtil.buildErrorResponse(exception));
												})));
	}

	private Mono<ServerResponse> validateCreateFranchiseRequest(CreateFranchiseRequest request, String traceId) {
		if (request == null	|| ValidationUtil.isNullOrEmpty(request.getName())) {
			RestUtil.logInfoError("CreateFranchise", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenPOSTAddBranchUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(AddBranchRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request AddBranch", request, traceId))
				.flatMap(request -> addBranchUseCase(request, traceId))
				.switchIfEmpty(
						RestUtil.buildGenericResponse(
								HttpStatus.BAD_REQUEST,
								RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST))
						)
				);
    }

	private Mono<ServerResponse> addBranchUseCase(AddBranchRequest request, String traceId) {
		return validateAddBranchRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() ->
								franchiseUseCase.addBranch(
										ValidationUtil.convertStringToInt(request.getIdFranchise()),
										request.getNameBranch(), traceId)
									.doOnSuccess(response -> RestUtil.logInfoDetails("response AddBranch", response, traceId))
									.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK,
											RestUtil.buildSuccessResponse(franchise)))
									.onErrorResume(
											FranchiseException.class,
											exception -> {
												RestUtil.logInfoError("AddBranch", exception.getMessageErrors(), traceId);
												return RestUtil.buildGenericResponse(
														HttpStatus.OK, RestUtil.buildErrorResponse(exception));
											})));
	}

	private Mono<ServerResponse> validateAddBranchRequest(AddBranchRequest request, String traceId) {
		if (request == null	|| ValidationUtil.isNullOrEmpty(request.getNameBranch())
			|| ValidationUtil.isNullOrEmpty(request.getIdFranchise())
			|| !ValidationUtil.isNumeric(request.getIdFranchise())){
			RestUtil.logInfoError("AddBranch", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenPUTFranchiseUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
	    return serverRequest
			    .bodyToMono(UpdateFranchiseRequest.class)
			    .doOnSuccess(request -> RestUtil.logInfoDetails("request UpdateFranchise", request, traceId))
			    .flatMap(request -> franchiseUseCase(request, traceId))
			    .switchIfEmpty(
					    RestUtil.buildGenericResponse(
							    HttpStatus.BAD_REQUEST,
							    RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST))
					    )
			    );
    }

	private Mono<ServerResponse> franchiseUseCase(UpdateFranchiseRequest request, String traceId) {
		return validateUpdateFranchiseRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() ->
								franchiseUseCase.updateFranchise(
										ValidationUtil.convertStringToInt(request.getIdFranchise()),
										request.getNewNameFranchise(), traceId)
									.doOnSuccess(response -> RestUtil.logInfoDetails("response UpdateFranchise", response, traceId))
									.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK,
											RestUtil.buildSuccessResponse(franchise)))
									.onErrorResume(
											FranchiseException.class,
											exception -> {
												RestUtil.logInfoError("UpdateFranchise", exception.getMessageErrors(), traceId);
												return RestUtil.buildGenericResponse(
														HttpStatus.OK,	RestUtil.buildErrorResponse(exception));
											})));
	}

	private Mono<ServerResponse> validateUpdateFranchiseRequest(UpdateFranchiseRequest request, String traceId) {
		if (request == null	|| ValidationUtil.isNullOrEmpty(request.getNewNameFranchise())
				|| ValidationUtil.isNullOrEmpty(request.getIdFranchise())
				|| !ValidationUtil.isNumeric(request.getIdFranchise())){
			RestUtil.logInfoError("UpdateFranchise", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

	public Mono<ServerResponse> listenGETQueryUseCase(ServerRequest serverRequest) {
		String traceId = RestUtil.getTraceId(serverRequest);
		return serverRequest
				.bodyToMono(QueryFranchiseRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request Query", request, traceId))
				.flatMap(request -> getQueryUseCase(request, traceId))
				.switchIfEmpty(
						RestUtil.buildGenericResponse(
								HttpStatus.BAD_REQUEST,
								RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST))
						)
				);
	}

	private Mono<ServerResponse> getQueryUseCase(QueryFranchiseRequest request, String traceId) {
		return validateQueryRequest(request, traceId)
				.switchIfEmpty(Mono.defer(() -> queryUseCase.query(
						ValidationUtil.convertStringToInt(request.getIdFranchise()),
								request.getType(), traceId)
						.collectList()
						.doOnSuccess(listResponse -> RestUtil.logInfoDetails("response Query", listResponse, traceId))
						.flatMap(listBranches -> RestUtil.buildGenericResponse(HttpStatus.OK, RestUtil.buildSuccessResponse(listBranches)))
						.onErrorResume(
								FranchiseException.class,
								exception -> {
									RestUtil.logInfoError("Query", exception.getMessageErrors(), traceId);
									return RestUtil.buildGenericResponse(
											HttpStatus.OK, RestUtil.buildErrorResponse(exception));
								})));
	}

	private Mono<ServerResponse> validateQueryRequest(QueryFranchiseRequest request, String traceId) {
		if(request == null	|| ValidationUtil.isNullOrEmpty(request.getIdFranchise())
				|| !ValidationUtil.isNumeric(request.getIdFranchise())
				|| ValidationUtil.isNullOrEmpty(request.getType())) {
			RestUtil.logInfoError("Query", MessageError.INVALID_REQUEST, traceId);
			return RestUtil.buildGenericResponse(
					HttpStatus.BAD_REQUEST,	RestUtil.buildErrorResponse(new FranchiseException(MessageError.INVALID_REQUEST)));
		}
		return Mono.empty();
	}

}
