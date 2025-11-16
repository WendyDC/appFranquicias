package co.com.wen.r2dbc.franchise;

import co.com.wen.model.branch.Branch;
import co.com.wen.model.exception.TechnicalException;
import co.com.wen.model.franchise.Franchise;
import co.com.wen.model.franchise.gateways.FranchiseRepository;
import co.com.wen.model.util.MessageError;
import co.com.wen.r2dbc.branch.BranchR2dbcRepository;
import co.com.wen.r2dbc.franchise.model.FranchiseEntity;
import co.com.wen.r2dbc.helper.ReactiveAdapterOperations;
import co.com.wen.r2dbc.util.R2dbUtil;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseRepositoryAdapter extends ReactiveAdapterOperations<
		Franchise,
		FranchiseEntity,
		Integer,
		FranchiseR2dbcRepository
> implements FranchiseRepository {

	private final BranchR2dbcRepository branchR2dbcRepository;
	private final ObjectMapper mapper;

    public FranchiseRepositoryAdapter(FranchiseR2dbcRepository repository, ObjectMapper mapper,
                                      BranchR2dbcRepository branchR2dbcRepository) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
        this.mapper = mapper;
		this.branchR2dbcRepository = branchR2dbcRepository;
    }

	@Override
	public Mono<Franchise> save(Franchise franchise, String traceId) {
		return save(franchise)
				.doOnError(error -> R2dbUtil.logInfoError("saveFranchise", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}

	@Override
	public Mono<Franchise> findById(Integer idFranchise, String traceId) {
		return findById(idFranchise)
				.flatMap(
						franchise -> {
							if (franchise == null) {
								return Mono.empty();
							}
							return branchR2dbcRepository.findByIdFranchise(franchise.getId())
									.map(pe -> mapper.map(pe, Branch.class))
									.collectList()
									.map(branches -> {
										franchise.setBranches(branches);
										return franchise;
									});
						}
				)
				.doOnError(error -> R2dbUtil.logInfoError("findByIdFranchise", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}
}
