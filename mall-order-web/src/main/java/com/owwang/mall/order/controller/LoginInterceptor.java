package com.owwang.mall.order.controller;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.sso.service.UserLoginService;
import com.owwang.mall.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份验证拦截器
 * @Classname LoginIntercepton
 * @Description TODO
 * @Date 2020-01-04
 * @Created by WANG
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Autowired
    private UserLoginService loginService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //用户身份验证
        //取cookie中的token
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        //判断token是否存在
        if(StringUtils.isBlank(token)){
            response.sendRedirect("http://localhost:8089/page/login?redirect="
                    +request.getRequestURL().toString());
            return false;
        }
        //根据token查询Redis中用户信息
        MallResult result = loginService.getUserByToken(token);
        if(result.getStatus()!=200){
            //跳转登录页面，并传递原来页面url
            response.sendRedirect("http://localhost:8089/page/login?redirect="
                    +request.getRequestURL().toString());
            return false;
        }
        //用户信息设置进入request
        request.setAttribute("USER_INFO",result.getData());
        //验证成功，放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
