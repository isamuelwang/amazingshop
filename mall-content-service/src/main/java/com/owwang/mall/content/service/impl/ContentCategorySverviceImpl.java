package com.owwang.mall.content.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.owwang.mall.content.service.ContentCategoryService;
import com.owwang.mall.mapper.TbContentCategoryMapper;
import com.owwang.mall.pojo.EasyUITreeNode;
import com.owwang.mall.pojo.TbContentCategory;
import com.owwang.mall.pojo.TbContentCategoryExample;
import com.owwang.mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategorySverviceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;
	
	/**
	 * 内容分类 节点查询
	 */
	@Override
	public List<EasyUITreeNode> getContentCategooryList(Long parentId) {
		//1.注入mapper
		//2.创建example
		TbContentCategoryExample example = new TbContentCategoryExample();
		//3.设置条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//4.执行条件
		List<TbContentCategory> list = mapper.selectByExample(example);
		List<EasyUITreeNode> nodes = new ArrayList<EasyUITreeNode>();
		for(TbContentCategory tb : list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tb.getId());
			node.setState(tb.getIsParent()?"closed":"open");
			node.setText(tb.getName());
			nodes.add(node);
		}
		//5.转化result
		return nodes;
	}
	
}
