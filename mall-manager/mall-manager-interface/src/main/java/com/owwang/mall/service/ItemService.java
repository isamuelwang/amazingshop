package com.owwang.mall.service;

import com.owwang.mall.pojo.EasyUIDataGriResult;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbItemDesc;

import java.util.List;

/**
 * 商品相关处理的service接口
 * @author Samuel
 * @data 2019年12月11日
 */
public interface ItemService {

	/**
	 * 根据商品ID查找商品描述
	 * @Description TODO
	 * @param itemId
	 * @return com.owwang.mall.pojo.TbItemDesc
	 * @Date 2019-12-29
	 * @auther Samuel
	 */
	TbItemDesc getItemDescById(Long itemId);

	/**
	 * 根据商品ID查询商品数据
	 * @Description TODO
	 * @param itemId 商品ID
	 * @return com.owwang.mall.pojo.TbItem
	 * @Date 2019-12-29
	 * @auther Samuel
	 */
	TbItem getItemById(Long itemId);
	
	/**
	 * 根据当前的页码和每页的行数进行分页查询
	 * @param page 当前页码
	 * @param rows 每页行数
	 * @return
	 */
	EasyUIDataGriResult getItemList(Integer page,Integer rows);

	/**
	 * 创建商品
	 * @param item
	 * @return
	 */
	MallResult createItem(TbItem item,String desc,String itemParam) throws Exception;

	/**
	 * 根据查找数量n和商品目录查询n条商品信息集合
	 * @Description TODO
	 * @param cid 商品目录
	 * @param quantity 需要查找的商品数量
	 * @return java.util.List<com.owwang.mall.pojo.TbItem>
	 * @Date 2020-01-03
	 * @auther Samuel
	 */
	List<TbItem> getItemListByCidAndQ(Long cid,Integer quantity);

}
