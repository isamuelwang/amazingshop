package com.owwang.mall.item.listener;

import com.owwang.mall.item.pojo.ItemPojo;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbItemDesc;
import com.owwang.mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ItemChangeGenHTMLMessageListener
 * @Description TODO
 * @Date 2019-12-30
 * @Created by WANG
 */
public class ItemChangeGenHTMLMessageListener implements MessageListener {
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfig config;
    @Override
    public void onMessage(Message message) {
        //接收消息
        if (message instanceof TextMessage) {
            TextMessage message1 = (TextMessage) message;
            String text = null;
            try {
                text = message1.getText();
                if (StringUtils.isNotBlank(text)) {
                    Long itemId = Long.valueOf(text);
                    //根据商品ID查询得到商品信息
                    TbItem item = itemService.getItemById(itemId);
                    TbItemDesc itemDesc = itemService.getItemDescById(itemId);
                    //调用方法生成静态页面
                    getHtml(item,itemDesc,"item.ftl");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //生成静态页面的private方法
    private void getHtml(TbItem item,TbItemDesc desc,String templateName) throws Exception{
        //生产HTML的路径
        String path = "F:\\develop\\freemarker\\item\\"+item.getId()+".html";
        //生产configuration对象
        Configuration configuration = config.getConfiguration();
        //生成template
        Template template = configuration.getTemplate(templateName);
        //数据集
        Map map = new HashMap();
        map.put("item",new ItemPojo(item));
        map.put("itemDesc",desc);
        //创建输出流
        Writer writer = new FileWriter(new File(path));
        //用模板输出html
        template.process(map,writer);
        writer.close();
    }
}
