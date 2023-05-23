package com.example.moida.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryCommentVO {
    private int commentId;
    private int userId;
    private int storyId;
    private int parentId;
    private String nickname;
    private String imgUrl;
    private String regTime;
    private String contents;
}
