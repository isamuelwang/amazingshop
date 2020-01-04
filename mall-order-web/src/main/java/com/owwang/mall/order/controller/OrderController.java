package com.owwang.mall.order.controller;

import com.owwang.mall.cart.service.CartService;
import com.owwang.mall.order.pojo.OrderInfo;
import com.owwang.mall.order.service.OrderService;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbUser;
import com.owwang.mall.sso.service.UserLoginService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单处理相关
 * @Classname OrderController
 * @Description TODO
 * @Date 2020-01-03
 * @Created by WANG
 */
@Controller
public class OrderController {
    @Value("${TT_TOKEN_KEY}")
    private String TT_TOKEN_KEY;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    /**
     * 获取购物车中的信息
     * @Description TODO
     * @param request
     * @return java.lang.String
     * @Date 2020-01-03
     * @auther Samuel
     */
    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request){
/*        //1.从cookie中获取用户token
        String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
        if(StringUtils.isNotBlank(token)){
            //2.调用sso获取用户信息
            MallResult result = userLoginService.getUserByToken(token);*/
            /*if(result.getStatus()==200){*/
                /*TbUser user = (TbUser)result.getData();*/
                TbUser user =(TbUser)request.getAttribute("USER_INFO");
                List<TbItem> items = cartService.queryCartListByUserId(user.getId());
                request.setAttribute("cartList",items);
            /*}*/
       /* }*/
        return "order-cart";
    }

    /**
     * 创建订单
     * @Description TODO
     * @param info 表单使用orderinfo来进行接收
     * @return java.lang.String 逻辑视图
     * @Date 2020-01-04
     * @auther Samuel
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo info,HttpServletRequest request){
        //查询用户信息并设置到info中
        TbUser user = (TbUser)request.getAttribute("USER_INFO");
        info.setUserId(user.getId());
        info.setBuyerNick(user.getUsername());
        MallResult result = orderService.createOrder(info);
        request.setAttribute("orderId",result.getData());
        request.setAttribute("payment",info.getPayment());
        DateTime dateTime = new DateTime();//当前时间
        DateTime dateTime1 = dateTime.plus(3);
        request.setAttribute("date",dateTime1.toString("yyyy-MM-dd"));
        return "success";
    }
}
