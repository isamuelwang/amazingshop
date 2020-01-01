package com.owwang.mall.sso.service.impl;

import com.owwang.mall.mapper.TbUserMapper;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbUser;
import com.owwang.mall.pojo.TbUserExample;
import com.owwang.mall.sso.service.UserRegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
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

    /**
     * 注册用户
     *
     * @param user 用户注册信息
     * @return com.owwang.mall.pojo.MallResult
     * @Description TODO
     * @Date 2020-01-01
     * @auther Samuel
     */
    @Override
    public MallResult register(TbUser user) {
        //1.校验数据（使用checkData方法）
        //1.1校验用户名密码不能为空
        if (StringUtils.isEmpty(user.getUsername())) {
            return MallResult.build(400, "注册失败。用户名不能为空，请校验数据后请再提交数据");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return MallResult.build(400, "注册失败。密码不能为空，请校验数据后请再提交数据");
        }
        //1.2校验用户名是否被注册
        MallResult mallResult = checkData(user.getUsername(), 1);
        if (!(boolean) mallResult.getData()) {
            return MallResult.build(400, "用户名已经被注册");
        }
        //1.3校验电话是否被注册
        if (StringUtils.isNotBlank(user.getPhone())) {
            MallResult mallResult1 = checkData(user.getPhone(), 2);
            if (!(boolean) mallResult1.getData()) {
                return MallResult.build(400, "手机已经被使用");
            }
        }
        //1.4校验邮箱是否被注册
        if (StringUtils.isNotBlank(user.getPhone())) {
            MallResult mallResult2 = checkData(user.getEmail(), 3);
            if (!(boolean) mallResult2.getData()) {
                return MallResult.build(400, "邮箱已经被使用");
            }
        }
        //2.补全其它的属性
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        //3.对密码进行MD5加密
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        //4.插入数据
        userMapper.insertSelective(user);
        //5.返回结果
        return MallResult.ok();
    }

}
