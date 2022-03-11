package com.dh.hotblue.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyObjectMapper extends ObjectMapper {
	 private static final long serialVersionUID = 1L;
	 
	    public MyObjectMapper(){
	        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 없는 필드로 인한 오류 무시
	    }
	    
	    
}