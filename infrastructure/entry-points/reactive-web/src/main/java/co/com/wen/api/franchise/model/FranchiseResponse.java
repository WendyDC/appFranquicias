package co.com.wen.api.franchise.model;

import co.com.wen.api.model.RestStatus;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class FranchiseResponse {
	RestStatus status;
	Object data;
}
