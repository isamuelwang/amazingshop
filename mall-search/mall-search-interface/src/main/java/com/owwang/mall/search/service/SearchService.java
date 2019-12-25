package com.owwang.mall.search.service;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.SearchResult;

/**
 * @Classname SearchItemService
 * @Description TODO
 * @Date 2019-12-24
 * @Created by WANG
 */
public interface SearchService {

    //导入所有的商品数据到索引库中
    MallResult importAllSearchItems() throws Exception;

    /**
     * 根据搜索的条件查询搜索结果
     * @Description TODO
     * @param
     * @param queryString 查询主条件
     * @param page 查询的当前页码
     * @param rows 每页的行数
     * @return com.owwang.mall.pojo.SearchResult
     * @Date 2019-12-25
     * @auther Samuel
     */
    public SearchResult search(String queryString,Integer page,Integer rows) throws Exception;
}
