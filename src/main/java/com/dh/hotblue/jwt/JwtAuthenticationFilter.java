package com.dh.hotblue.jwt;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dh.hotblue.advice.ErrorResponse;
import com.dh.hotblue.advice.SuccessResponse;
import com.dh.hotblue.auth.PrincipalDetails;
import com.dh.hotblue.user.entity.UserEntity;
import com.dh.hotblue.user.payload.UserDto.LoginResponse;
import com.dh.hotblue.user.repository.UserRepository;
import com.dh.hotblue.util.CommonUtil;
import com.dh.hotblue.util.error.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	AuthenticationManager authenticationManager;
	UserRepository userRepository;
	CommonUtil commonUtil;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,
			CommonUtil commonUtil) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.commonUtil = commonUtil;
	}

	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper om = new ObjectMapper();
			UserEntity user = om.readValue(request.getInputStream(), UserEntity.class);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user.getNickname(), user.getProviderId());
			log.info(authenticationToken.toString());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return authentication;

		} catch (BadCredentialsException | TokenExpiredException e) {
			response.setContentType("application/json");
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setCode(403);
			errorResponse.setData("");
			errorResponse.setMessage("Login Failed!");
			try {
				new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// attemptAuthentication 실행 후, 인증이 정상적으로 완료되면 실행
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		UserEntity user = principalDetails.getUser();
		// Hash암호 방식
		String accessToken = commonUtil.generateAccessToken(user, request);
		String refreshToken = commonUtil.generateRefreshToken(user, request);

		LoginResponse res = new LoginResponse();
		res.setAccessToken(accessToken);
		res.setRefreshToken(refreshToken);
		res.setUserId(user.getUserId());
		
		//최종 로그인 시각, refreshToken 저장
		user.setRefreshToken(refreshToken);
		userRepository.save(user);
		
		Cookie tokenCookie = new Cookie("accessToken", accessToken);
		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		tokenCookie.setMaxAge((int)JwtProperties.ACCESS_EXPIRATION_TIME);
		refreshCookie.setMaxAge((int)JwtProperties.REFRESH_EXPIRATION_TIME);
		response.addCookie(tokenCookie);
		response.addCookie(refreshCookie);
		
		//사용자 정보를 쿠키에 저장
		response.addCookie(new Cookie("userId", String.valueOf(user.getUserId())));
		String username = URLEncoder.encode(CommonUtil.isNull(user.getNickname()).toString(), "UTF-8");
		response.addCookie(new Cookie("username", username));
		
		SuccessResponse successResponse = new SuccessResponse();
		successResponse.setData(res);
		successResponse.setMsg("Login Success!");
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), successResponse);
	}

}
