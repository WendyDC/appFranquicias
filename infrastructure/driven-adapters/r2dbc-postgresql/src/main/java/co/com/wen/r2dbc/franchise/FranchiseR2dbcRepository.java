package co.com.wen.r2dbc.franchise;

import co.com.wen.r2dbc.franchise.model.FranchiseEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, Integer>, ReactiveQueryByExampleExecutor<FranchiseEntity> {

}
