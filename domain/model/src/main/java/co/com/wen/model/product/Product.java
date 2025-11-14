package co.com.wen.model.product;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Product {
	int id;
	String name;
}
