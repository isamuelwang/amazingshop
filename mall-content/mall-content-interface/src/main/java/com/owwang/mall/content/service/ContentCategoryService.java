package com.owwang.mall.content.service;

import java.util.List;

import com.owwang.mall.pojo.EasyUIDataGriResult;
import com.owwang.mall.pojo.EasyUITreeNode;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;

public interface ContentCategoryService {
	
	//通过节点的id查询节点的子节点列表
	List<EasyUITreeNode> getContentCategooryList(Long parentId);

	/**
	 * 添加内容分类
	 * @param parentid 父节点id
	 * @param name 新增节点名称
	 * @return
	 */
	MallResult createContentCategory(Long parentid,String name);

	/**
	 * 修改内容分类名称
	 * @param parentid 父节点id
	 * @param name 新增节点名称
	 */
	void updateContentCategory(Long parentid,String name);

	/**
	 * 通过categoryId查找内容
	 * @param categoryId 目录ID
	 * @return 内容
	 */
	EasyUIDataGriResult getContentsByCategoryId(Integer page,Integer rows,Long categoryId);

}
