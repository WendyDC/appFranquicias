package co.com.wen.api.branch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class UpdateStockRequest {

	@JsonProperty("idBranch")
	String idBranch;

	@JsonProperty("idProduct")
	String idProduct;

	@JsonProperty("newStock")
	String newStock;
}
