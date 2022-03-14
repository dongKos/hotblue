package com.dh.hotblue.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.dh.hotblue.product.dto.ProductDto.ProductList;
import com.dh.hotblue.product.dto.ProductDto.ProductSearch;
import com.dh.hotblue.product.entity.ProductEntity;
import com.dh.hotblue.product.entity.QProductEntity;
import com.dh.hotblue.product.repository.ProductRepository;
import com.dh.hotblue.product.service.ProductService;
import com.dh.hotblue.util.ExcelUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ExcelUtil excelUtil;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	JPAQueryFactory jpaQueryFactory;

	@Override
	public boolean excelUpload(MultipartFile file) {
		boolean result = true;
		if (file.isEmpty()) {
			return false;
		}
		List<ProductEntity> list = new ArrayList<>();
		List<Map<String, Object>> listMap = excelUtil.getListData(file, 0, 2);
		for (Map<String, Object> map : listMap) {
			ProductEntity prd = new ProductEntity();
			prd.setKeyword(map.get("0").toString());
			prd.setPrdName(map.get("1").toString());
			String uri = prd.getPrdName();
			MultiValueMap<String, String> parameters = 
					UriComponentsBuilder.fromUriString(uri).build().getQueryParams();
			List<String> param1 = parameters.get("nvMid");
			prd.setNvmid(param1.get(0));
			list.add(prd);
		}
		productRepository.saveAll(list);
		return result;
	}

	@Override
	public List<ProductList> findProduct(ProductSearch search) {
		BooleanBuilder builder = new BooleanBuilder();
		QProductEntity product = QProductEntity.productEntity;
		return jpaQueryFactory.select(Projections.constructor(ProductList.class, product.id, product.keyword,
				product.shopName, product.prdName, product.onebuRank, product.prdRank, product.workDate, product.memo,
				product.createDate)).from(product).where(builder).fetch();
	}

}