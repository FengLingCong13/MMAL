package com.xhhp.mmall.dao;

import com.xhhp.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getByOrderNoAndUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);


    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

    List<OrderItem> manageList(@Param("orderNo") Long orderNo);
}
