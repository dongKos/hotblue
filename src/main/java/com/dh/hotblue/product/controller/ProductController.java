package com.dh.hotblue.product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dh.hotblue.advice.SuccessResponse;
import com.dh.hotblue.product.dto.ProductDto.ProductList;
import com.dh.hotblue.product.dto.ProductDto.ProductResponse;
import com.dh.hotblue.product.dto.ProductDto.ProductSearch;
import com.dh.hotblue.product.entity.ProductEntity;
import com.dh.hotblue.product.service.ProductService;
import com.dh.hotblue.util.RestControllerUtil;

@RequestMapping(value="/product")
@RestController
public class ProductController extends RestControllerUtil {

	@Autowired 
	ProductService productService;
	
	@PostMapping("")
	public ResponseEntity<SuccessResponse> findAll(ProductSearch search) {
		List<ProductList> list = productService.findAll(search);
		return new ResponseEntity<SuccessResponse>(super.successResponse(list), HttpStatus.OK);
	}
	
	@PostMapping("/ntest")
	public ResponseEntity<SuccessResponse> findAllTest() {
		List<ProductEntity> list = productService.findAllTest();
		return new ResponseEntity<SuccessResponse>(super.successResponse(list), HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<SuccessResponse> save(ProductEntity product) {
		ProductEntity result = productService.save(product);
		return new ResponseEntity<SuccessResponse>(super.successResponse(result), HttpStatus.OK);
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<SuccessResponse> findOne(@PathVariable long id) {
		ProductResponse result = productService.findOne(id);
		return new ResponseEntity<SuccessResponse>(super.successResponse(result), HttpStatus.OK);
	}
	
	@PostMapping(value="/excelUpload")
	public ResponseEntity<SuccessResponse> excelUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
		boolean result = productService.excelUpload(file);
		return new ResponseEntity<SuccessResponse>(super.successResponse(result), HttpStatus.OK);
	}
	
	@PutMapping("")
	public ResponseEntity<SuccessResponse> update(@RequestBody ProductEntity product) {
		boolean result = productService.update(product);
		return new ResponseEntity<SuccessResponse>(super.successResponse(result), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<SuccessResponse> delete(@PathVariable long id) {
		boolean result = productService.delete(id);
		return new ResponseEntity<SuccessResponse>(super.successResponse(result), HttpStatus.OK);
	}
	
}
