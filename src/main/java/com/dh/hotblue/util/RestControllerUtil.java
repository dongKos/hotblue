package com.dh.hotblue.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.dh.hotblue.advice.SuccessResponse;


public class RestControllerUtil {
	@Autowired
	protected CommonUtil commonUtil;

	public SuccessResponse successResponse(Object obj) {
		SuccessResponse res = new SuccessResponse();
		res.setData(obj);
		return res;
	}
}
