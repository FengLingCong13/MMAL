package com.xhhp.mmall.service;

import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName,int parentId);

    ServerResponse updateCategoryName(int categoryId,String categoryName) ;

    ServerResponse<List<Category>> getChildrenPallelCategory(int categoryId) ;

    ServerResponse selectCategoryAndChildrenById(int categoryId);
}
