package co.com.wen.r2dbc.product;

import co.com.wen.model.exception.TechnicalException;
import co.com.wen.model.product.Product;
import co.com.wen.model.product.gateways.ProductRepository;
import co.com.wen.model.util.MessageError;
import co.com.wen.r2dbc.helper.ReactiveAdapterOperations;
import co.com.wen.r2dbc.product.model.ProductEntity;
import co.com.wen.r2dbc.util.R2dbUtil;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryAdapter extends ReactiveAdapterOperations<
		Product,
		ProductEntity,
		Integer,
		ProductR2dbcRepository
> implements ProductRepository {
    public ProductRepositoryAdapter(ProductR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }

	@Override
	public Mono<Product> save(Product product, String traceId) {
		return save(product)
				.doOnError(error -> R2dbUtil.logInfoError("saveProduct", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}

	@Override
	public Mono<Product> findById(Integer idProduct, String traceId) {
		return findById(idProduct)
				.doOnError(error -> R2dbUtil.logInfoError("findByIdProduct", error.getMessage(), traceId))
				.onErrorMap(error -> new TechnicalException(MessageError.GENERIC_ERROR));
	}
}
