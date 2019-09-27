package com.xhhp.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.dao.CategoryMapper;
import com.xhhp.mmall.pojo.Category;
import com.xhhp.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * ICategoryServiceImpl class
 *
 * @author Flc
 * @date 2019/9/27
 */
@Service(value = "iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, int parentId) {

        if(parentId < 0 || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByERRORMessage("添加参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);

        if(rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByERRORMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(int categoryId, String categoryName) {
        if(categoryId < 0 || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByERRORMessage("更新品类参数有误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名成功");
        }
        return ServerResponse.createByERRORMessage("更新品类名失败");

    }

    @Override
    public ServerResponse<List<Category>> getChildrenPallelCategory(int categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryParallelByParentId(categoryId);

        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse selectCategoryAndChildrenById(int categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryList = Lists.newArrayList();
        if(categoryId >= 0) {
            for(Category category: categorySet) {
                categoryList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    //递归算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet,int categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoryParallelByParentId(categoryId);

        for(Category category1: categoryList) {
            findChildCategory(categorySet,category1.getId());
        }
        return categorySet;
    }


}
