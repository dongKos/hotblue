package com.dh.hotblue.product.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dh.hotblue.history.entity.HistoryEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Entity
@SequenceGenerator(name = "prd_seq", sequenceName = "prd_seq", initialValue = 1, allocationSize = 1)
@Table(name="product")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude="histories")
public class ProductEntity {

	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prd_seq")
	private long id;
	
	@ApiModelProperty(value="키워드")
	private String keyword;
	
	@ApiModelProperty(value="상점명")
	private String shopName;
	
	@ApiModelProperty(value="원부 옵션명")
	private String onebuOptionName;
	
	@ApiModelProperty(value="원부 순위")
	private int onebuRank;
	
	@ApiModelProperty(value="이전 원부 순위")
	private int prvOnebuRank;
	
	@ApiModelProperty(value="원부 내 순위")
	private int onebuInnerRank;
	
	@ApiModelProperty(value="이전 원부 내 순위")
	private int prvOnebuInnerRank;
	
	@ApiModelProperty(value="원부 링크")
	@Column(columnDefinition = "LONGTEXT")
	private String onebuLink;
	
	@ApiModelProperty(value="단독상품 순위")
	private int prdRank;
	
	@ApiModelProperty(value="이전 단독상품 순위")
	private int prvPrdRank;
	
	@ApiModelProperty(value="단독상품 링크")
	@Column(columnDefinition = "LONGTEXT")
	private String singlePrdLink;
	
	@ApiModelProperty(value="작업 시각")
	@Column
	private LocalDateTime workDate;
	
	@ApiModelProperty(value="메모")
	private String memo;
	
	@ApiModelProperty(value="mid")
	private String nvmid;
	
	@ApiModelProperty(value="성공여부")
	private String successYn = "Y";
	
	@ApiModelProperty(value="사용여부")
	private String useYn = "Y";
	
	@Column
    @CreatedDate
    private LocalDateTime createdDateTime;
	
	@OneToMany(mappedBy="product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<HistoryEntity> histories = new ArrayList<>();
	
	public void addHistory(HistoryEntity history) {
		this.getHistories().add(history);
		history.setProduct(this);
	}

}
