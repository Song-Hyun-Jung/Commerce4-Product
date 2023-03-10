package com.digital4.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital4.schema.Category;
import com.digital4.schema.ErrorMsg;
import com.digital4.service.CategoryService;
import com.digital4.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "상품", description = "Product Related API")
@RequestMapping(value = "/rest/product")

public class CategoryController {
	@Resource
	CategoryService categorySvc;
	
	@RequestMapping(value = "/search/category/{categoryName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "상품카테고리 검색", notes = "상품 카테고리 검색하는 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Category.class),
			@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	public ResponseEntity<?> findCategory(@ApiParam(value = "카테고리명") @PathVariable String categoryName) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();

		ErrorMsg errors = new ErrorMsg();
		
		try {
			Category category = categorySvc.categorySearch(categoryName);
			return new ResponseEntity<Category>(category, header, HttpStatus.valueOf(200));  // ResponseEntity를 활용한 응답 생성
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}

	}
	
	@RequestMapping(value = "/insertCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "상품 카테고리 등록", notes = "카테고리 등록을 위한 API.")
	@ApiResponses({ @ApiResponse(code = 200, message = "성공", response = Category.class),
					@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class) })
	
	public ResponseEntity<?> insertCategory(
			@Parameter(name = "카테고리 등록을 위한 카테고리 정보", description = "", required = true) @RequestBody Category category){ 
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		Category category_res = new Category();
		
		try {
			if(categorySvc.categoryInsert(category)) {
				category_res = categorySvc.categorySearch(category.getCategoryName());
			}
			
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
		
		return new ResponseEntity<Category>(category_res, header, HttpStatus.valueOf(200)); // ResponseEntity를 활용한 응답 생성
	}
}
