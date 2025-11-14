package co.com.wen.api.franchise;

import co.com.wen.api.franchise.model.request.AddBranchRequest;
import co.com.wen.api.franchise.model.request.CreateFranchiseRequest;
import co.com.wen.api.franchise.model.FranchiseResponse;
import co.com.wen.api.franchise.model.request.UpdateFranchiseRequest;
import co.com.wen.api.model.RestStatus;
import co.com.wen.api.util.MessageStatus;
import co.com.wen.api.util.RestUtil;
import co.com.wen.model.exception.FranchiseException;
import co.com.wen.model.util.MessageError;
import co.com.wen.usecase.franchise.FranchiseUseCase;
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

    public Mono<ServerResponse> listenPOSTCreateFranchiseUseCase(ServerRequest serverRequest) {
	    return serverRequest
			    .bodyToMono(CreateFranchiseRequest.class)
			    .doOnSuccess(request -> RestUtil.logInfoDetails("request", request))
			    .flatMap(this::validateCreateFranchiseRequest)
				.flatMap(request -> franchiseUseCase.createFranchise(request.getName()))
			    .doOnSuccess(RestUtil::logInfo)
			    .flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, buildSuccessResponse()))
			    .onErrorResume(
					    FranchiseException.class,
					    exception -> {
						    RestUtil.logInfoError(exception);
						    return RestUtil.buildGenericResponse(
									HttpStatus.INTERNAL_SERVER_ERROR, buildErrorResponse(exception));
					    });
	}

	private Mono<CreateFranchiseRequest> validateCreateFranchiseRequest(CreateFranchiseRequest request) {
		if (request.getName() == null || request.getName().isEmpty()) {
			return Mono.error(new FranchiseException(MessageError.INVALID_REQUEST));
		}
		return Mono.just(request);
	}

	public Mono<ServerResponse> listenPOSTAddBranchUseCase(ServerRequest serverRequest) {
		return serverRequest
				.bodyToMono(AddBranchRequest.class)
				.doOnSuccess(request -> RestUtil.logInfoDetails("request", request))
				.flatMap(this::validateAddBranchRequest)
				.flatMap(request -> franchiseUseCase.addBranch(
						Integer.parseInt(request.getIdFranchise()), Integer.parseInt(request.getIdBranch())))
				.doOnSuccess(RestUtil::logInfo)
				.flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, buildSuccessResponse()))
				.onErrorResume(
						FranchiseException.class,
						exception -> {
							RestUtil.logInfoError(exception);
							return RestUtil.buildGenericResponse(
									HttpStatus.INTERNAL_SERVER_ERROR, buildErrorResponse(exception));
						});
    }

	private Mono<AddBranchRequest> validateAddBranchRequest(AddBranchRequest request) {
		if (request.getIdBranch() == null || request.getIdBranch().isEmpty() || !isNumeric(request.getIdBranch()) ||
		request.getIdFranchise() == null || request.getIdFranchise().isEmpty() || !isNumeric(request.getIdFranchise())){
			return Mono.error(new FranchiseException(MessageError.INVALID_REQUEST));
		}
		return Mono.just(request);
	}

	public Mono<ServerResponse> listenPUTUpdateFranchiseUseCase(ServerRequest serverRequest) {
	    return serverRequest
			    .bodyToMono(UpdateFranchiseRequest.class)
			    .doOnSuccess(request -> RestUtil.logInfoDetails("request", request))
			    .flatMap(this::validateUpdateFranchiseRequest)
			    .flatMap(request -> franchiseUseCase.updateFranchise(
							    Integer.parseInt(request.getIdFranchise()), request.getName()))
			    .doOnSuccess(RestUtil::logInfo)
			    .flatMap(franchise -> RestUtil.buildGenericResponse(HttpStatus.OK, buildSuccessResponse()))
			    .onErrorResume(
					    FranchiseException.class,
					    exception -> {
						    RestUtil.logInfoError(exception);
						    return RestUtil.buildGenericResponse(
								    HttpStatus.INTERNAL_SERVER_ERROR, buildErrorResponse(exception));
					    });
    }

	private Mono<UpdateFranchiseRequest> validateUpdateFranchiseRequest(UpdateFranchiseRequest request) {
		if (request.getName() == null || request.getName().isEmpty() ||
				request.getIdFranchise() == null || request.getIdFranchise().isEmpty() || !isNumeric(request.getIdFranchise())){
			return Mono.error(new FranchiseException(MessageError.INVALID_REQUEST));
		}
		return Mono.just(request);
	}

	private FranchiseResponse buildSuccessResponse() {
		return FranchiseResponse.builder()
				.status(
						RestStatus.builder()
								.code(MessageStatus.SUCCESS.getCode())
								.message(MessageStatus.SUCCESS.getMessage())
								.build()
				)
				.data("")
				.build();
	}

	private FranchiseResponse buildErrorResponse(FranchiseException exception) {
		String code = exception.getMessageErrors().getCode();
		String message = exception.getMessageErrors().getMessage();
		return FranchiseResponse.builder()
				.status(
						RestStatus.builder()
								.code(code)
								.message(message)
								.build()
				)
				.data("")
				.build();
	}

	private boolean isNumeric(String valor){
		try {
			Integer.parseInt(valor);
			return Boolean.TRUE;
		} catch (NumberFormatException e) {
			return Boolean.FALSE;
		}
	}
}
