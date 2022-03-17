package com.dh.hotblue.history.service;

import java.util.List;

import com.dh.hotblue.history.entity.HistoryEntity;

public interface HistoryService {

	List<HistoryEntity> findTop10(long id);

}
