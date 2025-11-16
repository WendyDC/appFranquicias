package co.com.wen.usecase.branch;

import co.com.wen.model.branch.Branch;
import co.com.wen.model.branch.gateways.BranchRepository;
import co.com.wen.model.exception.BusinessException;
import co.com.wen.model.product.Product;
import co.com.wen.model.product.gateways.ProductRepository;
import co.com.wen.model.util.MessageError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class BranchUseCase {

	private final BranchRepository branchRepository;
	private final ProductRepository productRepository;

	public Mono<Branch> addProduct(int idBranch, String nameProduct, int stockProduct, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.flatMap(branch -> {
					validateBranchExistence(branch, nameProduct);
					return addBranchFranchise(branch, nameProduct, stockProduct, traceId);
				});
	}

	private void validateBranchExistence(Branch branch, String nameProduct) {
		branch.getProducts().stream()
				.filter(product -> product.getName().equalsIgnoreCase(nameProduct))
				.findFirst()
				.ifPresent(product -> {
					Mono.error(new BusinessException(MessageError.DUPLICATED_RECORD));
				});
	}

	private Mono<Branch> addBranchFranchise(Branch branch, String nameProduct, int stockProduct, String traceId) {
		return productRepository.save(buildProduct(branch, nameProduct, stockProduct), traceId)
				.flatMap(product -> {
					List<Product> updateProducts = branch.getProducts();
					updateProducts.add(product);
					Branch updatedBranch = branch.toBuilder().products(updateProducts).build();
					return Mono.just(updatedBranch);
				});
	}

	private Product buildProduct(Branch branch, String nameProduct, int stockProduct) {
		return Product.builder().name(nameProduct).stock(stockProduct).idBranch(branch.getId()).build();
	}


	public Mono<Void> deleteProduct(int idBranch, int idProduct, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(branch -> branch.getProducts().stream()
						.filter(p -> p.getId() == idProduct)
						.findFirst()
						.map(product -> productRepository.delete(product, traceId))
						.orElseGet(() -> Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND))));
	}

	public Mono<Void> updateStock(int idBranch, int idProduct, int newStock, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(branch -> {
					return branch.getProducts().stream()
							.filter(p -> p.getId() == idProduct)
							.findFirst()
							.map(product -> {
								if(product.getStock() == newStock) return Mono.just(product);
								Product productUpdate = product.toBuilder().stock(newStock).build();
								return productRepository.save(productUpdate, traceId);
							})
							.orElseGet(() -> Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)));
				}).then();
	}

	public Mono<Branch> updateBranch(int idBranch, String newNameBranch, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(branch -> {
					if(branch.getName().equalsIgnoreCase(newNameBranch)){
						return Mono.just(branch);
					}
					Branch updatedBranch = branch.toBuilder().name(newNameBranch).build();
					return branchRepository.save(updatedBranch, traceId);
				});
	}


}
