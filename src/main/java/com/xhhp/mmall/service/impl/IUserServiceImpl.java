package com.xhhp.mmall.service.impl;

import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.dao.UserMapper;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IUserSerivce;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * IUserServiceImpl class
 *
 * @author Flc
 * @date 2019/9/25
 */
public class IUserServiceImpl implements IUserSerivce {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {



        return null;
    }
}
