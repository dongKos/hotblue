package com.dh.hotblue.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dh.hotblue.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

	Optional<ProductEntity> findByNvmid(String nvmid);

}
