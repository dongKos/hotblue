package com.dh.hotblue.jwt;

public interface JwtProperties {
	String SECRET = "BUDDYSTOCK_OFFICIAL_SECRET"; // 우리 서버만 알고 있는 비밀값
//	long ACCESS_EXPIRATION_TIME = (6 * 1) * 1000;	//6초
//	long ACCESS_EXPIRATION_TIME = (60 * 10) * 1000;	//10분
	long ACCESS_EXPIRATION_TIME = (60 * 60 * 24 * 10) * 1000;	//10일
//	long REFRESH_EXPIRATION_TIME = (6 * 10) * 1000;	//1분
	long REFRESH_EXPIRATION_TIME = (60 * 60 * 24 * 10) * 1000;	//10일
//	String HEADER_STRING = "Authorization";
	String ACCESS_HEADER_STRING = "accessToken";
	String REFRESH_HEADER_STRING = "refreshToken";
}
