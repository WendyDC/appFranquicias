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
					Branch updatedBranch = branch.toBuilder()
							.products(
									branch.getProducts() != null ?
											branch.getProducts().stream().toList() :
											java.util.Collections.emptyList()
							)
							.build();
					updatedBranch.getProducts().add(product);
					return branchRepository.save(updatedBranch, traceId);
				});
	}

	private Product buildProduct(Branch branch, String nameProduct, int stockProduct) {
		return Product.builder().name(nameProduct).stock(stockProduct).idBranch(branch.getId()).build();
	}


	public Mono<Branch> deleteProduct(int idBranch, int idProduct, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(branch -> {
					List<Product> products = branch.getProducts() != null
							? branch.getProducts()
							: java.util.Collections.emptyList();

					return products.stream()
							.filter(p -> p.getId() == idProduct)
							.findFirst()
							.map(product -> {
								List<Product> listProductUpdate = new java.util.ArrayList<>(products);
								listProductUpdate.removeIf(p -> p.getId() == idProduct);

								Branch updated = branch.toBuilder()
										.products(listProductUpdate)
										.build();

								return branchRepository.save(updated, traceId);
							})
							.orElseGet(() -> Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)));
				});
	}

	public Mono<Branch> updateStock(int idBranch, int idProduct, int newStock, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(branch -> {
					java.util.List<Product> products = branch.getProducts() != null
							? branch.getProducts()
							: java.util.Collections.emptyList();

					return products.stream()
							.filter(p -> p.getId() == idProduct)
							.findFirst()
							.map(product -> {

								Product productUpdate = product.toBuilder()
										.stock(newStock)
										.build();

								productRepository.save(productUpdate, traceId);

								List<Product> listProductUpdate = new java.util.ArrayList<>(products);
								listProductUpdate.removeIf(p -> p.getId() == idProduct);
								listProductUpdate.add(productUpdate);

								Branch updated = branch.toBuilder()
										.products(listProductUpdate)
										.build();

								return branchRepository.save(updated, traceId);
							})
							.orElseGet(() -> Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)));
				});
	}

	public Mono<Branch> query(String type, String traceId) {
		return null;
	}

	public Mono<Branch> updateBranch(int idBranch, String newNameBranch, String traceId) {
		return branchRepository.findById(idBranch, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(branch -> {
					Branch updatedBranch = branch.toBuilder().name(newNameBranch).build();
					return branchRepository.save(updatedBranch, traceId);
				});
	}


}
