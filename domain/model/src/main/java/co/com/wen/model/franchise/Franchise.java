package co.com.wen.model.franchise;

import co.com.wen.model.branch.Branch;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Franchise {
	int id;
	String name;
	List<Branch> branches;
}
