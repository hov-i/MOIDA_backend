package com.example.moida.dao;

import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailDAO {
    @Autowired
    private JavaMailSender mailSender;
// 임시 비밀번호 생성
    public String makeCode(int size) {
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;
        do {
            num = ran.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }
        } while (sb.length() < size);
        return sb.toString();
    }

    // 메일 내용 생성
    public String makeHtml(String type, String code) {
        String html = null;
        switch (type) {
            case "findPw":
                html = "<p>새로운 비밀번호는 <strong>" + code + "</strong>입니다.</p>";
                break;
        }
        return html;
    }

    public String sendMail(String type, String email) {
        String code = null;
        String html = null;
        String subject = null;

        switch(type) {
            case "findPw":
                code = makeCode(10);
                html = makeHtml(type, code);
                subject = "[MOIDA]새로운 비밀번호가 도착했습니다.";
                break;
        }

        MimeMessage mail = mailSender.createMimeMessage();
        try {
            mail.setSubject(subject, "utf-8");
            mail.setText(html, "utf-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "error";
        }

        return code;
    }
}

