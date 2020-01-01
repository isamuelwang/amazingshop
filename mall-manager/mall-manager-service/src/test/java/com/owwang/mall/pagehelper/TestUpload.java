package com.owwang.mall.pagehelper;

import com.owwang.mall.pojo.IDUtils;
import com.owwang.mall.pojo.SFTPUploadResult;
import com.owwang.mall.pojo.Sftp;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.*;

public class TestUpload {


    @Test
    public void testUpload() throws FileNotFoundException {
        String SFTP_ADDRESS="122.51.105.58";
        int SFTP_PORT=22;
        int SFTP_TIMEOUT=15000;
        String SFTP_USERNAME="ftpuser";
        String SFTP_PASSWORD="12388888";
        String IMAGE_BASE_URL="http://122.51.105.58/images";
        //文件源
        File file = new File("D:\\Pictures\\test\\1.png");
        InputStream inputStream = new FileInputStream(file);
        //生成新的文件名
        //生成新文件名
        String oldName = file.getName();
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
        System.out.println(pathName);
        //执行文件上传！
        boolean result = sftp.uploadFile(pathName,newName,inputStream);
        sftp.logout();
        if(!result){
            System.out.println("上传失败");
            return;
        }
        String httpPath = IMAGE_BASE_URL + dataPath + "/" + newName;
        System.out.println("上传成功");
        System.out.println(httpPath);
        System.out.println("===========================");
    }
}
