package co.com.wen.api.franchise.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class AddBranchRequest {

	@JsonProperty("idFranchise")
	String idFranchise;

	@JsonProperty("nameBranch")
	String nameBranch;
}
