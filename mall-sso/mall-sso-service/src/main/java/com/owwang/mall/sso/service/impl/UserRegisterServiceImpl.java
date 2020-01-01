package com.owwang.mall.sso.service.impl;

import com.owwang.mall.mapper.TbUserMapper;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbUser;
import com.owwang.mall.pojo.TbUserExample;
import com.owwang.mall.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname UserRegisterServiceImpl
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    @Autowired
    private TbUserMapper userMapper;

    public MallResult checkData(String param, Integer type) {
        //1.注入
        //2.根据param的生成查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            //username
            if (StringUtils.isEmpty(param)) {
                return MallResult.ok(false);
            }
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            //phone
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            //email
            criteria.andEmailEqualTo(param);
        } else {
            //非法参数
            return MallResult.build(400, "非法的参数");
        }
        //3.通过mapper查询数据
        List<TbUser> users = userMapper.selectByExample(example);
        //4.如果查询到了数据 数据不可用 false
        if (users != null && users.size() > 0) {
            return MallResult.ok(false);
        }

        //5.如果没有查到数据 数据可用 true
        return MallResult.ok(true);
    }
}
