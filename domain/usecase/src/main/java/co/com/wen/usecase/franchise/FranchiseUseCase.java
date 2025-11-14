package co.com.wen.usecase.franchise;

import co.com.wen.model.branch.gateways.BranchRepository;
import co.com.wen.model.exception.BusinessException;
import co.com.wen.model.franchise.Franchise;
import co.com.wen.model.franchise.gateways.FranchiseRepository;
import co.com.wen.model.util.MessageError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

	private final FranchiseRepository franchiseRepository;
	private final BranchRepository branchRepository;

	public Mono<Franchise> createFranchise(String name) {
		return franchiseRepository.save(builderFranchise(name))
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.NOT_SAVE_RECORD)));
	}

	private Franchise builderFranchise(String name) {
		return Franchise.builder().name(name).build();
	}

	public Mono<Franchise> addBranch(int idFranchise, int idBranch) {
		return franchiseRepository.findById(idFranchise)
				.flatMap(franchise -> {
					validateBranchExistence(franchise, idBranch);
					return addBranchFranchise(franchise, idBranch);
				});
	}

	private void validateBranchExistence(Franchise franchise, int idBranch) {
		franchise.getBranches().stream()
				.filter(branch -> branch.getId() == idBranch)
				.findFirst()
				.ifPresent(branch -> {
					Mono.error(new BusinessException(MessageError.DUPLICATED_RECORD));
				});
	}

	private Mono<Franchise> addBranchFranchise(Franchise franchise, int idBranch) {
		return branchRepository.findById(idBranch)
				.flatMap(branch -> {
					Franchise updatedFranchise = franchise.toBuilder()
							.branches(
									franchise.getBranches() != null ?
											franchise.getBranches().stream().toList() :
											java.util.Collections.emptyList()
							)
							.build();
					updatedFranchise.getBranches().add(branch);
					return franchiseRepository.save(updatedFranchise);
				});
	}

	public Mono<Franchise> updateFranchise(int idFranchise, String name) {
		return franchiseRepository.findById(idFranchise)
				.flatMap(franchise -> {
					Franchise updatedFranchise = franchise.toBuilder().name(name).build();
					return franchiseRepository.save(updatedFranchise);
				});
	}

}
