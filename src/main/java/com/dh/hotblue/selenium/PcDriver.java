package com.dh.hotblue.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PcDriver {

	@Value("${chrome-driver.path}") String path;
	@Value("${spring.profiles.active}") String profile;
	
	public WebDriver getPcDriver() {
		System.setProperty("webdriver.chrome.driver", path);
		ChromeOptions options;
		options = new ChromeOptions();
		if(profile.equals("product"))
			options.addArguments("--headless", "--disable-dev-shm-usage", "--no-sandbox");
		System.out.println(options);
		return new ChromeDriver(options);
	}
}
