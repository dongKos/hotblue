package com.dh.hotblue.selenium.runnable;

import java.util.List;


import com.dh.hotblue.product.entity.ProductEntity;
import com.dh.hotblue.selenium.service.SeleniumService;

public class RunnableWorker implements Runnable {

	private int idx;
	private SeleniumService seleniumService;
	private List<ProductEntity> prds;
	
	public RunnableWorker(int idx, List<ProductEntity> prds, SeleniumService seleniumService) {
		this.idx = idx;
		this.prds = prds;
		this.seleniumService = seleniumService;
	}
	@Override
	public void run() {
		seleniumService.work2(idx, prds);
	}

}
