package com.owwang.mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.owwang.mall.mapper.TbItemMapper;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbItemExample;


public class TestPageHlper {
	@Test
	public void testhelper(){
		//1.设置分页信息
		PageHelper.startPage(10,10);//第10页开始，每页10行
		//2.初始化spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//3.获取mapper代理对象
		TbItemMapper tbItemMapper = context.getBean(TbItemMapper.class);
		//4.调用mapper方法查询数据
		TbItemExample example = new TbItemExample();//不设置条件
		List<TbItem> list = tbItemMapper.selectByExample(example);//select * from tb_itm

		//取分页信息
		PageInfo<TbItem> info = new PageInfo<TbItem>(list);
		
		//5.循环遍历结果集
		System.out.println("查询的总记录数："+info.getTotal());
		for(TbItem tbItem : list){
			System.out.println(tbItem.getId()+">>>mingch>>"+tbItem.getTitle());
		}
	}
}
