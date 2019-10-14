package com.xhhp.mmall.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CookieUtil class
 *
 * @author Flc
 * @date 2019/10/9
 */
public class   CookieUtil {
    //private final static String COOKIE_DOMAIN = ".localhost";
    private final static String COOKIE_DOMAIN = "";
    private final static String COOKIE_NAME = "mmal_login_token";

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie:cookies) {
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//设置在根目录
        //-1代表永久
        ck.setMaxAge(60*60*24*365);
        ck.setHttpOnly(true);
        response.addCookie(ck);
    }


    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie:cookies) {
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
