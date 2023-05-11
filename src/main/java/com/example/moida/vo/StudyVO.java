package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Clob;
import java.sql.Date;


@Getter
@Setter
public class StudyVO {
    private int study_id;
    private String study_mgr_id;
    private String study_name;
    private String study_category;
    private int study_user_limit;
    private int study_user_count;
    private Date study_deadline;
    private String study_chat_url;
    private String study_intro;
    private Clob study_content;
    private String study_profile;
    private String tag_name;
    private String user_name;
}
