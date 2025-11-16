package co.com.wen.r2dbc.franchise;

import co.com.wen.model.exception.TechnicalException;
import co.com.wen.model.franchise.Franchise;
import co.com.wen.model.franchise.gateways.FranchiseRepository;
import co.com.wen.model.util.MessageError;
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

    public FranchiseRepositoryAdapter(FranchiseR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
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
				.doOnError(error -> R2dbUtil.logInfoError("findByIdFranchise", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}
}
