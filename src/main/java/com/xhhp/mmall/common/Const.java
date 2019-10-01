package com.xhhp.mmall.common;

/**
 * Const class
 *
 * @author Flc
 * @date 2019/9/26
 */
public class Const {

    public static final String CurrentUser = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员
    }

    public interface Cart {
        int CHECKED = 1; //购物车选中状态
        int UN_CHECKED = 0; //购物车未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";   //购物车数量大于库存
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS"; //购物车数量小于库存
    }
}
