package com.dh.hotblue.advice;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SuccessResponse {
	private int code = HttpStatus.OK.value();
	private String msg = "요청 성공";
    private Object data;
}
