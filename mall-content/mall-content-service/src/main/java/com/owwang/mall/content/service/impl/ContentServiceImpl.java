package com.owwang.mall.content.service.impl;

import com.owwang.mall.content.service.ContentService;
import com.owwang.mall.mapper.TbContentMapper;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;
import com.owwang.mall.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    /**
     *根据CID查询内容信息
     * @Description TODO
     * @param
     * @param Category_id
     * @return com.owwang.mall.pojo.TbContent
     * @Date 2019-12-21
     * @auther Samuel
     */
    public List<TbContent> getContentByCid(Long Category_id){
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(Category_id);
        List<TbContent> contents = mapper.selectByExampleWithBLOBs(example);
        return contents;
    }
}
