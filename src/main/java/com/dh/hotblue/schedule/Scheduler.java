package com.dh.hotblue.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.hotblue.selenium.service.SeleniumService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class Scheduler {
	@Autowired SeleniumService seleniumService;
	@Value("${spring.profiles.active}") String profile;
	
	//@Scheduled(cron = "0 0 0/1 1/1 * ?")	//1시간
//	@Scheduled(cron = "0 0 0/2 1/1 * ?")	//2시간
	public void updateBuddyCnt() {
		if(profile.equals("product")) {
			logStartTime("startTime : ");
			seleniumService.multiWork();
			logStartTime("endTime : ");
		}
	}

	private void logStartTime(String txt) {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String timeStr = format1.format(time);
		log.info(txt + timeStr);
	}
}
