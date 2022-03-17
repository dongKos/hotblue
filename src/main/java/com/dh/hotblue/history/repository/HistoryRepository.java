package com.dh.hotblue.history.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dh.hotblue.history.entity.HistoryEntity;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long>{

	List<HistoryEntity> findTop10ByProduct_IdOrderByWorkDateDesc(long id);

}
