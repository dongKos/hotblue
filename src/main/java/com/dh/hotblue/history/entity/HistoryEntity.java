package com.dh.hotblue.history.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dh.hotblue.product.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Entity
@SequenceGenerator(name = "history_seq", sequenceName = "history_seq", initialValue = 1, allocationSize = 1)
@Table(name="history")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude="product")
public class HistoryEntity {
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="history_seq")
	private long id;
	
	@ApiModelProperty(value="원부 순위")
	private int onebuRank;
	
	@ApiModelProperty(value="원부 내 순위")
	private int onebuInnerRank;
	
	@ApiModelProperty(value="단독상품 순위")
	private int prdRank;
	
	@ApiModelProperty(value="작업 시각")
	@Column
	private LocalDateTime workDate;
	
	@JsonBackReference
	@ManyToOne
	ProductEntity product;
}
