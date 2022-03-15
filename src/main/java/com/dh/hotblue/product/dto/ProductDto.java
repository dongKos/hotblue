package com.dh.hotblue.product.dto;

import java.time.LocalDateTime;
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
		private int onebuRank;
		private int onebuInnerRank;
		private int prdRank;
		private LocalDateTime workDate;
		private String memo;
		private LocalDateTime createdDateTime;
		private String nvmid;
		
		@QueryProjection
		public ProductList(long id, String keyword, String shopName, int onebuRank, int onebuInnerRank, int prdRank,
				LocalDateTime workDate, String memo, LocalDateTime createdDateTime, String nvmid) {
			this.id = id;
			this.keyword = keyword;
			this.shopName = shopName;
			this.onebuRank = onebuRank;
			this.onebuInnerRank = onebuInnerRank;
			this.prdRank = prdRank;
			this.workDate = workDate;
			this.memo = memo;
			this.createdDateTime = createdDateTime;
			this.nvmid = nvmid;
		}
	}
}
