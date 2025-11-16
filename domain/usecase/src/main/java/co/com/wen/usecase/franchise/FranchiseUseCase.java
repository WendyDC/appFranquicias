package co.com.wen.usecase.franchise;

import co.com.wen.model.branch.Branch;
import co.com.wen.model.branch.gateways.BranchRepository;
import co.com.wen.model.exception.BusinessException;
import co.com.wen.model.franchise.Franchise;
import co.com.wen.model.franchise.gateways.FranchiseRepository;
import co.com.wen.model.util.MessageError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class FranchiseUseCase {

	private final FranchiseRepository franchiseRepository;
	private final BranchRepository branchRepository;

	public Mono<Franchise> createFranchise(String name, String traceId) {
		return franchiseRepository.save(buildFranchise(name), traceId);
	}

	private Franchise buildFranchise(String name) {
		return Franchise.builder().name(name).build();
	}

	public Mono<Franchise> addBranch(int idFranchise, String nameBranch, String traceId) {
		return franchiseRepository.findById(idFranchise, traceId)
				.flatMap(franchise -> {
					validateBranchExistence(franchise, nameBranch);
					return addBranchFranchise(franchise, nameBranch, traceId);
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

	private Mono<Franchise> addBranchFranchise(Franchise franchise, String nameBranch, String traceId) {
		return branchRepository.save(buildBranch(franchise, nameBranch), traceId)

				.flatMap(branch -> {
					List<Branch> updateBranches = franchise.getBranches();
					updateBranches.add(branch);
					Franchise updatedFranchise = franchise.toBuilder().branches(updateBranches).build();
					return Mono.just(updatedFranchise);
				});


	}

	private Branch buildBranch(Franchise franchise, String nameBranch) {
		return Branch.builder().name(nameBranch).idFranchise(franchise.getId()).build();
	}

	public Mono<Franchise> updateFranchise(int idFranchise, String newNameFranchise, String traceId) {
		return franchiseRepository.findById(idFranchise, traceId)
				.switchIfEmpty(Mono.error(new BusinessException(MessageError.RECORD_NOT_FOUND)))
				.flatMap(franchise -> {
					if(franchise.getName().equalsIgnoreCase(newNameFranchise)){
						return Mono.just(franchise);
					}
					Franchise updatedFranchise = franchise.toBuilder().name(newNameFranchise).build();
					return franchiseRepository.save(updatedFranchise, traceId);
				});
	}
}
