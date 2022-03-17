package com.dh.hotblue.history.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.hotblue.history.entity.HistoryEntity;
import com.dh.hotblue.history.repository.HistoryRepository;
import com.dh.hotblue.history.service.HistoryService;

@Service
public class HistoryServiceImpl implements HistoryService {

	@Autowired HistoryRepository historyRepository;

	@Override
	public List<HistoryEntity> findTop10(long id) {
		return historyRepository.findTop10ByProduct_IdOrderByWorkDateDesc(id);
	}
}
