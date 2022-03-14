package com.dh.hotblue.product.dto;

import java.sql.Blob;
import java.util.Date;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

public class ProductDto {

	@Data
	public static class ProductSearch {
		private String prdName;
		private String memo;
	}
	
	@Data
	public static class ProductList {
		private long id;
		private String keyword;
		private String shopName;
		private String prdName;
		private int onebuRank;
		private int prdRank;
		private Date workDate;
		private String memo;
		private Date createDate;
		@QueryProjection
		public ProductList(long id, String keyword, String shopName, String prdName, int onebuRank, int prdRank,
				Date workDate, String memo,  Date createDate) {
			this.id = id;
			this.keyword = keyword;
			this.shopName = shopName;
			this.prdName = prdName;
			this.onebuRank = onebuRank;
			this.prdRank = prdRank;
			this.workDate = workDate;
			this.memo = memo;
			this.createDate = createDate;
		}
		
		
		
	}
}
