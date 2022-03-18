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
		if(profile.equals("product")) {
			options.addArguments("--headless", 
					"--disable-dev-shm-usage", 
					"--no-sandbox"/* , "--remote-debugging-port=9222", "executable_path=/usr/bin/chromedriver" */);
//		options.setBinary("/usr/bin/google-chrome");
		}
		options.addArguments(
				"--headless", 
				"window-size=1920x1080",
				"--disable-dev-shm-usage", 
				"disable-gpu",
				"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36",
				"--no-sandbox",
				"lang=ko_KR");
		return new ChromeDriver(options);
	}
}
