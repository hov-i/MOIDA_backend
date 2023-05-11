package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class User_infoVO {
    private int cnt;
    private String user_id;
    private String pw;
    private String newPw;
    private String email;
    private String newEmail;
    private String phone;
    private String newPhone;
    private String user_name;
    private String pw_confirm;
    private Date joinDate;
    private Date withdrawDate;
    private Date expireDate;
}
