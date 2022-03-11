package com.dh.hotblue.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dh.hotblue.jwt.JwtAuthenticationFilter;
import com.dh.hotblue.jwt.JwtAuthorizationFilter;
import com.dh.hotblue.user.repository.UserRepository;
import com.dh.hotblue.util.CommonUtil;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CorsConfig corsConfig;
	@Autowired
	private CommonUtil commonUtil;

	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/error/**", "/css/**", "/scss/**", "/images/**", "/js/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(),
				userRepository, commonUtil);
		jwtAuthenticationFilter.setFilterProcessesUrl("/login");
		// csrf disable
		http.csrf().disable();
		// 세션 사용 안함
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// web 관리자 logout시 redirect url
				.logout().logoutSuccessUrl("/loginPage")
				// 로그아웃 시, jwt token 들어있는 쿠키 삭제
				.invalidateHttpSession(true).deleteCookies("Authorization", "Refresh").and()
				.addFilter(corsConfig.corsFilter())
				// 폼 로그인, http basic 사용안함
				.formLogin().disable().httpBasic().disable();

		// 회원가입, 로그인, sms인증, 닉네임 중복체크, 회원가입 여부 확인,  토큰갱신 -> 허용
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/login").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/user").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/refreshToken").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/validateNickname/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/user/validateProvider").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/nickname/**").permitAll();
//		http.authorizeRequests().antMatchers(HttpMethod.GET, "/test/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/init/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/swagger-ui**").permitAll();
		
		http.authorizeRequests().antMatchers("/sns/**").permitAll();

		// 나머지 요청은 전부 체크
		http.authorizeRequests().antMatchers("/**").hasAnyAuthority("ROLE_USER");

		// 인증, 인가에 사용할 jwt 필터 등록
		http.addFilter(jwtAuthenticationFilter).addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userRepository, commonUtil),
				UsernamePasswordAuthenticationFilter.class);
//		http.addFilterBefore(new MainFilter(), SecurityContextPersistenceFilter.class);
	}
}
