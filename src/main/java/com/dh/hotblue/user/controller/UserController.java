package com.dh.hotblue.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dh.hotblue.advice.SuccessResponse;
import com.dh.hotblue.code.property.CodeProperty;
import com.dh.hotblue.user.payload.UserDto.UserRequest;
import com.dh.hotblue.user.service.UserService;
import com.dh.hotblue.util.CommonUtil;
import com.dh.hotblue.util.RestControllerUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
public class UserController extends RestControllerUtil {
	
	@Autowired UserService userService;
	@Autowired CommonUtil commonUtil;

	@ApiOperation(value = "회원가입")
	@ApiResponses(value = {@ApiResponse(code = CodeProperty.HttpStatusOk, message = CodeProperty.successMessage)})
	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> signUp(
			@ApiParam(value = "user", required = true, example="1") 
			@RequestBody UserRequest user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ResponseEntity<SuccessResponse>(super.successResponse(userService.save(user)), HttpStatus.OK);
	}
}
