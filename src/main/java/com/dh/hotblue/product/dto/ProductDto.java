package com.dh.hotblue.product.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

public class ProductDto {

	@Data
	public static class ProductSearch {
		private String keyword;
		private String shopName;
		private String nvmid;
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
		private int prvOnebuRank;
		private int prvOnebuInnerRank;
		private int prvPrdRank;
		private LocalDateTime workDate;
		private String memo;
		private LocalDateTime createdDateTime;
		private String nvmid;
		private String onebuOptionName;
		private String onebuLink;
		private String singlePrdLink;
		
		@QueryProjection
		public ProductList(long id, String keyword, String shopName, int onebuRank, int onebuInnerRank, int prdRank,
				int prvOnebuRank, int prvOnebuInnerRank, int prvPrdRank, LocalDateTime workDate, String memo,
				LocalDateTime createdDateTime, String nvmid, String onebuOptionName, String onebuLink,
				String singlePrdLink) {
			this.id = id;
			this.keyword = keyword;
			this.shopName = shopName;
			this.onebuRank = onebuRank;
			this.onebuInnerRank = onebuInnerRank;
			this.prdRank = prdRank;
			this.prvOnebuRank = prvOnebuRank;
			this.prvOnebuInnerRank = prvOnebuInnerRank;
			this.prvPrdRank = prvPrdRank;
			this.workDate = workDate;
			this.memo = memo;
			this.createdDateTime = createdDateTime;
			this.nvmid = nvmid;
			this.onebuOptionName = onebuOptionName;
			this.onebuLink = onebuLink;
			this.singlePrdLink = singlePrdLink;
		}
	}
	
	@Data
	public static class ProductResponse {
		private long id;
		private String keyword;
		private String onebuOptionName;
		private String nvmid;
		private String memo;

		@QueryProjection
		public ProductResponse(long id, String keyword, String onebuOptionName, String nvmid, String memo) {
			this.id = id;
			this.keyword = keyword;
			this.onebuOptionName = onebuOptionName;
			this.nvmid = nvmid;
			this.memo = memo;
		}
	}
}
