package co.com.wen.api.franchise;

import co.com.wen.api.util.*;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

@Configuration
public class FranchiseRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler) {
        return route(POST(Constants.PATH_FRANCHISE), franchiseHandler::listenPOSTCreateFranchiseUseCase)
                .andRoute(POST(Constants.PATH_FRANCHISE_BRANCH), franchiseHandler::listenPOSTAddBranchUseCase)
		        .and(route(PUT(Constants.PATH_FRANCHISE), franchiseHandler::listenPUTUpdateFranchiseUseCase));
    }
}
