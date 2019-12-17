package com.owwang.mall.controller;

import com.owwang.mall.pojo.SFTPUploadResult;
import com.owwang.mall.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 上传图片处理
 */
@Controller
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public SFTPUploadResult pictureUpload(byte[] bytes, String name){
        InputStream inputStream = new ByteArrayInputStream(bytes);

        SFTPUploadResult result = pictureService.uploadPicture(inputStream,name);
        return result;
    }
}
