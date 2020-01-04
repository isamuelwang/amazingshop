package com.owwang.mall.cart.service.impl;

import com.owwang.mall.cart.jedis.JedisClient;
import com.owwang.mall.cart.service.CartService;
import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Classname CartServiceImpl
 * @Description TODO
 * @Date 2020-01-02
 * @Created by WANG
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${MALL_CART_REDIS_PRE_KEY}")
    private String MALL_CART_REDIS_PRE_KEY;

    @Override
    public MallResult addItemCart(Long userId, TbItem item, Integer num) {
        //1.从Redis中查询该用户购物车列表
        TbItem tbItemInCart = queryItemByUserIdAndItemId(userId, item.getId());
        //2.判断购物车中是否有该商品
        if (tbItemInCart == null) {
            //如果不存在,则添加商品信息入购物车
            //设置图片
            String image = item.getImage();
            if (StringUtils.isNotBlank(image)) {
                item.setImage(image.split(",")[0]);
            }
            //设置数量
            item.setNum(num);
            //把商品添加到Redis购物车中
            jedisClient.hset(MALL_CART_REDIS_PRE_KEY + ":" + userId, item.getId() + "",
                    JsonUtils.objectToJson(item));
        } else {
            //如果存在，则商品数量增加
            tbItemInCart.setNum(tbItemInCart.getNum() + num);
            //重新设置回去
            jedisClient.hset(MALL_CART_REDIS_PRE_KEY + ":" + userId, item.getId() + "",
                    JsonUtils.objectToJson(tbItemInCart));
        }
        return MallResult.ok();
    }

    @Override
    public TbItem queryItemByUserIdAndItemId(Long userId, Long itemId) {
        String str = jedisClient.hget(MALL_CART_REDIS_PRE_KEY + ":" +
                userId, itemId + "");
        if (StringUtils.isNotBlank(str)) {
            TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
            return item;
        }
        return null;
    }

    @Override
    public List<TbItem> queryCartListByUserId(Long userId) {
        Map<String, String> itemMapInCart = jedisClient.hgetAll(
                MALL_CART_REDIS_PRE_KEY + ":" + userId);
        Set<Map.Entry<String, String>> set = itemMapInCart.entrySet();
        if (set != null) {
            List<TbItem> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : set) {
                TbItem tbItem = JsonUtils.jsonToPojo(entry.getValue(), TbItem.class);
                list.add(tbItem);
            }
            return list;
        }
        return null;
    }

    @Override
    public MallResult updateCartItemByItemId(Long userId, Long itemId, Integer num) {
        TbItem item = queryItemByUserIdAndItemId(userId, itemId);
        if (item != null) {
            item.setNum(num);
            jedisClient.hset(MALL_CART_REDIS_PRE_KEY + ":" + userId,
                    itemId + "",JsonUtils.objectToJson(item));
        }
        return MallResult.ok();
    }

    @Override
    public MallResult deleteByItemId(Long userId, Long itemId) {
        jedisClient.hdel(MALL_CART_REDIS_PRE_KEY + ":" + userId, itemId + "");
        return MallResult.ok();
    }

    @Override
    public MallResult updateCartItemByJson(List<TbItem> items,Long userId) {
        for(TbItem item : items){
            jedisClient.hset(MALL_CART_REDIS_PRE_KEY + ":" + userId,
                    item.getId()+"",JsonUtils.objectToJson(item));
        }
        return MallResult.ok();
    }


}
