package com.owwang.mall.search.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器的类
 *
 * @Classname GlobalExceptionResolver
 * @Description TODO
 * @Date 2019-12-27
 * @Created by WANG
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //1.日志写入到日志文件中，同时打印
        e.printStackTrace();
        //2.通知开发人员
        //SendMailUtil.send("iwangshujie@qq.com", "欧文商城全局异常", "出现异常啦，赶快处理" +"\\n"+ e.toString(), "smtp","smtp.qq.com", "592329306@qq.com", "465", "592329306@qq.com", "*******");
        //3.给用户提示
        ModelAndView modelAndView = new ModelAndView();
        //3.1设置挑战的错误页
        modelAndView.setViewName("error/exception");
        //3.2设置message
        modelAndView.addObject("message", "您的网络有问题，请重试");
        return modelAndView;
    }
}
