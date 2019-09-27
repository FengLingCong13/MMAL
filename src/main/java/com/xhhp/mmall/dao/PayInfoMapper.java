package com.xhhp.mmall.dao;

import com.xhhp.mmall.pojo.PayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}