package com.xhhp.mmall.dao;

import com.xhhp.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdUserId(@Param("shippingId")Integer shippingId,@Param("userId")Integer userId);

    int updateByShippingIdUserId(@Param("shipping")Shipping shipping);

    Shipping selectByShippingIdUserID(@Param("shippingId")Integer shippingId,@Param("userId")Integer userId);

    List<Shipping> selectByUserId(@Param("userId")Integer userId);
}