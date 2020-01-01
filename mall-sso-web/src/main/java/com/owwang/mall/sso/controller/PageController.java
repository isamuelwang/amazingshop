package com.owwang.mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname PageController
 * @Description TODO
 * @Date 2020-01-01
 * @Created by WANG
 */
@Controller
public class PageController {
    @RequestMapping("/page/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }
}
