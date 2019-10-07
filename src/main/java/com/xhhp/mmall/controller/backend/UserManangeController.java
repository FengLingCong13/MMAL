package com.xhhp.mmall.controller.backend;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IUserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * UserManangeController class
 *
 * @author Flc
 * @date 2019/9/26
 */
@Controller
@RequestMapping("/manage/user")
public class UserManangeController {

    @Autowired
    private IUserSerivce iUserSerivce;


    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserSerivce.login(username, password);
        if(response.isSuccess()) {
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN) {
                //说明用户是管理员
                session.setAttribute(Const.CurrentUser,user);
                return response;
            } else {
                return ServerResponse.createByERRORMessage("不是管理员无法登录");
            }
        }
        return response;
    }
}
