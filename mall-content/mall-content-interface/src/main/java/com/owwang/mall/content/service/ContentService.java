package com.owwang.mall.content.service;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;

import java.util.List;

/**
 * @Classname ContentService
 * @Description 内容处理接口
 * @Date 2019/12/19
 * @Created by Samuel
 */
public interface ContentService {

    /**
     * @Description 插入内容表
     * @param
     * @return com.owwang.mall.pojo.MallResult
     * @data 2019/12/19
     * @auther Samuel
     */
    MallResult saveContent(TbContent content);

    /**
     * @Description 根据CID查询内容信息
     * @param
     * @param Category_id
     * @return com.owwang.mall.pojo.TbContent
     * @Date 2019-12-21
     * @auther Samuel
     */
    List<TbContent> getContentByCid(Long Category_id);
}
