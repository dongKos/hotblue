package com.dh.hotblue.selenium.service.impl;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.hotblue.history.entity.HistoryEntity;
import com.dh.hotblue.product.entity.ProductEntity;
import com.dh.hotblue.product.repository.ProductRepository;
import com.dh.hotblue.selenium.PcDriver;
import com.dh.hotblue.selenium.runnable.RunnableWorker;
import com.dh.hotblue.selenium.service.SeleniumService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeleniumServiceImpl implements SeleniumService {

	@Autowired ProductRepository productRepository;
	@Autowired PcDriver pcDriver;
	
//	@Transactional
	@Override
	public void work() {
		List<ProductEntity> prds = productRepository.findAllEntityGraph();
		WebDriver driver = pcDriver.getPcDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		LocalDateTime now = LocalDateTime.now();
		int workCnt = 0;
		for(ProductEntity prd : prds) {
			workCnt++;
			if(prd.getKeyword().equals("") && prd.getNvmid().equals("")) continue;
			
			HistoryEntity history = new HistoryEntity();
			history.setOnebuInnerRank(prd.getOnebuInnerRank());
			history.setOnebuRank(prd.getOnebuRank());
			history.setPrdRank(prd.getPrdRank());
			history.setWorkDate(now);
			prd.addHistory(history);
			
			prd.setPrvOnebuRank(prd.getOnebuRank());
			prd.setPrvOnebuInnerRank(prd.getOnebuInnerRank());
			prd.setPrvPrdRank(prd.getPrdRank());
			
			prd.setWorkDate(now);
			try {
				String nvmid = prd.getNvmid();
				String option = prd.getOnebuOptionName();
				int onebuRank = 0;
				int onebuInnerRank = 0;
				int prdRank = 0;
				
				boolean onebuMatch = false;
				boolean singleMatch = false;
				for (int page = 1; page <= 3; page++) {
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
							if(html.contains("????????? ????????????") || html.contains("???????????? ?????????")) {
								onebuPrdList.add(el);
							} else {
								singlePrdList.add(el);
							}
						}
					}
					
					//?????? ?????? ??????
					for(WebElement el : singlePrdList) {
						prdRank++;
						WebElement a = el.findElement(By.cssSelector("div[class^=basicList_info_area] > div[class^=basicList_title] a"));
						String href = a.getAttribute("href");
						if(href.contains(nvmid)) {
							String shopNm = el.findElement(By.cssSelector("div[class^=basicList_mall_area] > div[class^=basicList_mall_title] a")).getText();
							prd.setShopName(shopNm);
							prd.setSinglePrdLink(driver.getCurrentUrl());
							prd.setOnebuLink("");
							singleMatch = true;
							break;
						}
					}
					
					
					//?????? ?????? ??????
					for (WebElement el : onebuPrdList) {
						if(onebuMatch || singleMatch) break;
						onebuRank++;
						WebElement a = el.findElement(By.cssSelector("div[class^=basicList_info_area] > div[class^=basicList_title] a"));
						//?????? ?????? ?????? -> ??? ??????
						a.click();
						ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
						driver.switchTo().window(tabs.get(1));
						Thread.sleep(1000);
						//?????? ?????? ?????? ?????? ??????
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
								System.out.println("?????? ????????? ?????????");
								log.info("?????? ????????? ????????? : " + prd.getId() + " // " + prd.getKeyword());
								driver.close();
								driver.switchTo().window(tabs.get(0));
								continue;
							}
						}
						
						list = driver.findElements(By.cssSelector("div[class^=productList_seller_wrap] div[class^=pagination_pagination] a"));
						int idx = 0;
						try {
							//?????? ?????? ?????? ?????? ???????????? ??????
							if(list.equals(null) || list.size() == 0) {
								onebuInnerRank = 0;
								list = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li"));
								//li ??????
								for(WebElement el3 : list) {
									onebuInnerRank++;
									
									List<WebElement> as = el3.findElements(By.cssSelector("a"));
									//li ????????? a ?????? ??????
									for(WebElement aTag : as) {
										String href = aTag.getAttribute("href");
										if(href.contains(nvmid)) {
											String shopNm = el3.findElement(By.cssSelector("div[class^=productList_inner] > div[class^=productList_mall] a span")).getText();
											prd.setShopName(shopNm);
											onebuMatch = true;
											
											prd.setOnebuLink(driver.getCurrentUrl());
											prd.setOnebuInnerRank(onebuInnerRank);
											prd.setSinglePrdLink("");
											onebuInnerRank = 0;
											break;
										}
									}
								}
								idx++;
							} else {
								for(WebElement el2 : list) {
									if(idx >= 9) break;
									if(onebuMatch || singleMatch) break;
									
									//??????????????? ????????? ????????? ??????
									el2.click();
									onebuInnerRank = 0;
									Thread.sleep(300);
									
									list = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li"));
									//li ??????
									for(WebElement el3 : list) {
										onebuInnerRank++;
										
										List<WebElement> as = el3.findElements(By.cssSelector("a"));
										//li ????????? a ?????? ??????
										for(WebElement aTag : as) {
											String href = aTag.getAttribute("href");
											if(href.contains(nvmid)) {
												String shopNm = el3.findElement(By.cssSelector("div[class^=productList_inner] > div[class^=productList_mall] a span")).getText();
												prd.setShopName(shopNm);
												onebuMatch = true;
												
												prd.setOnebuLink(driver.getCurrentUrl());
												prd.setOnebuInnerRank(onebuInnerRank);
												prd.setSinglePrdLink("");
												onebuInnerRank = 0;
												break;
											}
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
				if(!onebuMatch && !singleMatch) {
					log.info("?????? : " + prd.getId() + " // " + prd.getKeyword());
					prd.setSuccessYn("N");
				} else {
					String workPer = ((double) workCnt / (double) prds.size() * 100.0 + "%");
					log.info("[" + workPer + "] - ???????????? : " + prd.getOnebuRank() + " ??????????????? : " + prd.getOnebuInnerRank() + " ?????????????????? : " + prd.getPrdRank());
					prd.setSuccessYn("Y");
				}
				productRepository.save(prd);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		driver.quit();
	}
	
	@Override
	public void work2(int driverIdx, List<ProductEntity> prds) {
		log.info(driverIdx + "??? ???????????? ?????? : " + prds.size());
		WebDriver driver = pcDriver.getPcDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("Object.defineProperty(navigator, 'plugins', {get: function() {return[1, 2, 3, 4, 5]}})");
//		js.executeScript("Object.defineProperty(navigator, 'languages', {get: function() {return ['ko-KR', 'ko']}})");
//		js.executeScript("const getParameter = WebGLRenderingContext.getParameter;WebGLRenderingContext.prototype.getParameter = function(parameter) {if (parameter === 37445) {return 'NVIDIA Corporation'} if (parameter === 37446) {return 'NVIDIA GeForce GTX 980 Ti OpenGL Engine';}return getParameter(parameter);};");
		LocalDateTime now = LocalDateTime.now();
		int workCnt = 0;
		for(ProductEntity prd : prds) {
			workCnt++;
			if(prd.getKeyword().equals("") && prd.getNvmid().equals("")) continue;
			
			HistoryEntity history = new HistoryEntity();
			history.setOnebuInnerRank(prd.getOnebuInnerRank());
			history.setOnebuRank(prd.getOnebuRank());
			history.setPrdRank(prd.getPrdRank());
			history.setWorkDate(now);
			prd.addHistory(history);
			
			prd.setPrvOnebuRank(prd.getOnebuRank());
			prd.setPrvOnebuInnerRank(prd.getOnebuInnerRank());
			prd.setPrvPrdRank(prd.getPrdRank());
			
			prd.setWorkDate(now);
			try {
				String nvmid = prd.getNvmid();
				String option = prd.getOnebuOptionName();
				int onebuRank = 0;
				int onebuInnerRank = 0;
				int prdRank = 0;
				
				boolean onebuMatch = false;
				boolean singleMatch = false;
				for (int page = 1; page <= 3; page++) {
					if(onebuMatch || singleMatch) break;
					driver.get("https://search.shopping.naver.com/search/all?frm=NVSHATC&origQuery=" + prd.getKeyword() + "&pagingIndex=" + page + "&pagingSize=40&productSet=total&query=" + prd.getKeyword() + "&sort=rel&timestamp=&viewType=list");
					Thread.sleep(2000);
					for (int i = 1; i < 20; i++) {
						Thread.sleep(300);
						js.executeScript("window.scrollBy(0, " + (1000 * i) + ")");
					}
					List<WebElement> list = driver.findElements(By.cssSelector("ul.list_basis li[class^=basicList_item]"));
					List<WebElement> singlePrdList = new ArrayList<> ();
					List<WebElement> onebuPrdList = new ArrayList<> ();
					
					for (WebElement el : list) {
						String classNm = el.getAttribute("class");
						
						if (!classNm.contains("ad")) {
							String html = el.getText();
							if(html.contains("????????? ????????????") || html.contains("???????????? ?????????")) {
								onebuPrdList.add(el);
							} else {
								singlePrdList.add(el);
							}
						}
					}
					
					//?????? ?????? ??????
					for(WebElement el : singlePrdList) {
						prdRank++;
						WebElement a = el.findElement(By.cssSelector("div[class^=basicList_info_area] > div[class^=basicList_title] a"));
						String href = a.getAttribute("href");
						if(href.contains(nvmid)) {
							String shopNm = el.findElement(By.cssSelector("div[class^=basicList_mall_area] > div[class^=basicList_mall_title] a")).getText();
							prd.setShopName(shopNm);
							prd.setSinglePrdLink(driver.getCurrentUrl());
							prd.setOnebuLink("");
							singleMatch = true;
							break;
						}
					}
					
					
					//?????? ?????? ??????
					for (WebElement el : onebuPrdList) {
						if(onebuMatch || singleMatch) break;
						onebuRank++;
						WebElement a = el.findElement(By.cssSelector("div[class^=basicList_info_area] > div[class^=basicList_title] a"));
						//?????? ?????? ?????? -> ??? ??????
						a.click();
						ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
						driver.switchTo().window(tabs.get(1));
						Thread.sleep(2000);
						//?????? ?????? ?????? ?????? ??????
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
								System.out.println("?????? ????????? ?????????");
								log.info("?????? ????????? ????????? : " + prd.getId() + " // " + prd.getKeyword());
								driver.close();
								driver.switchTo().window(tabs.get(0));
								continue;
							}
						}
						
						list = driver.findElements(By.cssSelector("div[class^=productList_seller_wrap] div[class^=pagination_pagination] a"));
						int idx = 0;
						try {
							//?????? ?????? ?????? ?????? ???????????? ??????
							if(list.equals(null) || list.size() == 0) {
								onebuInnerRank = 0;
								list = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li"));
								//li ??????
								for(WebElement el3 : list) {
									onebuInnerRank++;
									
									List<WebElement> as = el3.findElements(By.cssSelector("a"));
									//li ????????? a ?????? ??????
									for(WebElement aTag : as) {
										String href = aTag.getAttribute("href");
										if(href.contains(nvmid)) {
											String shopNm = el3.findElement(By.cssSelector("div[class^=productList_inner] > div[class^=productList_mall] a span")).getText();
											prd.setShopName(shopNm);
											onebuMatch = true;
											
											prd.setOnebuLink(driver.getCurrentUrl());
											prd.setOnebuInnerRank(onebuInnerRank);
											prd.setSinglePrdLink("");
											onebuInnerRank = 0;
											break;
										}
									}
								}
								idx++;
							} else {
								for(WebElement el2 : list) {
									if(idx >= 9) break;
									if(onebuMatch || singleMatch) break;
									
									//??????????????? ????????? ????????? ??????
									el2.click();
									onebuInnerRank = 0;
									Thread.sleep(1000);
									
									list = driver.findElements(By.cssSelector("ul[class^=productList_list_seller] li"));
									//li ??????
									for(WebElement el3 : list) {
										onebuInnerRank++;
										
										List<WebElement> as = el3.findElements(By.cssSelector("a"));
										//li ????????? a ?????? ??????
										for(WebElement aTag : as) {
											String href = aTag.getAttribute("href");
											if(href.contains(nvmid)) {
												String shopNm = el3.findElement(By.cssSelector("div[class^=productList_inner] > div[class^=productList_mall] a span")).getText();
												prd.setShopName(shopNm);
												onebuMatch = true;
												
												prd.setOnebuLink(driver.getCurrentUrl());
												prd.setOnebuInnerRank(onebuInnerRank);
												prd.setSinglePrdLink("");
												onebuInnerRank = 0;
												break;
											}
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
				if(!onebuMatch && !singleMatch) {
					log.info("?????? : " + prd.getId() + " // " + prd.getKeyword());
					prd.setSuccessYn("N");
				} else {
					String workPer = ((int)((double) workCnt / (double) prds.size() * 100.0)) + "%";
					log.info(driverIdx + " - [" + workPer + "] - ???????????? : " + prd.getOnebuRank() + " ??????????????? : " + prd.getOnebuInnerRank() + " ?????????????????? : " + prd.getPrdRank());
					prd.setSuccessYn("Y");
				}
				productRepository.save(prd);
			} catch(Exception e) {
				log.info(driverIdx + " ??? ???????????? ??????!");
				log.info(e.getMessage());
			}
		}
		driver.quit();
	}

	@Override
	public void multiWork() {
		List<ProductEntity> prds = productRepository.findAllEntityGraph();
		int driverCnt = 5;
		ArrayList<ArrayList<ProductEntity>> arrList = new ArrayList<ArrayList<ProductEntity>>();
		for (int cnt = 0; cnt < driverCnt; cnt++) {
			arrList.add(new ArrayList<ProductEntity>());
		}
		
		int idx = -1;
		for(int i = 0; i < prds.size(); i++) {
			if(i % (prds.size()/driverCnt) == 0) idx++;
			if(idx == driverCnt) idx--;
			arrList.get(idx).add(prds.get(i));
		}
		
		try {
			int driverIdx = 0;
			for(ArrayList<ProductEntity> list : arrList) {
				RunnableWorker runnable = new RunnableWorker(driverIdx++, list, this);
				
				Thread thread = new Thread(runnable);
				thread.start();
				thread.join(5000);
			}
		} catch(Exception e) {
			
		}
		
	}

}
