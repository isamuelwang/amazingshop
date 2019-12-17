package com.owwang.mall.service;

import com.owwang.mall.pojo.SFTPUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface PictureService {
    SFTPUploadResult uploadPicture(InputStream inputStream, String name);
}
