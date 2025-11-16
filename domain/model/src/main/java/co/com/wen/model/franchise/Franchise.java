package co.com.wen.model.franchise;

import co.com.wen.model.branch.Branch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Franchise {
	int id;
	String name;
	List<Branch> branches;
}
