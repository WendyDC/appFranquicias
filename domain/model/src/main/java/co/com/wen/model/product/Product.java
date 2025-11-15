package co.com.wen.model.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Product {
	int id;
	int idBranch;
	String name;
	int stock;
}
