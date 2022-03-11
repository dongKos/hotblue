package com.dh.hotblue.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

public class BucketConfig {
	private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

	private String getHost(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getHeader("Host");
	}

	/* ---------------------- 접속 제한! ----------------- */ 
	public Bucket resolveBucket(
			HttpServletRequest httpServletRequest) {
		return cache.computeIfAbsent(getHost(httpServletRequest), this::newBucket);
	}

	private Bucket newBucket(String apiKey) {
		// 10개의 클라이언트가 10초에 1000개씩 보낼 수 있는 대역폭
		return Bucket4j.builder()
				.addLimit(Bandwidth.classic(1000, Refill.intervally(10, Duration.ofSeconds(10)))).build();
	}

	/* ---------------------- sms 문자 제한! ----------------- */ 
	public Bucket smsBucket(
			HttpServletRequest httpServletRequest) {
		return cache.computeIfAbsent(getHost(httpServletRequest), this::newSmsBucket);
	}

	private Bucket newSmsBucket(String apiKey) {
		// 2개의 클라이언트가 30초에 10개씩 보낼 수 있는 대역폭
		return Bucket4j.builder()
				.addLimit(Bandwidth.classic(10, Refill.intervally(2, Duration.ofSeconds(30)))).build();
	}
}
