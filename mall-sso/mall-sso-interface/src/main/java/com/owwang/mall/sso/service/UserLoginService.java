package com.owwang.mall.sso.service;

import com.owwang.mall.pojo.MallResult;

/**
 * @Classname UserLoginService
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
public interface UserLoginService {

    /**
     * 根据用户名和密码登录
     * @Description TODO
     * @param username
     * @param password
     * @return com.owwang.mall.pojo.MallResult 登录成功返回200 并且返回一个token数据；
     * 失败则返回400
     * @Date 2020-01-01
     * @auther Samuel
     */
    MallResult login(String username, String password);

    /**
     * 根据token获取用户信息
     * @Description TODO
     * @param token 登录令牌，同时存于Redis作为key和cookie中
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-01
     * @auther Samuel
     */
    MallResult getUserByToken(String token);

    /**
     * 安全退出
     * @Description TODO
     * @param token 登录令牌
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-01
     * @auther Samuel
     */
    MallResult logout(String token);
}
