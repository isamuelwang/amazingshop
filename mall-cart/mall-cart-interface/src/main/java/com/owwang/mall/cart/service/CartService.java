package com.owwang.mall.cart.service;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;

import java.util.List;

/**
 * @Classname CartService
 * @Description TODO
 * @Date 2020-01-02
 * @Created by WANG
 */
public interface CartService {
    /**
     * 增加商品进入特定用户ID购物车中
     * @Description TODO
     * @param userId 用户ID
     * @param item 商品信息
     * @param num 库存信息
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-02
     * @auther Samuel
     */
    MallResult addItemCart(Long userId, TbItem item, Integer num);

    /**
     * 查询特定ID用户购物车Redis中是否存在特定ID商品
     * @Description TODO
     * @param userId
     * @param itemId
     * @return com.owwang.mall.pojo.TbItem
     * @Date 2020-01-02
     * @auther Samuel
     */
    TbItem queryItemByUserIdAndItemId(Long userId,Long itemId);

    /**
     * 根据用户ID查询用户购物车信息
     * @Description TODO
     * @param userId 用户ID
     * @return java.util.List<com.owwang.mall.pojo.TbItem>
     * @Date 2020-01-02
     * @auther Samuel
     */
    List<TbItem> queryCartListByUserId(Long userId);
}
