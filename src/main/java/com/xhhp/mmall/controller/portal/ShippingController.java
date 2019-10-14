package com.xhhp.mmall.controller.portal;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.JsonUtil;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.Shipping;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.ShippingService;
import com.xhhp.mmall.util.CookieUtil;
import com.xhhp.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ShippingController class
 *
 * @author Flc
 * @date 2019/9/28
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private ShippingService iShippingService;

    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpServletRequest request,HttpSession session, @RequestBody Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(HttpServletRequest request,HttpSession session, Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }


    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpServletRequest request,HttpSession session, @RequestBody Shipping shipping) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse select(HttpServletRequest request,HttpSession session,@RequestParam("shippingId")Integer shippingId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(HttpServletRequest request,HttpSession session,@RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.list(user.getId(),pageSize,pageNum);
    }
}
