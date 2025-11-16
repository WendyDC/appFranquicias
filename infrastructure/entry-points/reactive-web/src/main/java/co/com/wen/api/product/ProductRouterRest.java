package co.com.wen.api.product;

import co.com.wen.api.util.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouterRest {

	@Bean
	@RouterOperations({
			@RouterOperation(path = RestConstants.PATH_PRODUCT,
					beanClass = ProductHandler.class, beanMethod = "listenPUTProductUseCase",
					operation = @Operation(operationId = "updateProduct", summary = "Actualizar producto"))
	})
	public RouterFunction<ServerResponse> productRouterFunction(ProductHandler productHandler) {
		return route(PUT(RestConstants.PATH_PRODUCT), productHandler::listenPUTProductUseCase);
	}
}
