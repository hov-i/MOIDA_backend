package com.example.moida.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class StoryVO {
    private int storyId;
    private int userId;
    private String category;
    private String imgUrl;
    private String studyName; // join 으로 사용할 예정
    private String title;

    private String userImgUrl; // join 으로 사용할 예정
    private String nickname; // join 으로 사용할 예정
    private int storyLike;
    private String regTime;
    private int studyId; // join 으로 사용할 예정
    private String studyProfile; // join 으로 사용할 예정
    private String studyIntro; // join 으로 사용할 예정
    private String studyTag; // join 으로 사용할 예정
    private String contents;
    private List<StoryCommentVO> comments; // join 으로 사용할 예정


    // Story 작성 insert VO
//    public StoryVO(int studyId, int userId, String title, String contents, String imgUrl) {
//
//        this.studyId = studyId;
//        this.userId = userId;
//        this.title = title;
//        this.contents = contents;
//        this.imgUrl = imgUrl;
//    }

}







