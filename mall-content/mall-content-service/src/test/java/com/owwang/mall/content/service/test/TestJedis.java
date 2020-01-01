package com.owwang.mall.content.service.test;

import com.owwang.mall.content.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Classname TestJedis
 * @Description TODO
 * @Date 2019-12-22
 * @Created by WANG
 */
public class TestJedis {

    @Test
    public  void testSingle(){
        //初始化spring
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //获取实现类
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        //调用方法
        jedisClient.set("single","1111111");
        System.out.println(jedisClient.get("single"));
    }

}
