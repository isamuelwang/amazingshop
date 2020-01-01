package com.owwang.mall.sso.controller;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.sso.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname UserRegesterController
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
@Controller
public class UserRegesterController {
    @Autowired
    private UserRegisterService userRegisterService;

    /**
     * 查询登录信息是否存在
     *
     * @param param 数据
     * @param type  数据类型 1、2、3 帐号 手机 邮箱
     * @return com.owwang.mall.pojo.MallResult
     * @Description TODO
     * @Date 2020-01-01
     * @auther Samuel
     */
    @RequestMapping(value = "/user/check/{param}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public MallResult checkData(@PathVariable String param, @PathVariable Integer type) {
        MallResult mallResult = userRegisterService.checkData(param, type);
        return mallResult;
    }
}
