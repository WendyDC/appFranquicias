package co.com.wen.r2dbc.query;

import co.com.wen.model.exception.TechnicalException;
import co.com.wen.model.query.BranchTopProduct;
import co.com.wen.model.query.gateways.QueryRepository;
import co.com.wen.model.util.MessageError;
import co.com.wen.r2dbc.util.R2dbUtil;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class QueryRepositoryAdapter implements QueryRepository {

	private final QueryR2dbcRepository repository;

	public QueryRepositoryAdapter(QueryR2dbcRepository repository) {
		this.repository = repository;
	}

	@Override
	public Flux<BranchTopProduct> findTopProductByFranchiseId(int franchiseId, String traceId) {
		Integer franchiseIdObj = franchiseId;
		return repository.findTopProductByFranchiseId(franchiseIdObj)
				.doOnError(error -> R2dbUtil.logInfoError("findTopProductByFranchiseId", error.getLocalizedMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}
}