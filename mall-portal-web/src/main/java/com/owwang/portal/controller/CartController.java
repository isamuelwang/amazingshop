package com.owwang.portal.controller;

import com.owwang.mall.cart.service.CartService;
import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbUser;
import com.owwang.mall.service.ItemService;
import com.owwang.mall.sso.service.UserLoginService;
import com.owwang.mall.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname CartController
 * @Description TODO
 * @Date 2020-01-02
 * @Created by WANG
 */
@Controller
public class CartController {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Value("${TT_CART_KEY}")
    private String TT_CART_KEY;
    @Value(value = "${TT_CART_EXPIRE_TIME}")
    private String TT_CART_EXPIRE_TIME;

    /**
     * 添加商品到购物车
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return java.lang.String
     * @Description TODO
     * @Date 2020-01-03
     * @auther Samuel
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest request,
                          HttpServletResponse response) {
        //1.获取token
        String tt_token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        //2.根据token获取用户信息
        MallResult resultByToken = userLoginService.getUserByToken(tt_token);
        if (resultByToken.getStatus() == 200) {
            TbItem item = itemService.getItemById(itemId);
            TbUser user = (TbUser) resultByToken.getData();
            cartService.addItemCart(user.getId(), item, num);
        } else {
            //用户未登录
            //存在cookie中
            addCookieCartItem(itemId,num,request,response);
        }
        //跳转到购物车页面
        return "cartSucess";
    }

    /**
     * 展示购物车
     * @param
     * @param request
     * @return java.lang.String
     * @Description TODO
     * @Date 2020-01-03
     * @auther Samuel
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request) {
        //通过token获取用户信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        //根据token调用SSO服务，获取用户信息
        MallResult result = userLoginService.getUserByToken(token);
        //判断是否已经登录
        if (result.getStatus() == 200) {
            //同步本地购物车
            loginMergeCart(request);
            TbUser user = (TbUser) result.getData();
            List<TbItem> tbItems = cartService.queryCartListByUserId(user.getId());
            request.setAttribute("cartList", tbItems);
        } else {
            //用户未登录
            List<TbItem> cartList = getCartList(request);
            request.setAttribute("cartList",cartList);
        }
        return "cart";
    }


    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
                                 HttpServletResponse response) {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        MallResult result = userLoginService.getUserByToken(token);
        if(result.getStatus()==200){
            //已经登录
            TbUser user = (TbUser)result.getData();
            cartService.deleteByItemId(user.getId(),itemId);
        }else {
            //没有登录
            deleteCookieCartItem(request,response,itemId);
        }
        return "redirect:/cart/cart.html";
    }

    /**
     * 购物车中添加商品数量
     * @Description TODO
     * @param itemId
     * @param num
     * @param request
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-03
     * @auther Samuel
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public MallResult updateCartNum(@PathVariable Long itemId, @PathVariable Integer num,
                                    HttpServletRequest request,HttpServletResponse response) {
        //获取cookie中的user信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        //通过token获取user信息
        MallResult result = userLoginService.getUserByToken(token);
        if (result.getStatus() == 200) {
            //已经登录
            //更新Redis
            TbUser user = (TbUser) result.getData();
            cartService.updateCartItemByItemId(user.getId(),itemId,num);
            return MallResult.ok();
        }else {
            //如果没有登录
            updateCartItem(itemId,num,request,response);
            return MallResult.ok();
        }
    }


    /**
     * 合并本地与云端的购物车
     * @Description TODO
     * @param
     * @param request
     * @return com.owwang.mall.pojo.MallResult
     * @Date 2020-01-03
     * @auther Samuel
     */
    @RequestMapping("/cart/syn")
    @ResponseBody
    public MallResult loginMergeCart(HttpServletRequest request){
        //本地购物车
        List<TbItem> localCart = getCartList(request);
        //通过token获取用户信息
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        //根据token调用SSO服务，获取用户信息
        MallResult result = userLoginService.getUserByToken(token);
        TbUser user = (TbUser) result.getData();
        //Redis里的购物车
        List<TbItem> redisCart = cartService.queryCartListByUserId(user.getId());
        //同步两个购物车
        for(TbItem lItem : localCart){
            for(TbItem rItem : redisCart){
                if(lItem.getId()==rItem.getId()){
                    lItem.setNum(rItem.getNum()+lItem.getNum());
                    redisCart.remove(rItem);
                }
            }
            redisCart.add(lItem);
        }
        MallResult result1 = cartService.updateCartItemByJson(redisCart, user.getId());
        return result1;
    }

    /**
     * 通过cookie更新用户的购物车
     * @Description TODO
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return void
     * @Date 2020-01-03
     * @auther Samuel
     */
    private void updateCartItem(Long itemId,Integer num, HttpServletRequest request,
                                HttpServletResponse response){
        List<TbItem> cartList = getCartList(request);
        //判断列表中的商品id与cookie中的商品id是否一致
        boolean flag = false;
        //查找对象商品id在cartlist中的商品
        for(TbItem item : cartList){
            if(item.getId()==itemId.longValue()){
                item.setNum(num);
                flag=true;
                break;
            }
        }

        Integer TT_CART_EXPIRE_TIME1 = Integer.parseInt(TT_CART_EXPIRE_TIME);
        if(flag=true){
            CookieUtils.setCookie(request,response,TT_CART_KEY,
                    JsonUtils.objectToJson(cartList),TT_CART_EXPIRE_TIME1,true);
        }
    }

    /**
     * 从cookie中获取购物车信息
     * @Description TODO
     * @param request
     * @return java.util.List<com.owwang.mall.pojo.TbItem>
     * @Date 2020-01-03
     * @auther Samuel
     */
    private List<TbItem> getCartList(HttpServletRequest request){
        //从cookie中获取商品列表
        String jsonStr = CookieUtils.getCookieValue(request, TT_CART_KEY,true);
        List<TbItem> list = new ArrayList<>();
        if(StringUtils.isNotBlank(jsonStr)){
            list = JsonUtils.jsonToList(jsonStr,TbItem.class);
        }
        return list;
    }

    /**
     * 删除cookie中某个商品
     * @Description TODO
     * @param request
     * @param response
     * @param itemId
     * @return void
     * @Date 2020-01-03
     * @auther Samuel
     */
    private void deleteCookieCartItem(HttpServletRequest request,
                                      HttpServletResponse response,Long itemId){
        List<TbItem> cartList = getCartList(request);
        boolean flag = false;
        for (TbItem item : cartList){
            if(item.getId()==itemId.longValue()){
                cartList.remove(item);
                flag=true;
                break;
            }
        }
        Integer TT_CART_EXPIRE_TIME1 = Integer.parseInt(TT_CART_EXPIRE_TIME);
        if(flag=true){
            CookieUtils.setCookie(request,response,
                    TT_CART_KEY,JsonUtils.objectToJson(cartList),
                    TT_CART_EXPIRE_TIME1,true);
        }
    }

    /**
     * 无登录状态下添加商品到购物车存储于cookie中
     * @Description TODO
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return void
     * @Date 2020-01-03
     * @auther Samuel
     */
    private void addCookieCartItem(Long itemId, Integer num,
                                   HttpServletRequest request,HttpServletResponse response){
        //获取购物车信息
        List<TbItem> cartList = getCartList(request);
        boolean flag = false;
        if(cartList!=null){
            for(TbItem tbItem : cartList){
                if(tbItem.getId()==itemId.longValue()){
                    tbItem.setNum(tbItem.getNum()+num);
                    flag = true;
                    break;
                }
            }
        }else {
            cartList = new ArrayList<>();
        }
        Integer TT_CART_EXPIRE_TIME1 = Integer.parseInt(TT_CART_EXPIRE_TIME);
        if(flag==true){
            //如果cookie中有该商品
            CookieUtils.setCookie(request,response,TT_CART_KEY,
                    JsonUtils.objectToJson(cartList), TT_CART_EXPIRE_TIME1,true);
        }else {
            //如果cookie中没有该商品
            TbItem item = itemService.getItemById(itemId);
            item.setNum(num);
            if(StringUtils.isNotBlank(item.getImage())){
                item.setImage(item.getImage().split(",")[0]);
            }
            cartList.add(item);
            CookieUtils.setCookie(request,response,TT_CART_KEY,
                    JsonUtils.objectToJson(cartList),TT_CART_EXPIRE_TIME1,true);
        }
    }
}
