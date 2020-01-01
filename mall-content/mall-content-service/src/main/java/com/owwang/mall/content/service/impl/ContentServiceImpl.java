package com.owwang.mall.content.service.impl;

import com.owwang.mall.content.jedis.JedisClient;
import com.owwang.mall.content.service.ContentService;
import com.owwang.mall.mapper.TbContentMapper;
import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;
import com.owwang.mall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

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
        try {
            //添加内容后，清空Redis缓存
            jedisClient.hdel(CONTENT_KEY,content.getCategoryId()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        //判断redis中是否有数据
        //如果有直接返回Redis中的值
        try {
            String jsonstr = jedisClient.hget(CONTENT_KEY, Category_id + "");
            if(StringUtils.isNotBlank(jsonstr)){
                return JsonUtils.jsonToList(jsonstr,TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果没有，查SQL数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(Category_id);
        List<TbContent> contents = mapper.selectByExampleWithBLOBs(example);
        //添加数据到redis中
        try {
            jedisClient.hset(CONTENT_KEY,Category_id+"", JsonUtils.objectToJson(contents));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }
}
