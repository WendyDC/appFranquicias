package co.com.wen.r2dbc.product;

import co.com.wen.r2dbc.product.model.ProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Integer>, ReactiveQueryByExampleExecutor<ProductEntity> {

}
