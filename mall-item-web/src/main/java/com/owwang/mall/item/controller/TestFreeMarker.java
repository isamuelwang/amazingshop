package com.owwang.mall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname TestFreeMarker
 * @Description TODO
 * @Date 2019-12-30
 * @Created by WANG
 */
@Controller
public class TestFreeMarker {
    @Autowired
    private FreeMarkerConfig config;

    @RequestMapping("/testfreemarker")
    @ResponseBody
    public String testMQ() throws Exception{
        //创建configuration对象
        Configuration configuration = config.getConfiguration();
        //创建template对象
        Template template = configuration.getTemplate("test.ftl");
        //数据集
        Map map = new HashMap();
        map.put("testKey","test_success");
        //文件输出对象
        Writer writer = new FileWriter(new File("F:\\develop\\freemarker\\test.html"));
        //用template进行模板输出
        template.process(map,writer);
        //关闭资源
        writer.close();
        return "ok";
    }
}
