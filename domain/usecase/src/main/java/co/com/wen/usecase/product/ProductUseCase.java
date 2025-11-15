package co.com.wen.usecase.product;

import co.com.wen.model.exception.BusinessException;
import co.com.wen.model.product.Product;
import co.com.wen.model.product.gateways.ProductRepository;
import co.com.wen.model.util.MessageError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

	private final ProductRepository productRepository;

	public Mono<Product> updateProduct(int idProduct, String newNameProduct) {
		return productRepository.findById(idProduct)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(product -> {
					Product updatedProduct = product.toBuilder().name(newNameProduct).build();
					return productRepository.save(updatedProduct);
				});
	}
}
