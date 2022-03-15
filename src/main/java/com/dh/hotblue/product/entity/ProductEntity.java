package com.dh.hotblue.product.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@SequenceGenerator(name = "prd_seq", sequenceName = "prd_seq", initialValue = 1, allocationSize = 1)
@Table(name="product")
@Data
@EntityListeners(AuditingEntityListener.class)
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
	
	@ApiModelProperty(value="원부 내 순위")
	private int onebuInnerRank;
	
	@ApiModelProperty(value="순위")
	private int prdRank;
	
	@ApiModelProperty(value="작업 시각")
	@Column
	private LocalDateTime workDate;
	
	@ApiModelProperty(value="메모")
	private String memo;
	
	@ApiModelProperty(value="mid")
	private String nvmid;
	
	@Column
    @CreatedDate
    private LocalDateTime createdDateTime;
	

}
