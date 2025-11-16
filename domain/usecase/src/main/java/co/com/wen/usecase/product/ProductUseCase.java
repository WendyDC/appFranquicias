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

	public Mono<Product> updateProduct(int idProduct, String newNameProduct, String traceId) {
		return productRepository.findById(idProduct, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(product -> {
					if(product.getName().equalsIgnoreCase(newNameProduct)){
						return Mono.just(product);
					}
					Product updatedProduct = product.toBuilder().name(newNameProduct).build();
					return productRepository.save(updatedProduct, traceId);
				});
	}
}
