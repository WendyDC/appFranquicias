package co.com.wen.api.product.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class UpdateProductRequest {
	@JsonProperty("idProduct")
	String idProduct;

	@JsonProperty("newNameProduct")
	String newNameProduct;
}
