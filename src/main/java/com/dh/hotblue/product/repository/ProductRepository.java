package com.dh.hotblue.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dh.hotblue.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

}
