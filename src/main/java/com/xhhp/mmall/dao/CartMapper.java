package com.xhhp.mmall.dao;

import com.xhhp.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(@Param("userId") Integer userId);

    int selectCartProductCheckedStatusByUserId(@Param("userId") Integer userId);

    int deleteByUserIdProducts(@Param("userId") Integer userId,@Param("productList")List<String> productList);

    int checkedOrUncheckedAllProduct(@Param("userId") Integer userId,@Param("checked") Integer checked, @Param("productId") Integer productId);

    int selectCartProductCount(@Param("userId")Integer userId);
}