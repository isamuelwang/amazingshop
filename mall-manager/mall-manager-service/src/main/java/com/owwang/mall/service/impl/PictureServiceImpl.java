package com.owwang.mall.service.impl;

import com.owwang.mall.pojo.IDUtils;
import com.owwang.mall.pojo.SFTPUploadResult;
import com.owwang.mall.pojo.Sftp;
import com.owwang.mall.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public SFTPUploadResult uploadPicture(byte[] pic,String oldName){
        SFTPUploadResult sftpUploadResult = new SFTPUploadResult();
        if(pic==null||pic.length==0){
            sftpUploadResult.setError(1);
            sftpUploadResult.setMessage("空文件");
            return sftpUploadResult;
        }
        InputStream inputStream = new ByteArrayInputStream(pic);
        //生成新的文件名
        //生成新文件名
        String newName = IDUtils.genImageName();
        //加后缀
        newName = newName + oldName.substring((oldName.lastIndexOf(".")));
        //创建sftp工具类对象
        Sftp sftp = new Sftp(SFTP_ADDRESS,SFTP_PORT,SFTP_TIMEOUT,SFTP_USERNAME,SFTP_PASSWORD);
        sftp.login();
        String  dataPath = new DateTime().toString("/yyyy-MM-dd");
        String pathName = "/home/ftpuser/www/images"+dataPath;
        sftp.changeDir("/home/ftpuser/www/images");
        sftp.makeDir(dataPath.substring(1));
        //执行文件上传！
        boolean result = sftp.uploadFile(pathName,newName,inputStream);
        sftp.logout();
        //上传失败时，回传信息
        if(!result){
            sftpUploadResult.setError(1);
            sftpUploadResult.setMessage("文件上传失败");
            return sftpUploadResult;
        }
        String httpPath = IMAGE_BASE_URL + dataPath + "/" + newName;
        sftpUploadResult.setUrl(httpPath);
        sftpUploadResult.setError(0);
        return sftpUploadResult;
    }
}
