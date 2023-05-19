package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class ScheduleVO {
    private int studyId;
    private int studyScId;
    private Date studyScDate;
    private String studyScContent;
    private int studyScUserLimit;
    private int studyScUserCount;

}
