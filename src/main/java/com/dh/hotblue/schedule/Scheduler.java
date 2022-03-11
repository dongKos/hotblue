package com.dh.hotblue.schedule;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class Scheduler {
	
	@Value("${chrome-driver.path}") String path;
	
	//10분마다 buddyCnt 업데이트
	@Scheduled(cron = "0 0/10 * * * *")
	public void updateBuddyCnt() {
		logStartTime("updateBuddyCnt : ");
		System.out.println("path : " + path);
		
		System.setProperty("webdriver.chrome.driver", path);
		ChromeOptions options;
		options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);
		options.setProxy(null);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);
//		options.addArguments("--headless", "--disable-gpu");
		options.addArguments("--no-sandbox");
		WebDriver driver = new ChromeDriver(options);
	}

	private void logStartTime(String txt) {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String timeStr = format1.format(time);
		log.info(txt + timeStr);
	}
}
