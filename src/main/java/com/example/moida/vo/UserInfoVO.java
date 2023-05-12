package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UserInfoVO {
    private int cnt;
    private String userId;
    private String pw;
    private String newPw;
    private String email;
    private String newEmail;
    private String phone;
    private String newPhone;
    private String userName;
    private String pwConfirm;
    private Date joinDate;
    private Date withdrawDate;
    private Date expireDate;
}
