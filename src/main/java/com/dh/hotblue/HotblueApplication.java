package com.dh.hotblue;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotblueApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotblueApplication.class, args);
	}

	@PostConstruct
	public void init() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
