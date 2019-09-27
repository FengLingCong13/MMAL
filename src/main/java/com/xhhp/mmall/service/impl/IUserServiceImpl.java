package com.xhhp.mmall.service.impl;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.common.TokenCache;
import com.xhhp.mmall.dao.UserMapper;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IUserSerivce;
import com.xhhp.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * IUserServiceImpl class
 *
 * @author Flc
 * @date 2019/9/25
 */
@Service("iUserService")
public class IUserServiceImpl implements IUserSerivce {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);

        if(resultCount == 0) {
            return ServerResponse.createByERRORMessage("用户名不存在");
        }

        //密码登录MD5
        String md5Password = MD5Util.md5Encrypt32Upper(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null) {
            return ServerResponse.createByERRORMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {

        ServerResponse validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()) {
           return validResponse;
        }

        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.md5Encrypt32Upper(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0) {
            return ServerResponse.createByERRORMessage("注册失败");
        }
        return ServerResponse.createByERRORMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(type)) {
            //开始校验
            if(Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(str);
                if(resultCount > 0) {
                    System.out.println(888);
                    return ServerResponse.createByERRORMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals("email")) {
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0) {
                    return ServerResponse.createByERRORMessage("email已存在");
                }
            }
        } else {
            return ServerResponse.createByERRORMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selctQuestion(String username) {
        ServerResponse validResponse = checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()) {
            return ServerResponse.createBySuccessMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNoneBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByERRORMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if(resultCount > 0) {
            //问题回答正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.set("token_"+username,forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);

        }
        return ServerResponse.createByERRORMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByERRORMessage("参数错误，需要token");

        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()) {
            return   ServerResponse.createByERRORMessage("用户不存在");
        }

        String token = TokenCache.get("token_" + username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByERRORMessage("token无效或过期");
        }
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)) {
            String md5Password = MD5Util.md5Encrypt32Upper(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByERRORMessage("token错误");
        }
        return ServerResponse.createByERRORMessage("修改密码失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);

        if(user == null) {
            return ServerResponse.createByERRORMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByERROR();
    }
}
