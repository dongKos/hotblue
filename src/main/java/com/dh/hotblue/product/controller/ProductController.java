package com.dh.hotblue.product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dh.hotblue.advice.SuccessResponse;
import com.dh.hotblue.product.dto.ProductDto.ProductList;
import com.dh.hotblue.product.dto.ProductDto.ProductSearch;
import com.dh.hotblue.product.service.ProductService;
import com.dh.hotblue.util.RestControllerUtil;

@RequestMapping(value="/product")
@RestController
public class ProductController extends RestControllerUtil {

	@Autowired 
	ProductService productService;
	
	@PostMapping("")
	public ResponseEntity<SuccessResponse> findProduct(ProductSearch search) {
		List<ProductList> list = productService.findProduct(search);
		return new ResponseEntity<SuccessResponse>(super.successResponse(list), HttpStatus.OK);
	}
	
	@PostMapping(value="/excelUpload")
	public ResponseEntity<SuccessResponse> excelUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
		boolean result = productService.excelUpload(file);
		return new ResponseEntity<SuccessResponse>(super.successResponse(result), HttpStatus.OK);
	}
}
