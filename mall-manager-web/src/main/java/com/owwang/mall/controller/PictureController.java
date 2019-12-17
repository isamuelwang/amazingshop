package com.owwang.mall.controller;

import com.owwang.mall.pojo.SFTPUploadResult;
import com.owwang.mall.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public SFTPUploadResult pictureUpload(@RequestParam MultipartFile uploadFile)  {
        byte[] pic = new byte[0];
        SFTPUploadResult sftpUploadResult = new SFTPUploadResult();
        try {
            pic = uploadFile.getBytes();
            String oldName = uploadFile.getOriginalFilename();
            sftpUploadResult = pictureService.uploadPicture(pic,oldName);
            return sftpUploadResult;
        } catch (IOException e) {
            e.printStackTrace();
            sftpUploadResult.setMessage("上传图片异常");
            sftpUploadResult.setError(1);
            return sftpUploadResult;
        }
    }
}
