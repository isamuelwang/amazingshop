package com.owwang.mall.sso.service.impl;

import com.owwang.mall.mapper.TbUserMapper;
import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbUser;
import com.owwang.mall.pojo.TbUserExample;
import com.owwang.mall.sso.jedis.JedisClient;
import com.owwang.mall.sso.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Classname UserLoginService
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${EXPIRE_TIME}")
    private Integer EXPIRE_TIME;

    @Override
    public MallResult login(String username, String password) {
        //1.注入mapper
        //2.校验用户名和密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return MallResult.build(400, "用户或密码错误");
        }
        //3.先校验用户名
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> users = tbUserMapper.selectByExample(example);
        if (users.size() == 0) {
            return MallResult.build(400, "用户名不存在");
        }
        //4.校验密码
        TbUser user = users.get(0);
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!user.getPassword().equals(md5DigestAsHex)) {
            return MallResult.build(400, "密码错误");
        }
        //5.如果校验成功，生成token（UUID实现）
        String token = UUID.randomUUID().toString();
        //设置密码为空,防止别人拿出来破解
        user.setPassword(null);
        //存放token入Redis
        jedisClient.set(USER_INFO + token, JsonUtils.objectToJson(user));
        jedisClient.expire(USER_INFO + token, EXPIRE_TIME);
        return MallResult.ok(token);
    }

    @Override
    public MallResult getUserByToken(String token) {
        //1.注入jedisClient
        //2.根据token查询用户信息
        String strjson = jedisClient.get(USER_INFO + token);
        //3.判断用户信息是否正确
        if (StringUtils.isNotBlank(strjson)) {
            TbUser user = JsonUtils.jsonToPojo(strjson, TbUser.class);
            user.setPassword(null);
            //重新设计token过期时间
            jedisClient.expire(USER_INFO + token, EXPIRE_TIME);
            return MallResult.ok(user);
        }
        return MallResult.build(400, "用户已过期");
    }


    /**
     * 安全退出
     * @Description TODO
     * @param token
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-01
     * @auther Samuel
     */
    @Override
    public MallResult logout(String token) {
        if(StringUtils.isBlank(token)){
            return MallResult.build(400,"请先登录");
        }
        jedisClient.del(USER_INFO + token);
        return MallResult.ok();
    }
}
