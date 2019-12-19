package com.owwang.mall.content.service.impl;

import com.owwang.mall.content.service.ContentService;
import com.owwang.mall.mapper.TbContentMapper;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Classname ContentService
 * @Description TODO
 * @Date 2019/12/19
 * @Created by Samuel
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper mapper;

    /**
     * @Description 保存内容
     * @param content
     * @return com.owwang.mall.pojo.MallResult
     * @data 2019/12/19
     * @auther Samuel
     */
    @Override
    public MallResult saveContent(TbContent content) {
        //补全属性
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        mapper.insertSelective(content);
        return MallResult.ok();
    }
}
