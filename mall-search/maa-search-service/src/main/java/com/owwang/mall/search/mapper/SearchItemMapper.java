package com.owwang.mall.search.mapper;

import com.owwang.mall.pojo.SearchItem;

import java.util.List;

/**
 * 定义MAPPER，查收搜索时的商品数据
 * @Classname mapper
 * @Description TODO
 * @Date 2019-12-24
 * @Created by WANG
 */
public interface SearchItemMapper {
    //查询所有商品的搜索数据
    List<SearchItem> getSearchItemList();
}
