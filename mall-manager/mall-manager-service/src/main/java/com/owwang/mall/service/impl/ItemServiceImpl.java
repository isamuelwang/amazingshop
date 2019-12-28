package com.owwang.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.owwang.mall.mapper.TbItemDescMapper;
import com.owwang.mall.mapper.TbItemMapper;
import com.owwang.mall.mapper.TbItemParamItemMapper;
import com.owwang.mall.pojo.*;
import com.owwang.mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "topicDestination")
    private Destination destination;

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
     * 添加商品
     *
     * @param item
     * @param desc
     * @return
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

}
