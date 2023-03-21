package com.hrhrng.drugforecast.common.stub;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailHelper {

    public void sendEmail(String to, String subject, String text) throws MessagingException {

        // 创建一个Properties对象，用于设置SMTP服务器的配置信息
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        // 创建一个Session对象，用于与SMTP服务器进行通信
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 设置SMTP服务器的用户名和密码
                return new PasswordAuthentication("test@qq.com", "test");
            }
        });

        // 创建一个MimeMessage对象，用于设置邮件的内容
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("test@qq.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);

        // 发送邮件
        Transport.send(message);
    }


}
