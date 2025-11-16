package co.com.wen.r2dbc.franchise.model;

import co.com.wen.r2dbc.branch.model.BranchEntity;
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
@Table(name = "franchise")
public class FranchiseEntity {

	@Id
	@Column("id")
	int id;

	@Column("name")
	String name;

	@Transient
	List<BranchEntity> branches = new ArrayList<>();
}
