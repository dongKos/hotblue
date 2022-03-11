package com.dh.hotblue.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/schedule")
@RestController
public class ScheduleController {

	@Autowired Scheduler scheduler;
	
	@GetMapping("/updateBuddyCnt")
	public void updateBuddyCnt() {
		scheduler.updateBuddyCnt();
	}
}
