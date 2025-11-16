package co.com.wen.r2dbc.query;

import co.com.wen.model.query.BranchTopProduct;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class QueryR2dbcRepository {

	private final DatabaseClient dataBaseClient;

	public QueryR2dbcRepository(DatabaseClient dataBaseClient) {
		this.dataBaseClient = dataBaseClient;
	}

	public Flux<BranchTopProduct> findTopProductByFranchiseId(Integer franchiseId) {
		String sql = """
				SELECT DISTINCT ON (b.id)
                      b.id AS branchId,
                      b.name AS branchName,
                      p.id AS productId,
                      p.name AS productName,
                      p.stock AS stock
               FROM branch b
               JOIN product p ON p.id_branch = b.id
               WHERE b.id_franchise = $1
               ORDER BY b.id, p.stock DESC
				""";

		var spec = dataBaseClient.sql(sql)
				.bind(0, franchiseId);

		return spec.map((row, meta) -> new BranchTopProduct(
						row.get("branchId", Integer.class),
						row.get("branchName", String.class),
						row.get("productId", Integer.class),
						row.get("productName", String.class),
						row.get("stock", Integer.class)
				))
				.all();
	}
}