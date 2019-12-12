package com.owwang.mall.service;

import com.owwang.mall.pojo.EasyUIDataGriResult;

/**
 * 商品相关处理的service接口
 * @author Samuel
 * @data 2019年12月11日
 */
public interface ItemService {
	
	/**
	 * 根据当前的页码和每页的行数进行分页查询
	 * @param page 当前页码
	 * @param rows 每页行数
	 * @return
	 */
	EasyUIDataGriResult getItemList(Integer page,Integer rows);
}
