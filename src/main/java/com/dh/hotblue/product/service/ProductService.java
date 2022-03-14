package com.dh.hotblue.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dh.hotblue.product.dto.ProductDto.ProductList;
import com.dh.hotblue.product.dto.ProductDto.ProductSearch;

public interface ProductService {

	boolean excelUpload(MultipartFile file);

	List<ProductList> findProduct(ProductSearch search);

}
