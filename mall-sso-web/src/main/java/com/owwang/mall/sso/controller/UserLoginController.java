package com.owwang.mall.sso.controller;

import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.sso.service.UserLoginService;
import com.owwang.mall.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname UserLoginController
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
@Controller
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return com.owwang.mall.pojo.MallResult
     * @Description TODO
     * @Date 2020-01-01
     * @auther Samuel
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public MallResult login(String username, String password,
                            HttpServletRequest request, HttpServletResponse response) {
        //1.引入服务（Springmvcp配置）
        //2.注入服务
        //3.调用服务
        MallResult result = userLoginService.login(username, password);
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, TOKEN_KEY,
                    result.getData().toString());
        }
        //4.设置token入cookie
        return result;
    }

    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {
        //判断是否是jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            MallResult result = userLoginService.getUserByToken(token);
            String jsonpstr = callback + "(" + JsonUtils.objectToJson(result) + ")";
            return jsonpstr;
        }
        MallResult result = userLoginService.getUserByToken(token);
        return JsonUtils.objectToJson(result);
    }

    /**
     * 安全退出
     * @param request
     * @return com.owwang.mall.pojo.MallResult
     * @Description TODO
     * @Date 2020-01-01
     * @auther Samuel
     */
    @RequestMapping(value = "/user/logout")
    @ResponseBody
    public MallResult logout(HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        MallResult result = userLoginService.logout(token);
        return result;
    }
}
