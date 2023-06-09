package com.example.moida.vo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostVO {
    private int postId;
    private int userId;
    private String nickname; // 닉네임은 POST테이블에 있진 않지만 기본적으로 조인해서 사용할 예정입니다
    private String userImgUrl;
    private String regTime;
    private String title;
    private String contents;
    private int views;
    private int recommend;
    private String boardName;
    private String imgUrl;
    private List<CommentVO> comments;
}



