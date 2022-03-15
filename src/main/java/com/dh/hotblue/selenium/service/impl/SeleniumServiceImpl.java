package com.dh.hotblue.selenium.service.impl;

import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDateTime;
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
//			System.out.println(new Date(System.currentTimeMillis()));
//			prd.setWorkDate(new Date(System.currentTimeMillis()));
			prd.setWorkDate(LocalDateTime.now());
			try {
				String nvmid = prd.getNvmid();
				String option = prd.getOnebuOptionName();
				int onebuRank = 0;
				int onebuInnerRank = 0;
				int prdRank = 0;
				
				boolean onebuMatch = false;
				boolean singleMatch = false;
				for (int page = 1; page <= 10; page++) {
					if(onebuMatch || singleMatch) break;
					driver.get("https://search.shopping.naver.com/search/all?frm=NVSHATC&origQuery=" + prd.getKeyword() + "&pagingIndex=" + page + "&pagingSize=40&productSet=total&query=" + prd.getKeyword() + "&sort=rel&timestamp=&viewType=list");
					Thread.sleep(2000);
					for (int i = 1; i < 20; i++) {
						Thread.sleep(100);
						js.executeScript("window.scrollBy(0, " + (1000 * i) + ")");
					}
					List<WebElement> list = driver.findElements(By.cssSelector("ul.list_basis li[class^=basicList_item]"));
					List<WebElement> singlePrdList = new ArrayList<> ();
					List<WebElement> onebuPrdList = new ArrayList<> ();
					
					for (WebElement el : list) {
						String classNm = el.getAttribute("class");
						
						if (!classNm.contains("ad")) {
							String html = el.getText();
							if(html.contains("브랜드 카탈로그") || html.contains("쇼핑몰별 최저가")) {
								onebuPrdList.add(el);
							} else {
								singlePrdList.add(el);
							}
						}
					}
					
					//단독 상품 작업
					for(WebElement el : singlePrdList) {
						prdRank++;
						WebElement a = el.findElement(By.cssSelector("div[class^=basicList_info_area] > div[class^=basicList_title] a"));
						String href = a.getAttribute("href");
						if(href.contains(nvmid)) {
							singleMatch = true;
						}
					}
					
					
					//원부 상품 작업
					for (WebElement el : onebuPrdList) {
						if(onebuMatch || singleMatch) break;
						onebuRank++;
						WebElement a = el.findElement(By.cssSelector("div[class^=basicList_info_area] > div[class^=basicList_title] a"));
						//원부 상품 클릭 -> 탭 전환
						a.click();
						ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
						driver.switchTo().window(tabs.get(1));
						Thread.sleep(1000);
						//옵션 있는 경우 옵션 선택
						if(!option.equals("")) {
							try {
								List<WebElement> optionList = driver.findElements(By.cssSelector("div[class^=filter_condition_group] > ul li span[class^=filter_text]"));
								
								int targetIdx = 0;
								for(int l = 0; l < optionList.size(); l++) {
									targetIdx++;
									if(optionList.get(l).getText().equals(option))  {
										break;
									}
								}
								driver.findElement(By.cssSelector("label[for^=main-"+targetIdx+"]")).click();
							} catch(Exception e) {
								System.out.println("옵션 없어서 건너뜀");
								driver.close();
								driver.switchTo().window(tabs.get(0));
								continue;
							}
						}
						
						list = driver.findElements(By.cssSelector("div[class^=productList_seller_wrap] div[class^=pagination_pagination] a"));
						int idx = 0;
						try {
							//원부 상품 내에 단일 페이지인 경우
							if(list.equals(null) || list.size() == 0) {
								List<WebElement> links = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li a"));
								for(WebElement el3 : links) {
									onebuInnerRank++;
									String href = el3.getAttribute("href");
									if(href.contains(nvmid)) {
										onebuMatch = true;
										System.out.println("원부순위 : " + onebuRank);
										System.out.println("원부내순위 : " + onebuInnerRank);
										
										prd.setOnebuInnerRank(onebuInnerRank);
										onebuInnerRank = 0;
										break;
									}
								}
								idx++;
							} else {
								for(WebElement el2 : list) {
									if(idx >= 9) break;
									if(onebuMatch || singleMatch) break;
									
									//원부페이지 내에서 페이지 이동
									el2.click();
									onebuInnerRank = 0;
									Thread.sleep(300);
									
									list = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li a"));
									for(WebElement el3 : list) {
										onebuInnerRank++;
										String href = el3.getAttribute("href");
										if(href.contains(nvmid)) {
											onebuMatch = true;
											System.out.println("원부순위 : " + onebuRank);
											System.out.println("원부내순위 : " + onebuInnerRank);
											prd.setOnebuInnerRank(onebuInnerRank);
											onebuInnerRank = 0;
											break;
										}
									}
									idx++;
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
						driver.close();
						driver.switchTo().window(tabs.get(0));
					}
				}
				if(onebuMatch) {
					prd.setOnebuRank(onebuRank);
				}
				if(singleMatch) {
					prd.setOnebuInnerRank(0);
					prd.setPrdRank(prdRank);
				}
				productRepository.save(prd);
			} catch(Exception e) {
				
			}
		}
		driver.quit();
	}

}
