package com.dh.hotblue.history.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dh.hotblue.advice.SuccessResponse;
import com.dh.hotblue.history.entity.HistoryEntity;
import com.dh.hotblue.history.service.HistoryService;
import com.dh.hotblue.util.RestControllerUtil;

@RestController
@RequestMapping("/history")
public class HistoryController extends RestControllerUtil{
	
	@Autowired HistoryService historyService;

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse> findTop10(@PathVariable long id) {
		List<HistoryEntity> list = historyService.findTop10(id);
		return new ResponseEntity<SuccessResponse>(super.successResponse(list), HttpStatus.OK);
	}
}
