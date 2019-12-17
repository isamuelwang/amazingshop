package com.owwang.mall.service.impl;

import com.owwang.mall.pojo.IDUtils;
import com.owwang.mall.pojo.SFTPUploadResult;
import com.owwang.mall.pojo.Sftp;
import com.owwang.mall.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    //取SFTP连接信息
    @Value("${SFTP_ADDRESS}")
    private String SFTP_ADDRESS;
    @Value("${SFTP_PORT}")
    private int SFTP_PORT;
    @Value("${SFTP_TIMEOUT}")
    private int SFTP_TIMEOUT;
    @Value("${SFTP_USERNAME}")
    private String SFTP_USERNAME;
    @Value("${SFTP_PASSWORD}")
    private String SFTP_PASSWORD;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public SFTPUploadResult uploadPicture(MultipartFile uploadFile){
        SFTPUploadResult sftpUploadResult = new SFTPUploadResult();
        try {
            //生成新的文件名
            //取原始文件名
            String oldName = uploadFile.getOriginalFilename();
            //生成新文件名
            String newName = IDUtils.genImageName();
            //加后缀
            newName = newName + oldName.substring((oldName.lastIndexOf(".")));
            //图片上传
            Sftp sftp = new Sftp(SFTP_ADDRESS,SFTP_PORT,SFTP_TIMEOUT,SFTP_USERNAME,SFTP_PASSWORD);
            String  dataPath = new DateTime().toString("/yyyy/MM/dd");
            String pathName = "/home/ftpuser/www/images"+dataPath;
            sftp.login();
            boolean result = sftp.uploadFile(pathName,newName,uploadFile.getInputStream());
            sftp.logout();
            if(!result){
                sftpUploadResult.setError(1);
                sftpUploadResult.setMessage("文件上传失败");
                return sftpUploadResult;
            }
            sftpUploadResult.setError(0);
            String httpPath = IMAGE_BASE_URL + dataPath + "/" + newName;
            sftpUploadResult.setUrl(httpPath);
            return sftpUploadResult;
        } catch (IOException e) {
            e.printStackTrace();
            sftpUploadResult.setError(1);
            sftpUploadResult.setMessage("文件上传发生异常");
            return sftpUploadResult;
        }
    }
}
