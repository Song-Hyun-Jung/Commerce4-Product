package com.digital4.sql.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.digital4.sql.vo.CategoryVO;

@Mapper
public interface CategoryMapper {
	public int insertCategory(CategoryVO categoryVO);
	public CategoryVO getCategory(String categoryName);
}
