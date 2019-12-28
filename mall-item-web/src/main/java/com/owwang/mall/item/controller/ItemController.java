package com.owwang.mall.item.controller;

import com.owwang.mall.item.pojo.ItemPojo;
import com.owwang.mall.pojo.TbItem;
import com.owwang.mall.pojo.TbItemDesc;
import com.owwang.mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname ItemController
 * @Description TODO
 * @Date 2019-12-29
 * @Created by WANG
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String getItem(@PathVariable Long itemId, Model model){
        //1.调用dao查询商品信息
        TbItem item = itemService.getItemById(itemId);
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        //2.把商品信息存入返回值POJO中
        ItemPojo itemPojo = new ItemPojo(item);
        model.addAttribute("item",itemPojo);
        model.addAttribute("itemDesc",itemDesc);
        //3.返回结果
        return "item";
    }
}
