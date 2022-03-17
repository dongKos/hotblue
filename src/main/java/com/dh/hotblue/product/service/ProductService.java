package com.dh.hotblue.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dh.hotblue.product.dto.ProductDto.ProductList;
import com.dh.hotblue.product.dto.ProductDto.ProductResponse;
import com.dh.hotblue.product.dto.ProductDto.ProductSearch;
import com.dh.hotblue.product.entity.ProductEntity;

public interface ProductService {

	boolean excelUpload(MultipartFile file);

	List<ProductList> findAll(ProductSearch search);

	ProductResponse findOne(long id);

	boolean update(ProductEntity product);

	boolean delete(long id);

	ProductEntity save(ProductEntity product);

	List<ProductEntity> findAllTest();

}
