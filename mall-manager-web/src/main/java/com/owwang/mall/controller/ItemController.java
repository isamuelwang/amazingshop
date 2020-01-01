package com.owwang.mall.controller;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;
import javassist.runtime.Desc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.owwang.mall.pojo.EasyUIDataGriResult;
import com.owwang.mall.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 获取商品信息
	 * url:/item/list
	 * method:get
	 * 参数:page rows
	 * 返回值: json
	 */
	@ResponseBody
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	public EasyUIDataGriResult getItemList(Integer page,Integer rows){
		//1.引用服务并注入
		EasyUIDataGriResult result = itemService.getItemList(page, rows);
		//2.返回值
		return result;
	}

	/**
	 * 增加商品信息
	 * @param item
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/item/save",method = RequestMethod.POST)
	@ResponseBody
	public MallResult createItem(TbItem item,String desc,String itemParams) throws Exception{
		MallResult result = itemService.createItem(item,desc,itemParams);
		return result;
	}
}
