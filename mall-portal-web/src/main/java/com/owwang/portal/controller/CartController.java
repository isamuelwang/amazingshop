package com.owwang.portal.controller;

import com.owwang.mall.cart.service.CartService;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbUser;
import com.owwang.mall.service.ItemService;
import com.owwang.mall.sso.service.UserLoginService;
import com.owwang.mall.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 添加商品到购物车
     * @Description TODO
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return java.lang.String
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

        }
        //跳转到购物车页面
        return "cartSucess";
    }

    /**
     *  展示购物车
     * @Description TODO
     * @param
     * @param request
     * @return java.lang.String
     * @Date 2020-01-03
     * @auther Samuel
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request){
        //通过token获取用户信息
        String token = CookieUtils.getCookieValue(request,"TT_TOKEN");
        //根据token调用SSO服务，获取用户信息
        MallResult result = userLoginService.getUserByToken(token);
        //判断是否已经登录
        if(result.getStatus()==200){
            TbUser user = (TbUser)result.getData();
            List<TbItem> tbItems = cartService.queryCartListByUserId(user.getId());
            request.setAttribute("cartList",tbItems);
        }else{
            //用户未登录

        }
        return "cart";
    }

}
