package co.com.wen.api.branch;

import co.com.wen.api.util.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouterRest{

	@Bean
	@RouterOperations({
			@RouterOperation(path = RestConstants.PATH_BRANCH_PRODUCTS,
					beanClass = BranchHandler.class, beanMethod = "listenPOSTAddProductUseCase",
					operation = @Operation(operationId = "addProduct", summary = "Agregar producto a sucursal")),
			@RouterOperation(path = RestConstants.PATH_BRANCH_PRODUCTS,
					beanClass = BranchHandler.class, beanMethod = "listenDELETEProductUseCase",
					operation = @Operation(operationId = "deleteProduct", summary = "Eliminar producto de sucursal")),
			@RouterOperation(path = RestConstants.PATH_BRANCH_PRODUCTS,
					beanClass = BranchHandler.class, beanMethod = "listenPUTStockUseCase",
					operation = @Operation(operationId = "updateStock", summary = "Actualizar stock")),
			@RouterOperation(path = RestConstants.PATH_BRANCH,
					beanClass = BranchHandler.class, beanMethod = "listenPUTBranchUseCase",
					operation = @Operation(operationId = "updateBranch", summary = "Actualizar sucursal"))
	})
	public RouterFunction<ServerResponse> branchRouterFunction(BranchHandler branchHandler) {
		return route(POST(RestConstants.PATH_BRANCH_PRODUCTS), branchHandler::listenPOSTAddProductUseCase)
				.andRoute(DELETE(RestConstants.PATH_BRANCH_PRODUCTS), branchHandler::listenDELETEProductUseCase)
				.andRoute(PUT(RestConstants.PATH_BRANCH_PRODUCTS), branchHandler::listenPUTStockUseCase)
				.and(route(PUT(RestConstants.PATH_BRANCH), branchHandler::listenPUTBranchUseCase));
	}
}
