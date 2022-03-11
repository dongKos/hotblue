package com.dh.hotblue.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dh.hotblue.util.error.BadRequestException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		
		int code = 0;
		boolean isTracable = true;
		if(e instanceof BadCredentialsException || e instanceof TokenExpiredException) {
			code = HttpStatus.UNAUTHORIZED.value();
		} else if(e instanceof BadRequestException || e instanceof HttpMessageNotReadableException) {
			isTracable = false;
			code = HttpStatus.BAD_REQUEST.value();
		} else if(e instanceof AccessDeniedException) {
			code = HttpStatus.FORBIDDEN.value();
		} else {
			code = HttpStatus.INTERNAL_SERVER_ERROR.value(); 
		}
		
		ErrorResponse err = new ErrorResponse(code, e.getMessage(), null);
		if(isTracable) e.printStackTrace();
		
		return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}