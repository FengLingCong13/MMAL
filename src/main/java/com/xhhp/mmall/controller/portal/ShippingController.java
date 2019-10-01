package com.xhhp.mmall.controller.portal;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.Shipping;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse add(HttpSession session,@RequestBody Shipping shipping) {
        User user =  (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(HttpSession session, Integer shippingId) {
        User user =  (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }


    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpSession session, @RequestBody Shipping shipping) {
        User user =  (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse select(HttpSession session,@RequestParam("shippingId")Integer shippingId) {
        User user =  (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(HttpSession session,@RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum) {
        User user =  (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iShippingService.list(user.getId(),pageSize,pageNum);
    }
}
