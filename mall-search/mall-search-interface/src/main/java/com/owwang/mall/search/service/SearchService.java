package com.owwang.mall.search.service;

import com.owwang.mall.pojo.MallResult;

/**
 * @Classname SearchItemService
 * @Description TODO
 * @Date 2019-12-24
 * @Created by WANG
 */
public interface SearchService {

    //导入所有的商品数据到索引库中
    MallResult importAllSearchItems() throws Exception;
}
