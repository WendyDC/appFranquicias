package co.com.wen.model.query;

public record BranchTopProduct(
		Integer branchId,
		String branchName,
		Integer productId,
		String productName,
		Integer stock
) {}
