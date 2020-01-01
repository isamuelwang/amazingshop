package com.owwang.mall.sso.controller;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbUser;
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
     * 查询登录信息是否存在接口
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

    /**
     * 注册用户接口
     * @Description TODO
     * @param user 用户信息
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-01
     * @auther Samuel
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    @ResponseBody
    public MallResult register(TbUser user){
        MallResult mallResult = userRegisterService.register(user);
        return mallResult;
    }
}
