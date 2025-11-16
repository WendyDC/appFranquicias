package co.com.wen.r2dbc.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {

	@Id
	@Column("id")
	int id;

	@Column("name")
	String name;

	@Column("stock")
	int stock;

	@Column("id_branch")
	int idBranch;
}
