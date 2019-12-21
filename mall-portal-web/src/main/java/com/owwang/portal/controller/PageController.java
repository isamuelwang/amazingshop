package com.owwang.portal.controller;

import com.owwang.mall.content.service.ContentService;
import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.TbContent;
import com.owwang.portal.controller.com.owwang.mall.portal.pojp.AD1Pojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页管理
 *
 * @author Samuel
 * @data 2019年12月13日
 */
@Controller
public class PageController {

    @Autowired
    private ContentService contentService;

    @Value("${AD1_Category_ID}")
    private Long AD1_Category_ID;
    @Value("${AD1_Height}")
    private String AD1_Height;
    @Value("${AD1_HeightB}")
    private String AD1_HeightB;
    @Value("${AD1_Width}")
    private String AD1_Width;
    @Value("${AD1_WidthB}")
    private String AD1_WidthB;


    @RequestMapping("/index")
    public String showIndex(Model model) {
		AD1Pojo ad1pojo = new AD1Pojo();
        //查询数据库中内容信息
		List<TbContent> contents = contentService.getContentByCid(AD1_Category_ID);
        //返回值集合
        List<AD1Pojo> lists = new ArrayList<>();
        //对返回值赋值
        for(TbContent content : contents){
            //新建返回值POJO
            AD1Pojo ad1Pojo = new AD1Pojo();
            ad1Pojo.setAlt(content.getSubTitle());
            ad1Pojo.setHeight(AD1_Height);
            ad1Pojo.setHeightB(AD1_HeightB);
            ad1Pojo.setHref(content.getUrl());
            ad1Pojo.setSrc(content.getPic());
            ad1Pojo.setSrcB(content.getPic2());
            ad1Pojo.setWidth(AD1_Width);
            ad1Pojo.setWidth(AD1_WidthB);
            lists.add(ad1Pojo);
        }
        model.addAttribute("ad1", JsonUtils.objectToJson(lists));
        return "index";
    }
}
