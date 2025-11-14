package co.com.wen.model.franchise.gateways;

import co.com.wen.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

	Mono<Franchise> save(Franchise franchise);
	Mono<Franchise> findById(int idFranchise);
}
