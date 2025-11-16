package co.com.wen.model.query.gateways;

import co.com.wen.model.query.BranchTopProduct;
import reactor.core.publisher.Flux;

public interface QueryRepository {
	Flux<BranchTopProduct> findTopProductByFranchiseId(int idFranchise, String traceId);
}
