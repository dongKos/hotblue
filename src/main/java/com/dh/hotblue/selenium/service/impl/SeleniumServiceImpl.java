package com.dh.hotblue.selenium.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.hotblue.product.entity.ProductEntity;
import com.dh.hotblue.product.repository.ProductRepository;
import com.dh.hotblue.selenium.PcDriver;
import com.dh.hotblue.selenium.service.SeleniumService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeleniumServiceImpl implements SeleniumService {

	@Autowired ProductRepository productRepository;
	@Autowired PcDriver pcDriver;
	
	@Override
	public void work() {
		List<ProductEntity> prds = productRepository.findAll();
		log.info(prds.toString());
		WebDriver driver = pcDriver.getPcDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for(ProductEntity prd : prds) {
			try {
				String nvmid = prd.getNvmid();
				int onebuRank = 0;
				boolean match = false;
				for (int page = 1; page <= 10; page++) {
					if(match) break;
					driver.get("https://search.shopping.naver.com/search/all?frm=NVSHATC&origQuery=" + "기계식키보드" + "&pagingIndex=" + page + "&pagingSize=40&productSet=total&query=" + "기계식키보드" + "&sort=rel&timestamp=&viewType=list");
					Thread.sleep(1000);
					for (int i = 1; i < 20; i++) {
						Thread.sleep(100);
						js.executeScript("window.scrollBy(0, " + (1000 * i) + ")");
					}
					List<WebElement> els = new ArrayList<>();
					List<WebElement> list = driver.findElements(By.cssSelector("ul.list_basis li[class^=basicList_item]"));
					for (WebElement el : list) {
						String classNm = el.getAttribute("class");
						
						if (!classNm.contains("ad")) {
							list = el.findElements(By.cssSelector("div[class^=basicList_mall_title] a"));
							for (WebElement el2 : list) {
								String href = el2.getAttribute("href");
								if (href.startsWith("https://cr.shopping.naver.com/")) {
//									System.out.println(href);
									els.add(el2);
								}
							}
						}
					}
					
					
					for (WebElement el : els) {
						if(match) break;
						onebuRank++;
						el.click();
						ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
						driver.switchTo().window(tabs.get(1));
						Thread.sleep(1000);
						list = driver.findElements(By.cssSelector("div[class^=productList_seller_wrap] div[class^=pagination_pagination] a"));
						int idx = 0;
						try {
							for(WebElement el2 : list) {
								if(idx >= 9) break;
								if(match) break;
								System.out.println(el2.getText());
								el2.click();
								Thread.sleep(300);
								
								list = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li a"));
								for(WebElement el3 : list) {
									String href = el3.getAttribute("href");
									if(href.contains(nvmid)) {
										match = true;
										System.out.println("원부순위 : " + onebuRank);
										break;
									}
								}
								idx++;
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						driver.close();
						driver.switchTo().window(tabs.get(0));
					}
				}
				prd.setOnebuRank(onebuRank);
				productRepository.save(prd);
			} catch(Exception e) {
				
			}
		}
		driver.quit();
	}

}
