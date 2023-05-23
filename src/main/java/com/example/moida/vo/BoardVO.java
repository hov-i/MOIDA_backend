package com.example.moida.vo;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class BoardVO {
    private int studyId;
    private int userId;
    private String boardName;
    private String boardContent;
    private Date boardDate;
}
