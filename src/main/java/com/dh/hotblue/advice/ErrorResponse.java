package com.dh.hotblue.advice;


import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Component
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	private int code = 401;
	private String message;
	private Object data;
}
