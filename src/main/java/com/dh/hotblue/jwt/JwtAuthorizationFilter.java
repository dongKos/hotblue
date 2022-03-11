package com.dh.hotblue.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dh.hotblue.advice.ErrorResponse;
import com.dh.hotblue.user.entity.UserEntity;
import com.dh.hotblue.user.repository.UserRepository;
import com.dh.hotblue.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	AuthenticationManager authenticationManager;
	UserRepository userRepository;
	CommonUtil commonUtil;
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,
			 CommonUtil commonUtil) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.commonUtil = commonUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (isPublic(request)) {
			chain.doFilter(request, response);
		} else {
			// header가 있는지 확인
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

			// header에 Authorization -> accessToken이 있는지 확인
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

				try {

					String token = authorizationHeader.substring("Bearer ".length());
					String nickname = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
							.getClaim("nickname").asString();
					String[] roles = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
							.getClaim("roles").asArray(String.class);

					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

					Arrays.stream(roles).forEach(role -> {
						authorities.add(new SimpleGrantedAuthority(role));
					});

					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							nickname, null, authorities);

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					chain.doFilter(request, response);
				} catch (Exception e) {
					response.setContentType("application/json");
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					ErrorResponse errorResponse = new ErrorResponse();
					errorResponse.setCode(HttpStatus.UNAUTHORIZED.value());
					errorResponse.setData("");
					errorResponse.setMessage(e.getMessage());
					try {
						new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

				// header에 Authorization -> accessToken이 없으면 통과
				// 이러면 인증이 안되지않나? 라고 생각할 수 있지만
				// SecurityContextHolder에 권한이 없기 때문에 리소스 접근불가
			} else {

				// web으로 로그인한 경우 cookie를 체크
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {

					try {
						for (Cookie cookie : cookies) {
							if (cookie.getName().equals(JwtProperties.ACCESS_HEADER_STRING)) {

								String token = cookie.getValue();
								String nickname = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
										.verify(token).getClaim("nickname").asString();
								String[] roles = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
										.verify(token).getClaim("roles").asArray(String.class);

								Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

								Arrays.stream(roles).forEach(role -> {
									authorities.add(new SimpleGrantedAuthority(role));
								});

								UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
										nickname, null, authorities);

								SecurityContextHolder.getContext().setAuthentication(authenticationToken);
								break;
							}
						}
					} catch (Exception e) {
						try {
							for(Cookie cookie : cookies) {
								if(cookie.getName().equals(JwtProperties.REFRESH_HEADER_STRING)) {
									String refreshToken = cookie.getValue();
									String nickname = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshToken)
											.getClaim("nickname").asString();
									
									
									Optional<UserEntity> optUser = userRepository.findByNickname(nickname);
									String redisToken = optUser.get().getRefreshToken();

									if (optUser.isPresent() && refreshToken.equals(redisToken)) {

										UserEntity user = optUser.get();
										String accessToken = commonUtil.generateAccessToken(user, request);

										List<String> roles = user.getRoleList();

										Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

										roles.forEach(role -> {
											authorities.add(new SimpleGrantedAuthority(role));
										});
										UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
												nickname, null, authorities);

										SecurityContextHolder.getContext().setAuthentication(authenticationToken);
										Cookie accessCookie = new Cookie(JwtProperties.ACCESS_HEADER_STRING, accessToken);
										response.addCookie(accessCookie);
										chain.doFilter(request, response);
										return;

									} else {
										response.sendRedirect("/");
									}
									break;
								}
								
							}
						} catch(Exception ex) {
							response.sendRedirect("/");
						}
						
					}
				}
				chain.doFilter(request, response);
			}

		}
	}

	private boolean isPublic(HttpServletRequest request) {
		boolean isPublic = false;
		if (request.getServletPath().equals("/login") || request.getServletPath().equals("/user/refreshToken")) {
			isPublic = true;
		}
		return isPublic;
	}

}
