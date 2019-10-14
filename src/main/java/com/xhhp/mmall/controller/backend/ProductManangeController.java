package com.xhhp.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.JsonUtil;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.Product;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IFileService;
import com.xhhp.mmall.service.IProductService;
import com.xhhp.mmall.service.IUserSerivce;
import com.xhhp.mmall.util.CookieUtil;
import com.xhhp.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * ProductManangeController class
 *
 * @author Flc
 * @date 2019/9/27
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManangeController {

    @Autowired
    private IUserSerivce iUserSerivce;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    private String ftpPrefix="http://image.xhhp.com";

    private String ftp="32";


    @RequestMapping(value = "kk.do", method = RequestMethod.POST)
    @ResponseBody
    public void kk(HttpSession session, Product product) {
        System.out.println(ftp+"666");
    }


    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest request,HttpSession session, Product product) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request,HttpSession session, @RequestParam("productId") Integer productId,@RequestParam("status") Integer status) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }


    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest request,HttpSession session, @RequestParam("productId") Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }


    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpServletRequest request,HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.getProductList(pageNum,pageSize);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }

    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpServletRequest request,HttpSession session,@RequestParam("productName") String productName,@RequestParam("productId") int productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }


    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user  = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            //String path = PropertiesUtil.getProperty("ftp.server.http.prefix");
            String targetFileName = iFileService.upload(file,path);
            String url = ftpPrefix +"/img/"+ targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);

            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }




    }





}
