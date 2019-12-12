package com.owwang.mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.owwang.mall.mapper.TbItemMapper;
import com.owwang.mall.pojo.EasyUIDataGriResult;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbItemExample;
import com.owwang.mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Override
	public EasyUIDataGriResult getItemList(Integer page, Integer rows) {
		//1.设置分页的信息
		if(page==null)page=1;
		if(rows==null)rows=30;
		PageHelper.startPage(page,rows);
		//2.注入mapper
		//3.查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//4.取值
		PageInfo<TbItem> info = new PageInfo<TbItem>(list);
		EasyUIDataGriResult result = new EasyUIDataGriResult();
		result.setTotal((int)info.getTotal());
		result.setRows(info.getList());
		//5.传值
		return result;
	}

}
