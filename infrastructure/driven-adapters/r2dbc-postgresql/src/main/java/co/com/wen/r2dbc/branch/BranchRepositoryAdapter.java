package co.com.wen.r2dbc.branch;

import co.com.wen.model.branch.Branch;
import co.com.wen.model.branch.gateways.BranchRepository;
import co.com.wen.model.exception.TechnicalException;
import co.com.wen.model.util.MessageError;
import co.com.wen.r2dbc.branch.model.BranchEntity;
import co.com.wen.r2dbc.helper.ReactiveAdapterOperations;
import co.com.wen.r2dbc.util.R2dbUtil;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BranchRepositoryAdapter extends ReactiveAdapterOperations<
		Branch,
		BranchEntity,
		Integer,
		BranchR2dbcRepository
> implements BranchRepository {

	public BranchRepositoryAdapter(BranchR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

	public Mono<Branch> save(Branch branch, String traceId) {
		return save(branch)
				.doOnError(error -> R2dbUtil.logInfoError("saveBranch", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}

	@Override
	public Mono<Branch> findById(Integer id, String traceId) {
		return findById(id)
				.doOnError(error -> R2dbUtil.logInfoError("findByIdBranch", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}
}
