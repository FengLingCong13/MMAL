package com.xhhp.mmall.service;

import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;

public interface IUserSerivce {

    ServerResponse<User> login(String username, String password);
}
