package com.example.moida.vo;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Clob;
import java.sql.Date;

@Getter
@Setter

public class StoryVO {

    private String story_img;

    private String study_name;

    private int story_id;

    private int user_id;

    private String story_name;

    private String story_content;

    private String study_id;

    private Date story_date;

    private int story_like;
}
