package com.owwang.portal.controller;

import com.owwang.mall.content.service.ContentService;
import com.owwang.mall.pojo.JsonUtils;
import com.owwang.mall.pojo.TbContent;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.service.ItemService;
import com.owwang.portal.controller.com.owwang.mall.portal.pojp.AD1Pojo;
import com.owwang.portal.controller.com.owwang.mall.portal.pojp.RecommendItem1Pojo;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private ItemService itemService;

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
        //获得最新左右滑动9个商品的信息
        String ritems = showRecomandItem1(null, 9);
        //返回到首页
        model.addAttribute("DATA_MScroll",ritems);
        return "index";
    }

    /**
     * 上方9个左右滑动最新商品推荐
     * @Description TODO
     * @param
     * @return java.util.List<com.owwang.portal.controller.com.owwang.mall.portal.pojp.RecommendItem1Pojo>
     * @Date 2020-01-03
     * @auther Samuel
     */
    private String showRecomandItem1(Long cid,Integer quantity){
        //通过cid和数量n从数据库获得最新n个商品的信息
        List<TbItem> items = itemService.getItemListByCidAndQ(cid, quantity);
        //返回信息
        List<RecommendItem1Pojo> recommendItem1Pojos = new ArrayList<>();
        Integer index = 0;
        for(TbItem item : items){
            RecommendItem1Pojo ritem = new RecommendItem1Pojo();
            //循环遍历设置返回值
            //设置标题
            if(item.getTitle()!=null){
                ritem.setAlt(item.getTitle());
            }
            //page HTML  http://localhost:8087/item/1411736360.html
            String pageHtml = "http://localhost:8087/item/"+item.getId()+".html";
            ritem.setHref(pageHtml);
            ritem.setIndex(index);
            //图片地址
            String picsPath = item.getImage();
            if(StringUtils.isNotBlank(picsPath)){
                String[] pic = picsPath.split(",");
                ritem.setSrc(pic[0]);
            }else {
                ritem.setSrc("http://localhost:8087/images/blank.gif");
            }
            recommendItem1Pojos.add(ritem);
            index++;
        }
        //把推荐商品集合转为json格式
        String str = JsonUtils.objectToJson(recommendItem1Pojos);
        return str;
    }
}
