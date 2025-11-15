package co.com.wen.api.branch;

import co.com.wen.api.util.RestConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouterRest{
	@Bean
	public RouterFunction<ServerResponse> routerFunction(BranchHandler branchHandler) {
		return route(POST(RestConstants.PATH_BRANCH_PRODUCTS), branchHandler::listenPOSTAddProductUseCase)
				.andRoute(DELETE(RestConstants.PATH_BRANCH_PRODUCTS), branchHandler::listenDELETEProductUseCase)
				.andRoute(PUT(RestConstants.PATH_BRANCH_PRODUCTS), branchHandler::listenPUTStockUseCase)
				.andRoute(GET(RestConstants.PATH_BRANCH_PRODUCTS_QUERY), branchHandler::listenGETQueryUseCase)
				.and(route(PUT(RestConstants.PATH_BRANCH), branchHandler::listenPUTBranchUseCase));
	}
}
