package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UserInfoVO {
    private int cnt;
    private int userId;
    private String pw;
    private String email;
    private String phone;
    private String userName;
    private String nickname;
    private String img;
    private String intro;
    private Date joinDate;
    private Date withdrawDate;
    private Date expireDate;
}
