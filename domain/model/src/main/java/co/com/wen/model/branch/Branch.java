package co.com.wen.model.branch;

import co.com.wen.model.product.Product;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Branch {
	int id;
	int idFranchise;
	String name;
	List<Product> products;
}
