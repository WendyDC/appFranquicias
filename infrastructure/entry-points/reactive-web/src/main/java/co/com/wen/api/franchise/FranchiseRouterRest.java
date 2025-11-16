package co.com.wen.api.franchise;

import co.com.wen.api.branch.BranchHandler;
import co.com.wen.api.util.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouterRest {

	@Bean
	@RouterOperations({
			@RouterOperation(path = RestConstants.PATH_FRANCHISE,
					beanClass = FranchiseHandler.class, beanMethod = "listenPOSTCreateFranchiseUseCase",
					operation = @Operation(operationId = "createFranchise", summary = "Crear franquicia")),
			@RouterOperation(path = RestConstants.PATH_FRANCHISE_BRANCH,
					beanClass = FranchiseHandler.class, beanMethod = "listenPOSTAddBranchUseCase",
					operation = @Operation(operationId = "addBranch", summary = "Adicionar sucursal a franquicia")),
			@RouterOperation(path = RestConstants.PATH_FRANCHISE,
					beanClass = FranchiseHandler.class, beanMethod = "listenPUTFranchiseUseCase",
					operation = @Operation(operationId = "updateFranchise", summary = "Actualizar franquicia")),
			@RouterOperation(path = RestConstants.PATH_BRANCH_FRANCHISE_QUERY,
					beanClass = BranchHandler.class, beanMethod = "listenGETQueryUseCase",
					operation = @Operation(operationId = "queryProducts", summary = "Consultar información de franquicias."))
	})
    public RouterFunction<ServerResponse> franchiseRouterFunction(FranchiseHandler franchiseHandler) {
        return route(POST(RestConstants.PATH_FRANCHISE), franchiseHandler::listenPOSTCreateFranchiseUseCase)
                .andRoute(POST(RestConstants.PATH_FRANCHISE_BRANCH), franchiseHandler::listenPOSTAddBranchUseCase)
		        .andRoute(PUT(RestConstants.PATH_FRANCHISE), franchiseHandler::listenPUTFranchiseUseCase)
				.and(route(GET(RestConstants.PATH_BRANCH_FRANCHISE_QUERY), franchiseHandler::listenGETQueryUseCase));
    }
}
