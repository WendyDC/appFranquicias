package co.com.wen.api.branch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class AddProductRequest {

	@JsonProperty("idBranch")
	String idBranch;

	@JsonProperty("nameProduct")
	String nameProduct;

	@JsonProperty("stockProduct")
	String stockProduct;
}
