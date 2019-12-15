package com.owwang.mall.content.service;

import java.util.List;

import com.owwang.mall.pojo.EasyUITreeNode;

public interface ContentCategoryService {
	
	//通过节点的id查询节点的子节点列表
	List<EasyUITreeNode> getContentCategooryList(Long parentId);
}
