package com.owwang.mall.controller;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname ImportAllItems
 * @Description TODO
 * @Date 2019-12-24
 * @Created by WANG
 */
@Controller
public class ImportAllItems {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/index/importAll", method = RequestMethod.GET)
    @ResponseBody
    public MallResult importAll() throws Exception {
        //引入服务
        //注入服务
        //调用方法
        return searchService.importAllSearchItems();
    }
}
