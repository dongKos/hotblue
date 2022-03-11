package com.dh.hotblue.user.payload;


import lombok.Data;

public class UserDto {
	@Data 
	public static class LoginResponse {
		private String accessToken;
		private String refreshToken;
		private String fcmToken;
		private long userId;
	}

	@Data
	public static class UserRequest {
		private String providerId;
		private String nickname;
		
	}
}
