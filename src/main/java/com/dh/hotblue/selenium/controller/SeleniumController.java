package com.dh.hotblue.selenium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dh.hotblue.advice.SuccessResponse;
import com.dh.hotblue.selenium.service.SeleniumService;
import com.dh.hotblue.util.RestControllerUtil;

@RequestMapping("/selenium")
@RestController
public class SeleniumController extends RestControllerUtil{

	@Autowired SeleniumService seleniumService;
	
	@GetMapping("/work")
	public ResponseEntity<SuccessResponse> work() {
		seleniumService.work();
		return new ResponseEntity<SuccessResponse>(super.successResponse(null), HttpStatus.OK);
	}
}
