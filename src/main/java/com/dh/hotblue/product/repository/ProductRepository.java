package com.dh.hotblue.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dh.hotblue.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

	Optional<ProductEntity> findByNvmid(String nvmid);

	@EntityGraph(attributePaths = "histories")
	@Query("select distinct a from ProductEntity a")
	List<ProductEntity> findAllEntityGraph();
}
