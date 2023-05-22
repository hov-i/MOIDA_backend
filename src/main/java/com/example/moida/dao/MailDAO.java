package com.example.moida.dao;

import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    // 아이디 확인
    public String getUsernameByPhone(String phone) {
        System.out.println("값" + phone);
        String sql = "SELECT USERNAME FROM USER_INFO WHERE PHONE = ?";
        return jdbcTemplate.queryForObject(sql, String.class, phone);
    }

    // 메일 내용 생성
    public String makeHtml(String type, String code, String phone) {
        String html = null;
        switch (type) {
            case "findPw":
                html = "<p>새로운 비밀번호는 <strong>" + code + "</strong>입니다.</p>";
                break;

            case "findId":
                html = "<p>가입하신 아이디는<strong>" + phone + "</strong>입니다.</p>";
                break;//이유는 모르겠는데 phone 값을 username으로 설정하니 오류 남 일단 phone은 username임
        }
        return html;
    }

    public boolean sendMail(String type, String email, String phone) {
        String code = null;
        String html = null;
        String subject = null;
        boolean result = false;

        switch(type) {
            case "findPw":
                code = makeCode(10);
                html = makeHtml(type, code, null);
                subject = "[MOIDA] 새로운 비밀번호가 도착했습니다.";
                break;

            case "findId":
                String username = getUsernameByPhone(phone);
                html = makeHtml(type, null, username);
                subject = "[MOIDA] 아이디 찾기 결과입니다.";
                break;
        }



        MimeMessage mail = mailSender.createMimeMessage();
        try {
            mail.setSubject(subject, "utf-8");
            mail.setText(html, "utf-8", "html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mailSender.send(mail);
            result = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 비밀번호 임시 비밀번호로 변경
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean changePasswordToTemporary(String username, String tempPw) {
        String sql = "UPDATE USER_INFO SET pw = ? WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(sql, tempPw, username);
        return rowsAffected > 0;
    }

    // phone, username, email 일치 확인
    public boolean checkPhoneUsernameAndEmailMatch(String username, String phone, String email) {
        String sql = "SELECT COUNT(*) FROM USER_INFO WHERE username = ? AND phone = ? AND email = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username, phone, email);
        return count > 0;
    }

    // phone, email 일치 확인
    public boolean checkEmailAndPhoneMatch(String email, String phone) {
        String sql = "SELECT COUNT(*) FROM USER_INFO WHERE email = ? AND phone = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, email, phone);
        return count > 0;
    }

}

