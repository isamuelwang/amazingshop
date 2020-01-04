package com.owwang.mall.order.service.impl;

import com.owwang.mall.mapper.TbOrderItemMapper;
import com.owwang.mall.mapper.TbOrderMapper;
import com.owwang.mall.mapper.TbOrderShippingMapper;
import com.owwang.mall.order.jedis.JedisClient;
import com.owwang.mall.order.pojo.OrderInfo;
import com.owwang.mall.order.service.OrderService;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbOrderItem;
import com.owwang.mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Classname OrderServiceImpl
 * @Description TODO
 * @Date 2020-01-04
 * @Created by WANG
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private JedisClient jedisClient;
    @Value("${GEN_ORDER_ID_KEY}")
    private String GEN_ORDER_ID_KEY;
    @Value("${GEN_ORDER_ID_INIT}")
    private String GEN_ORDER_ID_INIT;
    @Value("${GEN_ORDER_ITEM_ID_KEY}")
    private String GEN_ORDER_ITEM_ID_KEY;
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Override
    public MallResult createOrder(OrderInfo orderInfo) {
        //1.插入订单表
        //初始化key
        if(!jedisClient.exists(GEN_ORDER_ID_KEY)){
            jedisClient.set(GEN_ORDER_ID_KEY,GEN_ORDER_ID_INIT);
        }
        //获取新订单id
        String orderId = jedisClient.incr(GEN_ORDER_ID_KEY).toString();
        orderInfo.setCreateTime(new Date());
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("0");
        orderInfo.setStatus(1);
        orderInfo.setUpdateTime(orderInfo.getCreateTime());
        tbOrderMapper.insert(orderInfo);
        //2.插入订单项表
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for(TbOrderItem tbOrderItem : orderItems){
            //获取ID
            String orderItemId = jedisClient.incr(GEN_ORDER_ITEM_ID_KEY).toString();
            tbOrderItem.setId(orderItemId);
            tbOrderItem.setOrderId(orderId);
            //插入数据库
            tbOrderItemMapper.insert(tbOrderItem);
        }
        //3.插入订单物流表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(orderInfo.getCreateTime());
        orderShipping.setUpdated(orderInfo.getCreateTime());
        //插入数据库中
        tbOrderShippingMapper.insert(orderShipping);
        //4.返回订单ID
        return MallResult.ok(orderId);
    }
}
