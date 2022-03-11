package com.dh.hotblue.util;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
//import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dh.hotblue.code.property.CodeProperty;
import com.dh.hotblue.code.property.HttpProperty;
import com.dh.hotblue.jwt.JwtProperties;
import com.dh.hotblue.user.entity.UserEntity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class CommonUtil {

	public String generateAccessToken(UserEntity user, HttpServletRequest request) {
		return JWT.create().withSubject(user.getNickname())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("nickname", user.getNickname())
				.withClaim("userId", user.getUserId())
				.withClaim("roles", user.getRoleList()).sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
	public String generateRefreshToken(UserEntity user, HttpServletRequest request) {
		return JWT.create()
				.withSubject(user.getNickname())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("nickname", user.getNickname())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
	//jpa paging
	public boolean isPageable(Page<?> paging) {
		boolean isPageable = true;

		int page = paging.getPageable().getPageNumber();
		int totalPages = paging.getTotalPages();
		
		if((page + 1) >= totalPages) isPageable = false;
		return isPageable;
	}
	
	//querydsl paging
	public boolean isPageable(long page, long listCnt) {
		boolean isPageable = true;
		int totalPages = (int)Math.ceil((double) listCnt / CodeProperty.PAGING_SIZE);
		if(page + 1 >= totalPages) isPageable = false;
		return isPageable;
	}
	
	public static Object isNull(Object obj) {
		if(obj == null) return "";
		else return obj;
	}
	
	public Object nvl(Object obj, Object replace) {
		if(obj == null) return replace;
		else return obj;
	}
	
	public static JSONObject post(String url, JSONObject requestBody) throws Exception {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(HttpProperty.PARSER_URL)
				.post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString())) //POST로 전달할 내용 설정
				.build();
		
		Response response;
		
		response = client.newCall(request).execute();
		return new MyObjectMapper().readValue(response.body().string(), JSONObject.class);
	}
	
	public Long getUserId(HttpServletRequest request) {
		Long userId = null;
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring("Bearer ".length());
			userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
					.getClaim("userId").asLong();
		}
		return userId;
	}
}
