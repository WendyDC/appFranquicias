package co.com.wen.model.branch.gateways;

import co.com.wen.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchRepository {
	Mono<Branch> findById(int id);
	Mono<Branch> save(Branch branch);
}
