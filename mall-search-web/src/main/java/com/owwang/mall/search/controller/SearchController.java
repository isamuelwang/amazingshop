package com.owwang.mall.search.controller;

import com.owwang.mall.pojo.SearchResult;
import com.owwang.mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname SearchController
 * @Description TODO
 * @Date 2019-12-25
 * @Created by WANG
 */
@Controller
public class SearchController {

    @Value("${ITEM_ROWS}")
    private Integer ITEM_ROWS;

    @Autowired
    private SearchService searchService;

    /**
     * 根据条件搜索商品的数据
     * @Description TODO
     * @param page 当前查询的页数
     * @param queryString 查询的关键字
     * @return java.lang.String
     * @Date 2019-12-25
     * @auther Samuel
     */
    @RequestMapping("/search")
    public String search(@RequestParam(defaultValue = "1") Integer page, @RequestParam(value = "q") String queryString, Model model) throws Exception {
        //1.引入
        //2.注入
        //3.调用
        //处理get请求乱码问题
        queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
        SearchResult searchResult = searchService.search(queryString, page, ITEM_ROWS);
        //4.设置数据传递到jsp中
        model.addAttribute("query",queryString);
        model.addAttribute("totalPages",searchResult.getPageCount());
        model.addAttribute("itemList",searchResult.getItemList());
        model.addAttribute("page",page);
        return "search";
    }
}
