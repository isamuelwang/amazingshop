package com.owwang.mall.order.service;

import com.owwang.mall.order.pojo.OrderInfo;
import com.owwang.mall.pojo.MallResult;

/**
 * @Classname OrderService
 * @Description TODO
 * @Date 2020-01-03
 * @Created by WANG
 */
public interface OrderService {

    /**
     * 创建订单
     * @Description TODO
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-04
     * @auther Samuel
     */
    MallResult createOrder(OrderInfo orderInfo);
}
