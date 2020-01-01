package com.owwang.mall.controller;

import com.owwang.mall.pojo.EasyUIDataGriResult;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItemParam;
import com.owwang.mall.service.ItemParamItemService;
import com.owwang.mall.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/item/param/list")
    @ResponseBody
    public EasyUIDataGriResult getItemParamList(@RequestParam(value = "page",defaultValue = "1") int page,
                                                @RequestParam(value = "rows")int rows){
        EasyUIDataGriResult result = itemParamService.getItemParamList(page, rows);
        return result;
    }

    @RequestMapping("/item/param/query/itemcatid/{itemCatId}")
    @ResponseBody
    public MallResult getItemParamByCid(@PathVariable Long itemCatId){
        MallResult result = itemParamService.getItemParamByCid(itemCatId);
        return result;
    }

    @RequestMapping("/item/param/save/{cid}")
    @ResponseBody
    public MallResult insertItemParam(@PathVariable Long cid,String paramData){
        TbItemParam itemParam = new TbItemParam();
        itemParam.setItemCatId(cid);
        itemParam.setParamData(paramData);
        MallResult result = itemParamService.insertItemParam(itemParam);
        return result;
    }
}
