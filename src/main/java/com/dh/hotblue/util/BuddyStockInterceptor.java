package com.dh.hotblue.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.dh.hotblue.config.BucketConfig;

import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BuddyStockInterceptor implements HandlerInterceptor {

	BucketConfig bucketConfig = new BucketConfig();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(null != request.getHeader("txid") && null != request.getHeader("timestamp") && isLoggable(request)) {
			log.info("timestamp : {}, txid : {}, ipAddr : {}", request.getHeader("timestamp"), request.getHeader("txid"), getIp(request));
		}
		Bucket bucket = bucketConfig.resolveBucket(request);
		if (bucket.tryConsume(1)) { // 1개 사용 요청
			return true;
		} else { // 제한 초과
			return false;
		}
	}

	private String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
//		boolean isLoggable = this.isloggable(request);
//		if(isLoggable) {
//		}
	}

	private boolean isLoggable(HttpServletRequest request) {
		String reqUrl = request.getRequestURL().toString();
		boolean isLoggable = true;

		if (reqUrl.indexOf(".js") > -1 || reqUrl.indexOf(".svg") > -1 || reqUrl.indexOf(".woff2") > -1
				|| reqUrl.indexOf(".css") > -1 || reqUrl.indexOf("/error") > -1

		) {
			isLoggable = false;
		}

		return isLoggable;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object,
			@Nullable Exception arg3) throws Exception {
	}
}
