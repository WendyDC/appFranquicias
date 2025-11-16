package co.com.wen.r2dbc;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class branchRepositoryAdapterTest {

	/*
    // TODO: change four you own tests

    @InjectMocks
    BranchRepositoryAdapter repositoryAdapter;

    @Mock
    BranchR2dbcRepository repository;

    @Mock
    ObjectMapper mapper;

	private final static String TRACE_ID = "traceId";

    @Test
    void mustFindValueById() {

        when(repository.findById(1)).thenReturn(Mono.just(BranchEntity.builder().build()));
        when(mapper.map("test", Branch.class)).thenReturn("test");

        Mono<Branch> result = repositoryAdapter.findById(1, TRACE_ID);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findByExample("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save("test")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = repositoryAdapter.save("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

	 */
}
