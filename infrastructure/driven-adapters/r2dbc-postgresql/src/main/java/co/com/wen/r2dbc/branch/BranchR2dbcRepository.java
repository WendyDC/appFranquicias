package co.com.wen.r2dbc.branch;

import co.com.wen.r2dbc.branch.model.BranchEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, Integer>, ReactiveQueryByExampleExecutor<BranchEntity> {

}
