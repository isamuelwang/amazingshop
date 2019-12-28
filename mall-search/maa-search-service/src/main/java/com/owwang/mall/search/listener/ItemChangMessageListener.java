package com.owwang.mall.search.listener;

import com.owwang.mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收MQ消息的监听器
 *
 * @Classname ItemChangMessageListener
 * @Description TODO
 * @Date 2019-12-29
 * @Created by WANG
 */
public class ItemChangMessageListener implements MessageListener {

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        //判断消息类型是否为Message
        try {
            if (message instanceof TextMessage) {
                //如果是，获取商品ID
                TextMessage textMessage = (TextMessage) message;
                Long itemId = Long.parseLong(textMessage.getText());
                //通过商品ID查询数据库，得到搜索时候的相关数据
                //更新索引库
                searchService.UpdateSearchItemById(itemId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
