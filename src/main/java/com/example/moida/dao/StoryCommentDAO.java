package com.example.moida.dao;

import com.example.moida.common.Common;
import com.example.moida.vo.CommentVO;
import com.example.moida.vo.StoryCommentVO;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoryCommentDAO {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rSet = null;

    public List<StoryCommentVO> getCommentsByStoryId(int storyId) {
        List<StoryCommentVO> list = new ArrayList<>();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT S.*, U.NICKNAME, U.IMG AS USER_IMG_URL FROM STORY_COMMENT S INNER JOIN USER_INFO U ");
            sql.append("ON S.USER_ID = U.USER_ID WHERE STORY_ID = ? ");
            sql.append("START WITH PARENT_COMMENT_ID IS NULL ");
            sql.append("CONNECT BY PRIOR STORY_COMMENT_ID = PARENT_COMMENT_ID ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, String.valueOf(storyId));
            rSet = pstmt.executeQuery();

            while (rSet.next()) {
                int commentId = rSet.getInt("STORY_COMMENT_ID");
                int userId = rSet.getInt("USER_ID");
                int parentId = rSet.getInt("PARENT_COMMENT_ID");
                String nickname = rSet.getString("NICKNAME"); // 닉네임은 POST테이블에 있진 않지만 기본적으로 조인해서 사용할 예정입니다
                String imgUrl = rSet.getString("USER_IMG_URL");
                String regTime = rSet.getString("REG_TIME");
                String contents = rSet.getString("CONTENTS");

                StoryCommentVO vo = new StoryCommentVO();
                vo.setCommentId(commentId);
                vo.setUserId(userId);
                vo.setParentId(parentId);
                vo.setStoryId(storyId);
                vo.setNickname(nickname);
                vo.setImgUrl(imgUrl);
                vo.setRegTime(regTime);
                vo.setContents(contents);

                list.add(vo);
            }
            Common.close(rSet);
            Common.close(pstmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // 포스트 댓글 기능
    public boolean storyCommentInsert(int userId, int storyId, String contents) {
        int result = 0;
        String sql = "INSERT INTO STORY_COMMENT(USER_ID, STORY_ID, CONTENTS) VALUES(?, ?, ?) ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, userId);
            pstmt.setInt(2, storyId);
            pstmt.setString(3, contents);

            result = pstmt.executeUpdate();
            System.out.println("post DB 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);

        if(result == 1) return true;
        else return false;
    }


    // 대댓글 INSERT 오버로딩
    public boolean storyCommentInsert(int userId, int storyId, int parentId, String contents) {
        int result = 0;
        String sql = "INSERT INTO STORY_COMMENT(USER_ID, STORY_ID, PARENT_COMMENT_ID, CONTENTS) VALUES(?, ?, ?, ?) ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, userId);
            pstmt.setInt(2, storyId);
            pstmt.setInt(3, parentId);
            pstmt.setString(4, contents);

            result = pstmt.executeUpdate();
            System.out.println("post DB 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);

        if(result == 1) return true;
        else return false;
    }

    // 댓글 UPDATE
    public boolean storyCommentUpdate(int commentId, String contents) {
        int result = 0;
        String sql = "UPDATE STORY_COMMENT SET CONTENTS = ? WHERE STORY_COMMENT_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, contents);
            pstmt.setInt(2, commentId);
            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);

        if(result == 1) return true;
        else return false;
    }


    // post 댓글 DELETE
    public boolean storyCommentDelete(int commentId) {
        int result = 0;
        String sql = "DELETE FROM STORY_COMMENT WHERE STORY_COMMENT_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, commentId);
            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);

        if(result == 1) return true;
        else return false;
    }
}
