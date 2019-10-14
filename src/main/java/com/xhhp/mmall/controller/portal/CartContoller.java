package com.xhhp.mmall.controller.portal;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.JsonUtil;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.ICartService;
import com.xhhp.mmall.util.CookieUtil;
import com.xhhp.mmall.util.RedisPoolUtil;
import com.xhhp.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

/**
 * CartColler class
 *
 * @author Flc
 * @date 2019/9/28
 */
@Controller
@RequestMapping("cart/")
public class CartContoller {

    @Autowired
    private ICartService iCartService;


    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request,HttpSession session) {

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping(value = "add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest request,HttpSession session, @RequestParam("count") Integer count,@RequestParam("productId") Integer producId) {
        if(producId == null || count == null) {
            return ServerResponse.createByERRORMessage("参数错误");
        }

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.add(user.getId(),  producId, count);
    }

    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest request,HttpSession session, @RequestParam("count")Integer count,@RequestParam("productId") Integer producId) {
        if(producId == null || count == null) {
            return ServerResponse.createByERRORMessage("参数错误");
        }

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.update(user.getId(), producId, count);
    }

    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpServletRequest request,HttpSession session,@RequestParam("productIds") String productIds) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.delete(user.getId(), productIds);
    }


    //全选
    @RequestMapping(value = "select_all.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request,HttpSession session,String producIds) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, null);
    }

    //全不选
    @RequestMapping(value = "un_select_all.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request,HttpSession session, @RequestParam("productId") Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, null);
    }

    //选择
    @RequestMapping(value = "select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest request,HttpSession session, @RequestParam("productId")Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.CHECKED, productId);
    }

    //不选择
    @RequestMapping(value = "un_select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest request,HttpSession session, @RequestParam("productId") Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        return iCartService.selectOrUnSelect(user.getId(), Const.Cart.UN_CHECKED, productId);
    }

    @RequestMapping(value = "get_cart_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request,HttpSession session) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }




}
