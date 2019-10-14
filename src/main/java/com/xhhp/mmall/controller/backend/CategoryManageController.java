package com.xhhp.mmall.controller.backend;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.JsonUtil;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.ICategoryService;
import com.xhhp.mmall.service.IUserSerivce;
import com.xhhp.mmall.util.CookieUtil;
import com.xhhp.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * CategoryManageController class
 *
 * @author Flc
 * @date 2019/9/27
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserSerivce iUserSerivce;

    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping(value="add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpServletRequest request,HttpSession session, @RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.addCategory(categoryName,parentId);
        } else {
            return ServerResponse.createByERRORMessage("没有管理员权限，需要进行登录");
        }
    }

    @RequestMapping(value = "set_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request,HttpSession session,int categoryId,String categoryName){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        } else {
            return ServerResponse.createByERRORMessage("没有管理员权限，需要进行登录");
        }
    }


    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request,HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //查询子节点d额category信息，不递归，保持平级
                return iCategoryService.getChildrenPallelCategory(categoryId);
        } else {
            return ServerResponse.createByERRORMessage("没有管理员权限，需要进行登录");
        }
    }


    @RequestMapping(value = "get_deeo_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request,HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //查询当前子节点id和递归节点的id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByERRORMessage("没有管理员权限，需要进行登录");
        }
    }
}
