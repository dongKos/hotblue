package com.dh.hotblue.selenium.service;

import java.util.List;

import com.dh.hotblue.product.entity.ProductEntity;

public interface SeleniumService {

	void work();

	void multiWork();

	void work2(int idx, List<ProductEntity> prds);

}
