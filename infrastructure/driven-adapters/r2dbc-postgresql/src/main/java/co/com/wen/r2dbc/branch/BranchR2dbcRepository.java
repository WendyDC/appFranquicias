package co.com.wen.r2dbc.branch;

import co.com.wen.r2dbc.branch.model.BranchEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, Integer>, ReactiveQueryByExampleExecutor<BranchEntity> {

	Flux<BranchEntity> findByIdFranchise(int idFranchise);
}
