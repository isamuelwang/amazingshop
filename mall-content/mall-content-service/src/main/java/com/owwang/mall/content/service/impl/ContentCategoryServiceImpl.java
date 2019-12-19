package com.owwang.mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.owwang.mall.mapper.TbContentMapper;
import com.owwang.mall.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.owwang.mall.content.service.ContentCategoryService;
import com.owwang.mall.mapper.TbContentCategoryMapper;
import com.owwang.mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;

	@Autowired
	private TbContentMapper contentMapper;
	
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
	

	@Override
	public MallResult createContentCategory(Long parentid, String name) {
		//1.构建对象，补全其它属性
		TbContentCategory category = new TbContentCategory();
		category.setCreated(new Date());
		category.setIsParent(false);
		category.setName(name);
		category.setParentId(parentid);
		category.setSortOrder(1);
		category.setStatus(1);
		category.setUpdated(category.getCreated());
		//2.插入contentCategory数据
		mapper.insertSelective(category);
		//3.返回result添加id 需要主键返回
		//判断父为叶子节点，则更新为父节点
		TbContentCategory parent = mapper.selectByPrimaryKey(parentid);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			mapper.updateByPrimaryKeySelective(parent);
		}
		return MallResult.ok(category);
	}

	/**
	 * @Description TODO
	 * @param
	 * @param id
	 * @param name
	 * @return void
	 * @data 2019/12/19
	 * @auther Samuel
	 */
	@Override
	public void updateContentCategory(Long id, String name) {
		TbContentCategory category = mapper.selectByPrimaryKey(id);
		category.setName(name);
		mapper.updateByPrimaryKeySelective(category);
	}

	@Override
	public EasyUIDataGriResult getContentsByCategoryId(Integer page,Integer rows,Long categoryId) {
		//设置默认页数和行数
		if(page==null)page=1;
		if(rows==null)rows=20;
		//开始分页查询
		PageHelper.startPage(page,rows);
		TbContentExample example = new TbContentExample();
		TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		//得到查询信息
		PageInfo<TbContent> info =new PageInfo(list);
		//设置返回值信息
		EasyUIDataGriResult result = new EasyUIDataGriResult();
		result.setTotal((int)info.getTotal());
		result.setRows(list);
		return result;
	}

}
