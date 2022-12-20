package com.digital4.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.digital4.context.InventoryProductContext;
import com.digital4.schema.Auth;
import com.digital4.schema.Product;
import com.digital4.sql.mapper.ProductMapper;
import com.digital4.sql.vo.ProductVO;
import com.digital4.utils.HttpConnectionUtils;

@Component
public class ProductService {

	@Autowired
	ProductMapper productMapper;
	@Autowired
	InventoryProductContext inventoryContext;
	
	//상품id로 검색
	public Product productSearchById(long productId) throws Exception {	
		try {
			ProductVO pv = productMapper.getProductById(productId);
			Product product = new Product();

			if (pv != null) {
				product.setProductId(pv.getProductId());
				product.setCategoryId(pv.getCategoryId());
				product.setPrice(pv.getPrice());
				product.setProductName(pv.getProductName());
			} 
			return product;
		}catch(Exception e) {
			throw e;
		}
	
	}

	//상품 검색
	public Product productSearch(String productName) throws Exception {
		try {
			ProductVO pv = productMapper.getProduct(productName);
			Product product = new Product();
	
			if (pv != null) {
				product.setProductId(pv.getProductId());
				product.setCategoryId(pv.getCategoryId());
				product.setPrice(pv.getPrice());
				product.setProductName(pv.getProductName());
			} 
			return product;
		}catch(Exception e) {
			throw e;
		}
	
	}
		
	//고객이 키워드를 입력했을 때 키워드가 포함된 상품 검색
	public List<Product> searchByKeyword(String searchKeyword) throws Exception {
		try {
			List<Product> productList = new ArrayList<Product>();
			List<ProductVO> productVOList = productMapper.searchByKeyword(searchKeyword);
			if(productVOList.size() == 0) {
				throw new Exception("해당 상품이 없습니다.");
			}
			
			for(ProductVO pv : productVOList) {
				
				Product product = new Product();
						
				product.setProductId(pv.getProductId());
				product.setCategoryId(pv.getCategoryId());
				product.setPrice(pv.getPrice());
				product.setProductName(pv.getProductName());
					
				productList.add(product);
			}
						
			return productList;
		}catch(Exception e) {
			throw e;
		}
			
	}
	//카테고리별 상품 검색
	public List<Product> searchByCategory(String categoryName) throws Exception {
		try {
			List<Product> productList = new ArrayList<Product>();
			
			List<ProductVO> productVOList = productMapper.searchByCategory(categoryName);
			if(productVOList.size() == 0) {
				throw new Exception("해당 상품이 없습니다.");
			}
				
			for(ProductVO pv : productVOList) {
					
				Product product = new Product();
						
				product.setProductId(pv.getProductId());
				product.setCategoryId(pv.getCategoryId());
				product.setPrice(pv.getPrice());
				product.setProductName(pv.getProductName());
					
				productList.add(product);
			}
						
			return productList;
		}catch(Exception e) {
			throw e;
		}
	}
	
	
	//상품 등록
	@Transactional
	public boolean insertProduct(Product product, String token) throws Exception {

		try {
			if (productSearch(product.getProductName()).getProductName() != null) {
				throw new Exception("이미 등록된 상품 입니다.");
			}

			product.setProductId(System.currentTimeMillis());
			Thread.sleep(10);
			ProductVO pv = new ProductVO(product.getProductId(), product.getCategoryId(), product.getPrice(), product.getProductName());
				
			int insertProduct = productMapper.insertProduct(pv);
			//HttpUrlConnection->바운디드 컨텍스트
			String response = inventoryContext.insertInventory(product.getProductId(), token);
			if(insertProduct == 1 && response.contains("productId")) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			throw e;
		}
	
	}
}
