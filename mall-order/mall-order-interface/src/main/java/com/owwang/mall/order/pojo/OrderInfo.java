package com.owwang.mall.order.pojo;

import com.owwang.mall.pojo.TbOrder;
import com.owwang.mall.pojo.TbOrderItem;
import com.owwang.mall.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname OrderInfo
 * @Description TODO
 * @Date 2020-01-04
 * @Created by WANG
 */
public class OrderInfo extends TbOrder implements Serializable {
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;
    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }
    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

}

