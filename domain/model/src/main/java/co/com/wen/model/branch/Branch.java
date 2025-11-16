package co.com.wen.model.branch;

import co.com.wen.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {
	int id;
	int idFranchise;
	String name;
	List<Product> products;
}
