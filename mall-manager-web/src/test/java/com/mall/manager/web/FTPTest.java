package com.mall.manager.web;

import com.jcraft.jsch.JSch;
import com.owwang.mall.pojo.Sftp;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/*public class FTPTest {

    //测试：上传文件
    @Test
    public void testFtpClient() throws Exception{
        Sftp sftp = new Sftp("122.51.105.58",22,1500,
                "ftpuser","12388888");
        sftp.login();
        //切换工作目录
        sftp.changeDir("/home/ftpuser/www/images//11");
        //File file = new File("D:\\Pictures\\test\\测试.jpg");
        //InputStream inputStream = new FileInputStream(file);
        //上传文件
        //sftp.uploadFile("/home/ftpuser/www/images","测试.jpg", inputStream);
        sftp.uploadFile("/home/ftpuser/www/images","3.jpg",
                "D:\\Pictures\\test\\测试.jpg");
        sftp.logout();
    }

    //测试：删除文件
    @Test
    public void testFtpClien2() throws Exception{
        Sftp sftp = new Sftp("122.51.105.58",22,1500,
                "ftpuser","12388888");
        sftp.login();
        //切换工作目录
        sftp.changeDir("/home/ftpuser/www/images");
        //sftp.delFile("1.png");
        //sftp.delFile("11.png");
        sftp.delFile("1.jsp");
        sftp.logout();
    }
}*/
