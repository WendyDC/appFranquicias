package co.com.wen.model.product.gateways;

import co.com.wen.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

	Mono<Product> save(Product product);
	Mono<Product> findById(int idProduct);
}
