package com.owwang.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.owwang.mall.mapper.TbItemDescMapper;
import com.owwang.mall.mapper.TbItemMapper;
import com.owwang.mall.mapper.TbItemParamItemMapper;
import com.owwang.mall.pojo.*;
import com.owwang.mall.service.ItemService;
import com.owwang.mall.service.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    //Redis客户端
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "topicDestination")
    private Destination destination;
    @Value("${ITEM_INFO_KEY}")
    private String ITEM_INFO_KEY;
    @Value("${ITEM_INFO_EXPIRE}")
    private int ITEM_INFO_EXPIRE;

    /**
     * 通过商品ID查询商品详细信息
     * @Description TODO
     * @param
     * @param itemId 商品ID
     * @return com.owwang.mall.pojo.TbItemDesc
     * @Date 2019-12-29
     * @auther Samuel
     */
    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        try {
            String jsonStr = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":DESC");
            //判断Redis中是否有缓存
            if (StringUtils.isNotBlank(jsonStr)) {
                //设置有效期
                jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
                //Redis中取数据
                return JsonUtils.jsonToPojo(jsonStr, TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据库中取数据
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        //注入缓存
        try {
            jedisClient.set(ITEM_INFO_KEY + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
            jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemDesc;
    }

    /**
     * 通过商品ID查询商品信息
     * @param itemId 商品ID
     * @return com.owwang.mall.pojo.TbItem
     * @Description TODO
     * @Date 2019-12-29
     * @auther Samuel
     */
    @Override
    public TbItem getItemById(Long itemId) {
        try {
            String jsonStr = jedisClient.get(ITEM_INFO_KEY + ":" + itemId + ":BASE");
            //判断Redis中是否有缓存
            if (StringUtils.isNotBlank(jsonStr)) {
                //设置有效期
                jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
                //Redis中取数据
                return JsonUtils.jsonToPojo(jsonStr, TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //数据库中取数据
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        //注入缓存
        try {
            jedisClient.set(ITEM_INFO_KEY + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
            jedisClient.expire(ITEM_INFO_KEY + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public EasyUIDataGriResult getItemList(Integer page, Integer rows) {
        //1.设置分页的信息
        if (page == null) page = 1;
        if (rows == null) rows = 30;
        PageHelper.startPage(page, rows);
        //2.注入mapper
        //3.查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = tbItemMapper.selectByExample(example);
        //4.取值
        PageInfo<TbItem> info = new PageInfo<TbItem>(list);
        EasyUIDataGriResult result = new EasyUIDataGriResult();
        result.setTotal((int) info.getTotal());
        result.setRows(info.getList());
        //5.传值
        return result;
    }

    /**
     * 添加商品到数据库中
     *
     * @param item
     * @param desc
     * @throws Exception
     */
    private Long createItemToSQL(TbItem item, String desc, String itemParams) throws Exception {
        Long itemID = IDUtils.genItemId();
        item.setId(itemID);
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        tbItemMapper.insert(item);
        //添加商品描述信息
        MallResult descResult = insertItemDesc(itemID, desc);
        //上传不成功则抛异常
        if (descResult.getStatus() != 200) {
            throw new Exception("商品描述信息上传异常");
        }
        //添加规格参数
        MallResult result = insertParamItem(itemID, itemParams);
        if (result.getStatus() != 200) {
            throw new Exception("商品规格信息上传异常");
        }
        return itemID;
    }

    /**
     * 插入商品信息到数据库，并发送MQ消息
     *
     * @param item      商品类
     * @param desc      商品描述
     * @param itemParam
     * @return com.owwang.mall.pojo.MallResult
     * @Description TODO
     * @Date 2019-12-29
     * @auther Samuel
     */
    @Override
    public MallResult createItem(TbItem item, String desc, String itemParam) throws Exception {
        //添加商品信息到数据库
        final Long itemID = createItemToSQL(item, desc, itemParam);
        //添加activeMQ发送消息的业务逻辑
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //发送消息
                return session.createTextMessage(itemID + "");
            }
        });
        //返回结果
        return MallResult.ok();
    }


    /**
     * 添加商品描述
     *
     * @param desc
     */
    private MallResult insertItemDesc(Long itemID, String desc) {
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemID);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insert(itemDesc);
        return MallResult.ok();
    }

    /**
     * 插入商品规格信息
     *
     * @param itemId
     * @param itemParam
     * @return
     */
    private MallResult insertParamItem(Long itemId, String itemParam) {
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParam);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
        //向表中插入数据
        itemParamItemMapper.insert(itemParamItem);
        return MallResult.ok();
    }

    public List<TbItem> getItemListByCidAndQ(Long cid,Integer quantity){
        //判断redis中是否有数据
        //如果有直接返回Redis中的值
        try {
            String PORTAL_LATEST9ITEMS = jedisClient.get("PORTAL:LATEST9ITEMS");
            if(StringUtils.isNotBlank(PORTAL_LATEST9ITEMS)){
                List<TbItem> items = JsonUtils.jsonToList(PORTAL_LATEST9ITEMS, TbItem.class);
                return items;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置查询条件map
        Map map = new HashMap<>();
        map.put("cid",cid);
        map.put("quantity",quantity);
        List<TbItem> items = tbItemMapper.getItemListByCidAndQ(map);
        //添加数据到redis中
        try {
            jedisClient.set("PORTAL:LATEST9ITEMS",JsonUtils.objectToJson(items));
            jedisClient.expire("PORTAL:LATEST9ITEMS",1800);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
