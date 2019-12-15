package com.owwang.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页管理
 * @author Samuel
 * @data 2019年12月13日
 */
@Controller
public class PageController {
	
	@RequestMapping("/index")
	public String showIndex(){
		return "index";
	}
}
