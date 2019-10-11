package com.xhhp.mmall.controller.portal;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.JsonUtil;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IUserSerivce;
import com.xhhp.mmall.util.CookieUtil;
import com.xhhp.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * UserController class
 *
 * @author Flc
 * @date 2019/9/25
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserSerivce iUserSerivce;

    /**
     * 用户登录功能
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse servletResponse, HttpServletRequest request) {
        ServerResponse<User> response = iUserSerivce.login(username,password);

        if(response.isSuccess()){
            CookieUtil.readLoginToken(request);
            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }
        return response;
    }


    @RequestMapping(value = "logout.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse logOut(HttpServletRequest request, HttpServletResponse response) {
//        session.removeAttribute(Const.CurrentUser);
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email,@RequestParam("phone") String phone,@RequestParam("question") String question,@RequestParam("answer") String answer) {
        User user = new User(username,password,email,phone,question,answer);
        return iUserSerivce.register(user);
    }

    @RequestMapping(value = "check_valid.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(@RequestParam("str") String str,@RequestParam("type") String type) {
        return iUserSerivce.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo( HttpServletRequest request) {
//        User user = (User)session.getAttribute(Const.CurrentUser);
//        System.out.println(user);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByERRORMessage("用户未登录，无法获取信息");
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(@RequestParam("username") String username){
        return iUserSerivce.selctQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckQuestion(@RequestParam("username")String username, @RequestParam("question")String question, @RequestParam("answer")String answer) {
        return iUserSerivce.checkAnswer(username,question,answer);
    }


    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(@RequestParam("username") String username, @RequestParam("passwordNew") String passwordNew,@RequestParam("forgetToken") String forgetToken) {
        return iUserSerivce.forgetResetPassword(username,passwordNew,forgetToken);
    }


    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session) {

        User currentUser = (User)session.getAttribute(Const.CurrentUser);
        if(currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录");
        }
        return iUserSerivce.getInformation(currentUser.getId());
    }
}
