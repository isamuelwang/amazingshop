package com.owwang.mall.controller;

import com.owwang.mall.content.service.ContentService;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Classname ContentController
 * @Description 内容处理器
 * @Date 2019/12/19
 * @Created by Samuel
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "/content/save", method = RequestMethod.POST)
    @ResponseBody
    public MallResult savenContent(TbContent tbContent) {
        MallResult result = contentService.saveContent(tbContent);
        return result;
    }
}
