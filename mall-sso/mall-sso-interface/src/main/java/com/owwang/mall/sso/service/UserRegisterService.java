package com.owwang.mall.sso.service;

import com.owwang.mall.pojo.MallResult;

/**
 * @Classname UserRegisterService
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
public interface UserRegisterService {
    /**
     * 根据参数和类型对帐号进行是否存在检查
     * @Description TODO
     * @param param 参数
     * @param type 类型；1，2，3代表username、phone、email
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-01
     * @auther Samuel
     */
    public MallResult checkData(String param,Integer type);
}
