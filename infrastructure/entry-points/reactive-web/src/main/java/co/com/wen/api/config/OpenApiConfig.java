package co.com.wen.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Wen API")
						.version("v1")
						.description("Documentación API - WebFlux functional endpoints"));
	}

	@Bean
	public GroupedOpenApi branchGroup() {
		return GroupedOpenApi.builder()
				.group("branch")
				.packagesToScan("co.com.wen.api.branch", "co.com.wen.api.handler")
				.build();
	}
}
