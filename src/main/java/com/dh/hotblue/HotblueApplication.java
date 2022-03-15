package com.dh.hotblue;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HotblueApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotblueApplication.class, args);
	}

//	@PostConstruct
//	public void init() throws Exception {
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//	}
}
