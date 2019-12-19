package com.owwang.mall.content.service;

import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;

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
}
