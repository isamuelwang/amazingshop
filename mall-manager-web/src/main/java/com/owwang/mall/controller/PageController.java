package com.owwang.mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面显示控制
 * @author Samuel
 * @data 2019年12月11日
 */
@Controller
public class PageController {
	
	//挑战主页
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	//显示商品查询页面
	//url: /item-list
	@RequestMapping("/item-list")
	public String ShowItemList(){
		return "item-list";
	}
	
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
