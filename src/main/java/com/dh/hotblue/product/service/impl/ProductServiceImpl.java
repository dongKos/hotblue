package com.dh.hotblue.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dh.hotblue.product.dto.ProductDto.ProductList;
import com.dh.hotblue.product.dto.ProductDto.ProductResponse;
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
			prd.setNvmid(map.get("1").toString());
			prd.setOnebuOptionName(map.get("2").toString());
			Optional<ProductEntity> optPrd = productRepository.findByNvmid(prd.getNvmid());
			if(optPrd.isPresent()) {
				optPrd.get().setKeyword(prd.getKeyword());
				optPrd.get().setNvmid(prd.getNvmid());
				optPrd.get().setOnebuOptionName(prd.getOnebuOptionName());
				productRepository.save(optPrd.get());
			} else {
				if(!prd.getKeyword().equals("") && !prd.getNvmid().equals(""))
					list.add(prd);
			}
		}
		productRepository.saveAll(list);
		return result;
	}

	@Override
	public List<ProductList> findAll(ProductSearch search) {
		QProductEntity product = QProductEntity.productEntity;
		BooleanBuilder builder = new BooleanBuilder();
		if(StringUtils.hasText(search.getKeyword())){
			builder.and(product.keyword.contains(search.getKeyword()));
		}
		if(StringUtils.hasText(search.getShopName())){ 
			builder.and(product.shopName.contains(search.getShopName()));
		}
		if(StringUtils.hasText(search.getNvmid())){ 
			builder.and(product.nvmid.contains(search.getNvmid()));
		}
		if(StringUtils.hasText(search.getMemo())){ 
			builder.and(product.memo.contains(search.getMemo()));
		}
		
		return jpaQueryFactory.select(Projections.constructor(
				ProductList.class, 
						product.id, 
						product.keyword,
						product.shopName, 
						product.onebuRank, 
						product.onebuInnerRank,
						product.prdRank,
						product.prvOnebuRank,
						product.prvOnebuInnerRank,
						product.prvPrdRank,
						product.workDate, 
						product.memo,
						product.createdDateTime,
						product.nvmid,
						product.onebuOptionName,
						product.onebuLink,
						product.singlePrdLink
					))
					.from(product)
					.where(builder)
//					.orderBy(product.createdDateTime.asc())
					.fetch();
	}

	@Override
	public ProductResponse findOne(long id) {
		QProductEntity product = QProductEntity.productEntity;
		return jpaQueryFactory.select(Projections.constructor(
				ProductResponse.class, 
						product.id, 
						product.keyword,
						product.onebuOptionName,
						product.nvmid,
						product.memo
					))
					.from(product)
					.where(product.id.eq(id))
					.fetchFirst();
	}

	@Transactional
	@Override
	public boolean update(ProductEntity product) {
		Optional<ProductEntity> optPrd = productRepository.findById(product.getId());
		if(optPrd.isPresent()) {
			optPrd.get().setKeyword(product.getKeyword());
			optPrd.get().setNvmid(product.getNvmid());
			optPrd.get().setOnebuOptionName(product.getOnebuOptionName());
			optPrd.get().setMemo(product.getMemo());
			productRepository.save(optPrd.get());
		}
		return true;
	}

	@Override
	public boolean delete(long id) {
		productRepository.deleteById(id);
		return true;
	}

	@Override
	public ProductEntity save(ProductEntity product) {
		return productRepository.save(product);
	}

	@Transactional
	@Override
	public List<ProductEntity> findAllTest() {
		List<ProductEntity> list =productRepository.findAllEntityGraph();
		System.out.println(list.size());
		return list;
	}
	

}
