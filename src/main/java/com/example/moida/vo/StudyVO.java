package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Clob;
import java.sql.Date;


@Getter
@Setter
public class StudyVO {
    private int studyId;
    private int studyMgrId;
    private String userImg;
    private String studyName;
    private String studyCategory;
    private int studyUserLimit;
    private int studyUserCount;
    private Date studyDeadline;
    private String studyChatUrl;
    private String studyIntro;
    private String studyContent;
    private String studyProfile;
    private String tagName;
    private String userName;
    private String userIntro;
    private int userId;
}
