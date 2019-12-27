package com.owwang.mall.utils;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @Classname sendMail
 * @Description TODO
 * @Date 2019-12-27
 * @Created by WANG
 */
public class SendMailUtil {
    /**
     * 邮件发送的方法
     *
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param smtp 协议
     * @param host 发送服务器服务器
     * @param sendName 邮件发送人
     * @param sendPort 邮件发送人端口
     * @param userName 邮件发送人名
     * @param userPwd 邮件发送人密码
     * @return 成功或失败
     */
    public static boolean send(String to, String subject, String content, String smtp, String host,
                               String sendName, String sendPort, String userName, String userPwd) {

        // 第一步：创建Session
        Properties props = new Properties();
        // 指定邮件的传输协议，smtp(Simple Mail Transfer Protocol 简单的邮件传输协议)
        props.put("mail.transport.protocol", smtp);
        // 指定邮件发送服务器服务器 "smtp.qq.com"
        props.put("mail.host", host);
        // 指定邮件的发送人(您用来发送邮件的服务器，比如您的163\sina等邮箱)
        props.put("mail.from", sendName);
        if (true) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", sendPort);
        }
        Session session = Session.getDefaultInstance(props);

        // 开启调试模式
        session.setDebug(true);
        try {
            // 第二步：获取邮件发送对象
            Transport transport = session.getTransport();
            // 连接邮件服务器，链接您的163、sina邮箱，用户名（不带@163.com，登录邮箱的邮箱账号，不是邮箱地址）、密码
            transport.connect(userName, userPwd);
            Address toAddress = new  InternetAddress(to);

            // 第三步：创建邮件消息体
            MimeMessage message = new MimeMessage(session);
            //设置自定义发件人昵称
            String nick="";
            try {
                nick=javax.mail.internet.MimeUtility.encodeText("我的昵称");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setFrom(new InternetAddress(nick+" <"+sendName+">"));
            //设置发信人
            // message.setFrom(new InternetAddress(sendName));

            // 邮件的主题
            message.setSubject(subject);
            //收件人
            message.addRecipient(Message.RecipientType.TO, toAddress);
            /*//抄送人
            Address ccAddress = new InternetAddress("first.lady@whitehouse.gov");
            message.addRecipient(Message.RecipientType.CC, ccAddress);*/
            // 邮件的内容
            message.setContent(content, "text/html;charset=utf-8");
            // 邮件发送时间
            message.setSentDate(new Date());

            // 第四步：发送邮件
            // 第一个参数：邮件的消息体
            // 第二个参数：邮件的接收人，多个接收人用逗号隔开（test1@163.com,test2@sina.com）
            transport.sendMessage(message, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // 您要发送给谁，标题，内容
        SendMailUtil.send("*****@qq.com", "标题", "内容", "smtp", "smtp.qq.com", "发送人名", "端口号", "用户名", "密码");
    }
}
