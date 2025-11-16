package co.com.wen.api;

import co.com.wen.api.franchise.FranchiseHandler;
import co.com.wen.api.franchise.FranchiseRouterRest;
import co.com.wen.api.franchise.model.AddBranchRequest;
import co.com.wen.api.franchise.model.CreateFranchiseRequest;
import co.com.wen.api.franchise.model.UpdateFranchiseRequest;
import co.com.wen.api.model.RestResponse;
import co.com.wen.api.util.RestConstants;
import co.com.wen.model.branch.Branch;
import co.com.wen.model.branch.gateways.BranchRepository;
import co.com.wen.model.exception.BusinessException;
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
import static org.mockito.Mockito.when;

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

	@MockitoBean
	private BranchRepository branchRepository;

    private WebTestClient webTestClient;

	private static final String NAME_FRANCHISE = "WEN.SA";
	private static final String NAME_BRANCH = "CUCUTA-JARDIN PLAZA";

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
	    when(franchiseRepository.save(any())).thenReturn(Mono.just(buildFranchise()));
	    given(franchiseUseCase.createFranchise(any()))
			    .willReturn(Mono.just(buildFranchise()));

        webTestClient.post()
                .uri(RestConstants.PATH_FRANCHISE)
                .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(buildCreateFranchiseRequest())
                .exchange()
                .expectStatus()
		        .isOk()
                .expectBody(RestResponse.class)
                .value(restResponse -> {
							Assertions.assertThat(restResponse.getStatus().getCode()).isEqualTo("200");
							assertNotNull(restResponse);
                        }
                );
		verify(franchiseUseCase, times(1)).createFranchise(any());
    }

	@Test
	void listenPOSTCreateFranchiseErrorUseCase() {
		when(franchiseRepository.save(any())).thenReturn(Mono.just(buildFranchise()));
		given(franchiseUseCase.createFranchise(any()))
				.willReturn(Mono.error(new BusinessException(MessageError.NOT_SAVE_RECORD)));

		webTestClient.post()
				.uri(RestConstants.PATH_FRANCHISE)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(buildCreateFranchiseRequest())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(RestResponse.class)
				.value(restResponse -> {
							Assertions.assertThat(restResponse.getStatus().getCode()).isEqualTo("500");
							assertNotNull(restResponse);
						}
				);
		verify(franchiseUseCase, times(1)).createFranchise(any());
	}

	@Test
	void listenPOSTCreateFranchiseEmptyBodyTest() {
		webTestClient.post()
				.uri(RestConstants.PATH_FRANCHISE)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(CreateFranchiseRequest.builder().build())
				.exchange()
				.expectStatus()
				.is4xxClientError();
	}

	private CreateFranchiseRequest buildCreateFranchiseRequest() {
		return CreateFranchiseRequest.builder().name(NAME_FRANCHISE).build();
	}

	@Test
    void listenPOSTAddBranchUseCase() {
		when(franchiseRepository.findById(anyInt())).thenReturn(Mono.just(buildFranchise()));
		when(branchRepository.save(any())).thenReturn(Mono.just(Branch.builder().build()));
		when(franchiseRepository.save(any())).thenReturn(Mono.just(buildFranchise()));
		given(franchiseUseCase.addBranch(anyInt(),any()))
				.willReturn(Mono.just(buildFranchise()));

        webTestClient.post()
                .uri(RestConstants.PATH_FRANCHISE_BRANCH)
                .accept(MediaType.APPLICATION_JSON)
		        .bodyValue(buildAddBranchRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(RestResponse.class)
		        .value(restResponse -> {
					        Assertions.assertThat(restResponse.getStatus().getCode()).isEqualTo("200");
					        assertNotNull(restResponse);
				        }
		        );
		verify(franchiseUseCase, times(1)).addBranch(anyInt(),any());
    }

	@Test
	void listenPOSTAddBranchErrorUseCase() {
		when(franchiseRepository.findById(anyInt())).thenReturn(Mono.just(buildFranchise()));
		when(branchRepository.save(any())).thenReturn(Mono.just(Branch.builder().build()));
		when(franchiseRepository.save(any())).thenReturn(Mono.just(buildFranchise()));
		given(franchiseUseCase.addBranch(anyInt(),any()))
				.willReturn(Mono.error(new BusinessException(MessageError.DUPLICATED_RECORD)));

		webTestClient.post()
				.uri(RestConstants.PATH_FRANCHISE_BRANCH)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(buildAddBranchRequest())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(RestResponse.class)
				.value(restResponse -> {
							Assertions.assertThat(restResponse.getStatus().getCode()).isEqualTo("500");
							assertNotNull(restResponse);
						}
				);
		verify(franchiseUseCase, times(1)).addBranch(anyInt(),any());
	}

	@Test
	void listenPOSTAddBranchEmptyBodyTest() {
		webTestClient.post()
				.uri(RestConstants.PATH_FRANCHISE)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(AddBranchRequest.builder().build())
				.exchange()
				.expectStatus()
				.is4xxClientError();
	}

	private static AddBranchRequest buildAddBranchRequest() {
		return AddBranchRequest.builder().nameBranch(NAME_BRANCH).idFranchise("1").build();
	}

	@Test
    void listenPUTUpdateFranchiseUseCase() {
		when(franchiseRepository.findById(anyInt())).thenReturn(Mono.just(buildFranchise()));
		when(franchiseRepository.save(any())).thenReturn(Mono.just(buildFranchise()));
		given(franchiseUseCase.updateFranchise(anyInt(), any()))
				.willReturn(Mono.just(buildFranchise()));

        webTestClient.put()
                .uri(RestConstants.PATH_FRANCHISE)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(buildUpdateFranchiseRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(RestResponse.class)
		        .value(restResponse -> {
					        Assertions.assertThat(restResponse.getStatus().getCode()).isEqualTo("200");
					        assertNotNull(restResponse);
				        }
		        );
	    verify(franchiseUseCase, times(1)).updateFranchise(anyInt(), any());
    }

	@Test
	void listenPUTUpdateFranchiseErrorUseCase() {
		when(franchiseRepository.findById(anyInt())).thenReturn(Mono.just(buildFranchise()));
		when(franchiseRepository.save(any())).thenReturn(Mono.just(buildFranchise()));
		given(franchiseUseCase.updateFranchise(anyInt(), any()))
				.willReturn(Mono.error(new BusinessException(MessageError.NOT_SAVE_RECORD)));

		webTestClient.put()
				.uri(RestConstants.PATH_FRANCHISE)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(buildUpdateFranchiseRequest())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(RestResponse.class)
				.value(restResponse -> {
							Assertions.assertThat(restResponse.getStatus().getCode()).isEqualTo("500");
							assertNotNull(restResponse);
						}
				);
		verify(franchiseUseCase, times(1)).updateFranchise(anyInt(),any());
	}

	@Test
	void listenPUTUpdateFranchiseEmptyBodyTest() {
		webTestClient.put()
				.uri(RestConstants.PATH_FRANCHISE)
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(UpdateFranchiseRequest.builder().build())
				.exchange()
				.expectStatus()
				.is4xxClientError();
	}

	private static UpdateFranchiseRequest buildUpdateFranchiseRequest() {
		return UpdateFranchiseRequest.builder().idFranchise("1").newNameFranchise(NAME_FRANCHISE).build();
	}

	private Franchise buildFranchise() {
		return Franchise.builder().name(NAME_FRANCHISE).build();
	}
}
