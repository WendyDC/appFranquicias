package co.com.wen.usecase.franchise;

import co.com.wen.model.branch.Branch;
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
		return franchiseRepository.save(buildFranchise(name))
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.NOT_SAVE_RECORD)));
	}

	private Franchise buildFranchise(String name) {
		return Franchise.builder().name(name).build();
	}

	public Mono<Franchise> addBranch(int idFranchise, String nameBranch) {
		return franchiseRepository.findById(idFranchise)
				.flatMap(franchise -> {
					validateBranchExistence(franchise, nameBranch);
					return addBranchFranchise(franchise, nameBranch);
				});
	}

	private void validateBranchExistence(Franchise franchise, String nameBranch) {
		franchise.getBranches().stream()
				.filter(branch -> branch.getName().equalsIgnoreCase(nameBranch))
				.findFirst()
				.ifPresent(branch -> {
					Mono.error(new BusinessException(MessageError.DUPLICATED_RECORD));
				});
	}

	private Mono<Franchise> addBranchFranchise(Franchise franchise, String nameBranch) {
		return branchRepository.save(buildBranch(franchise, nameBranch))
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

	private Branch buildBranch(Franchise franchise, String nameBranch) {
		return Branch.builder().name(nameBranch).idFranchise(franchise.getId()).build();
	}

	public Mono<Franchise> updateFranchise(int idFranchise, String newNameFranchise) {
		return franchiseRepository.findById(idFranchise)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(franchise -> {
					Franchise updatedFranchise = franchise.toBuilder().name(newNameFranchise).build();
					return franchiseRepository.save(updatedFranchise);
				});
	}

}
