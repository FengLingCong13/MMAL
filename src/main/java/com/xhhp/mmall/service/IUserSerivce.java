package com.xhhp.mmall.service;

import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.dao.UserMapper;
import com.xhhp.mmall.pojo.User;

public interface IUserSerivce {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selctQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<User> getInformation(Integer userId) ;

    public ServerResponse checkAdminRole(User user) ;

}
