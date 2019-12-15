package com.owwang.mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.owwang.mall.content.service.ContentCategoryService;
import com.owwang.mall.pojo.EasyUITreeNode;

/**
 * 内容分类的处理controller
 * @author Samuel
 * @data 2019年12月15日
 */
@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService service;
	
	@RequestMapping(value="/content/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue="0") Long parentId){
		service.getContentCategooryList(parentId);
		return null;
	}
}
