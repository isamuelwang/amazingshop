package com.owwang.mall.controller;

import java.util.List;

import com.owwang.mall.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.owwang.mall.content.service.ContentCategoryService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 内容分类的处理controller
 * @author Samuel
 * @data 2019年12月15日
 */
@Controller
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService service;
	
	@RequestMapping(value = "/content/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> list = service.getContentCategooryList(parentId);
		return list;
	}

	//url:content/category/create
	//method:post
	//返回值：mallresult
	@RequestMapping(value = "/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public MallResult createContentCategory(Long parentId,String name){
		MallResult result = service.createContentCategory(parentId, name);
		return result;
	}

	//$.post("/content/category/update",{id:node.id,name:node.text});
	/**
	 * 重名名节点
	 * @param id 节点id
	 * @param name 需要重新命名的名字
	 */
	@RequestMapping(value = "/content/category/update",method = RequestMethod.POST)
	@ResponseBody
	public void updateContentCategory(Long id,String name){
		service.updateContentCategory(id,name);
	}

	//$.post("/content/category/delete/",{parentId:node.parentId,id:node.id}
	@RequestMapping(value = "/content/category/delete/",method = RequestMethod.POST)
	public void deleteContentCategory(Long parentId,Long id){

	}

	//url:'/content/query/list',queryParams:{categoryId:0}
	@RequestMapping(value = "/content/query/list",method = RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGriResult getContentsByCategoryId(Integer page,Integer rows,Long categoryId){
		return service.getContentsByCategoryId(page,rows,categoryId);
	}

}
