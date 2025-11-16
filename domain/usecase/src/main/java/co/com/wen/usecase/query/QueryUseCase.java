package co.com.wen.usecase.query;

import co.com.wen.model.exception.BusinessException;
import co.com.wen.model.query.BranchTopProduct;
import co.com.wen.model.query.gateways.QueryRepository;
import co.com.wen.model.util.MessageError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class QueryUseCase {

	private final QueryRepository queryRepository;

	public Flux<BranchTopProduct> query(int idFranchise, String type, String traceId) {
		switch (type) {
			case "MAX_STOCK":
				return queryRepository.findTopProductByFranchiseId(idFranchise, traceId);
			default:
				return Flux.error(new BusinessException(MessageError.INVALID_REQUEST));
		}
	}
}
