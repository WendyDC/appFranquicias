package co.com.wen.api;

import co.com.wen.api.franchise.FranchiseHandler;
import co.com.wen.api.franchise.FranchiseRouterRest;
import co.com.wen.api.franchise.model.request.AddBranchRequest;
import co.com.wen.api.franchise.model.request.CreateFranchiseRequest;
import co.com.wen.api.franchise.model.FranchiseResponse;
import co.com.wen.api.franchise.model.request.UpdateFranchiseRequest;
import co.com.wen.api.util.Constants;
import co.com.wen.model.exception.FranchiseException;
import co.com.wen.model.franchise.Franchise;
import co.com.wen.model.franchise.gateways.FranchiseRepository;
import co.com.wen.model.util.MessageError;
import co.com.wen.usecase.franchise.FranchiseUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FranchiseRouterRest.class, FranchiseHandler.class})
@WebFluxTest
class FranchiseRouterRestTest {

    @Autowired
    private ApplicationContext context;

	@MockitoBean
	private FranchiseUseCase franchiseUseCase;

	@MockitoBean
	private FranchiseRepository franchiseRepository;

    private WebTestClient webTestClient;

	private static String NAME_FRANCHISE = "WEN.SA";

	@BeforeEach
	void setUp() {
		webTestClient =
				WebTestClient.bindToApplicationContext(context)
						.configureClient()
						.responseTimeout(Duration.ofSeconds(30))
						.build();
	}

    @Test
    void listenPOSTCreateFranchiseUseCase() {

	    given(franchiseUseCase.createFranchise(any()))
			    .willReturn(Mono.just(buildFranchise()));

        webTestClient.post()
                .uri(Constants.PATH_FRANCHISE)
                .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(buildCreateFranchiseRequest())
                .exchange()
                .expectStatus()
		        .isOk()
                .expectBody(FranchiseResponse.class)
                .value(franchiseResponse -> {
							Assertions.assertThat(franchiseResponse.getStatus().getCode()).isEqualTo("200");
							assertNotNull(franchiseResponse);
                        }
                );
		verify(franchiseUseCase, times(1)).createFranchise(any());
    }

	@Test
	void listenPOSTCreateFranchiseErrorUseCase() {

		given(franchiseUseCase.createFranchise(any()))
				.willReturn(Mono.error(new FranchiseException(MessageError.NOT_SAVE_RECORD)));

		webTestClient.post()
				.uri(Constants.PATH_FRANCHISE)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(buildCreateFranchiseRequest())
				.exchange()
				.expectStatus()
				.is5xxServerError()
				.expectBody(FranchiseResponse.class)
				.value(franchiseResponse -> {
							Assertions.assertThat(franchiseResponse.getStatus().getCode()).isEqualTo("500");
							assertNotNull(franchiseResponse);
						}
				);
		verify(franchiseUseCase, times(1)).createFranchise(any());
	}

	private CreateFranchiseRequest buildCreateFranchiseRequest() {
		return CreateFranchiseRequest.builder().name(NAME_FRANCHISE).build();
	}

	@Test
    void listenPOSTAddBranchUseCase() {
		given(franchiseUseCase.addBranch(anyInt(),anyInt()))
				.willReturn(Mono.just(buildFranchise()));

        webTestClient.post()
                .uri(Constants.PATH_FRANCHISE_BRANCH)
                .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(builderAddBranchRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponse.class)
		        .value(franchiseResponse -> {
					        Assertions.assertThat(franchiseResponse.getStatus().getCode()).isEqualTo("200");
					        assertNotNull(franchiseResponse);
				        }
		        );
		verify(franchiseUseCase, times(1)).addBranch(anyInt(),anyInt());
    }

	private static AddBranchRequest builderAddBranchRequest() {
		return AddBranchRequest.builder().idBranch("1").idFranchise("1").build();
	}

	@Test
    void listenPUTUpdateFranchiseUseCase() {

		given(franchiseUseCase.updateFranchise(anyInt(), any()))
				.willReturn(Mono.just(buildFranchise()));

        webTestClient.put()
                .uri(Constants.PATH_FRANCHISE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(builderUpdateFranchiseRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(FranchiseResponse.class)
		        .value(franchiseResponse -> {
					        Assertions.assertThat(franchiseResponse.getStatus().getCode()).isEqualTo("200");
					        assertNotNull(franchiseResponse);
				        }
		        );
	    verify(franchiseUseCase, times(1)).updateFranchise(anyInt(), any());
    }

	private static UpdateFranchiseRequest builderUpdateFranchiseRequest() {
		return UpdateFranchiseRequest.builder().idFranchise("1").name(NAME_FRANCHISE).build();
	}

	private Franchise buildFranchise() {
		return Franchise.builder().name(NAME_FRANCHISE).build();
	}
}
