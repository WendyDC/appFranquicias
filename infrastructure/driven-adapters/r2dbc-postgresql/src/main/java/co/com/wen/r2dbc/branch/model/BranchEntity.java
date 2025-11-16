package co.com.wen.r2dbc.branch.model;

import co.com.wen.r2dbc.product.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("branch")
public class BranchEntity {

	@Id
	@Column("id")
	int id;

	@Column("id_franchise")
	int idFranchise;

	@Column("name")
	String name;

	@Transient
	List<ProductEntity> products = new ArrayList<>();
}