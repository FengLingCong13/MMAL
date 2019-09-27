package com.xhhp.mmall.controller.backend;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.ICategoryService;
import com.xhhp.mmall.service.IUserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse<String> addCategory(HttpSession session, @RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CurrentUser);
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
    public ServerResponse setCategoryName(HttpSession session,int categoryId,String categoryName){
        User user = (User)session.getAttribute(Const.CurrentUser);
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
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId) {
        User user = (User)session.getAttribute(Const.CurrentUser);
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
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId) {
        User user = (User)session.getAttribute(Const.CurrentUser);
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
