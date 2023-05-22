package com.example.moida.dao;

import com.example.moida.common.Common;
import com.example.moida.vo.CommentVO;
import com.example.moida.vo.StoryVO;
import com.example.moida.vo.StudyVO;

import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StoryDAO {
    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;
    private Statement stmt = null;


    // 스토리 메인 - 전체 리스트
    public List<StoryVO> StoryVOList() {
        List<StoryVO> list = new ArrayList<>();
        try {

            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ST.STORY_IMG, ST.STORY_NAME, SI.STUDY_NAME ");
            sql.append("FROM STORY ST");
            sql.append("JOIN STUDY_INFO SI ON ST.STUDY_ID = SI.STUDY_ID");

            rs = pStmt.executeQuery();

            while (rs.next()) {
                int storyId = rs.getInt("STORY_ID");
                String imgUrl = rs.getString("IMG_URL");
                String title  = rs.getString("TITLE");
                String studyName = rs.getString("STUDY_NAME");

                StoryVO vo = new StoryVO();

                vo.setStoryId(storyId);
                vo.setImgUrl(imgUrl);
                vo.setTitle(title);
                vo.setStudyName(studyName);

                list.add(vo);
            }
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } return list;
    }


    // 스토리 포스트 {storyId} // 조회수
    public StoryVO getStoryById(int storyId) {
        StoryVO vo = new StoryVO();

        // 포스트 조회 쿼리문
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.*, U.IMG AS USER_IMG_URL, U.NICKNAME, SI.STUDY_PROFILE, SI.STUDY_NAME, SI.STUDY_INTRO, T.TAG_NAME");
        sql.append("FROM STORY S");
        sql.append("INNER JOIN USER_INFO U ON S.USER_ID = U.USER_ID ");
        sql.append("JOIN STUDY_INFO SI ON S.STUDY_ID = SI.STUDY_ID");
        sql.append("JOIN STUDY_TAG_REL STR ON SI.STUDY_ID = STR.STUDY_ID");
        sql.append("JOIN TAGS T ON STR.TAG_ID = T.TAG_ID ");
        sql.append("WHERE STORY_ID = ? ");

        // 조회 수 증가 쿼리문
//        String sql1 = "UPDATE STORY SET VIEWS = VIEWS + 1 WHERE STORY_ID = ? ";

        try {
            conn = Common.getConnection();

            pStmt = conn.prepareStatement(sql.toString());
            pStmt.setInt(1, storyId);
            rs = pStmt.executeQuery();

            int num = 0;
            while (rs.next()) {
                if (num == 1) vo.setStudyTag(vo.getStudyTag() + " #" + rs.getString("TAG_NAME"));

                else {
                    vo.setStoryId(storyId);
                    vo.setTitle(rs.getString("TITLE"));
                    vo.setUserId(rs.getInt("USER_ID"));
                    vo.setUserImgUrl(rs.getString("USER_IMG_URL"));
                    vo.setNickname(rs.getString("NICKNAME"));
                    vo.setStoryLike(rs.getInt("RECOMMEND"));
                    vo.setRegTime(rs.getString("REG_TIME"));

                    // Study Info
                    vo.setStudyId(rs.getInt("STUDY_ID"));
                    vo.setStudyProfile(rs.getString("STUDY_PROFILE"));
                    vo.setStudyName(rs.getString("STUDY_NAME"));
                    vo.setStudyIntro(rs.getString("STUDY_INTRO"));
                    vo.setStudyTag("#" + rs.getString("TAG_NAME"));

                    vo.setImgUrl(rs.getString("IMG_URL"));
                    vo.setContents(rs.getString("CONTENTS"));
                }
            }
            num = 1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }


    // 스토리 등록
    public boolean storyInsert(int userId, int studyId, String title, String imgUrl, String contents) {
        int result = 0;
        String sql = "INSERT INTO STORY(USER_ID, STUDY_ID, TITLE, IMG_URL, CONTENTS) VALUES(?, ?, ?, ?, ?) ";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);

            pStmt.setInt(1, userId);
            pStmt.setInt(2, studyId);
            pStmt.setString(3, title);
            pStmt.setString(4, imgUrl);
            pStmt.setString(5, contents);

            result = pStmt.executeUpdate();
            System.out.println("story DB 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);

        if (result == 1) return true;
        else return false;
    }


    // 스토리 수정
    public boolean storyUpdate(String title, String contents, int storyId) {
        int result = 0;
        String sql = "UPDATE STORY SET TITLE = ? CONTENTS= ? WHERE STORY_ID = ? ";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);

            pStmt.setString(1, title);
            pStmt.setString(3, contents);
            pStmt.setInt(3, storyId);

            result = pStmt.executeUpdate();
            System.out.println("story DB 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pStmt);
        Common.close(conn);

        if (result == 1) return true;
        else return false;
    }


    // 스토리 삭제
    public boolean storyDelete(int storyId) {
        int result = 0;
        String sql = "DELETE FROM STORY WHERE STORY_ID = " + storyId;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();

            result = stmt.executeUpdate(sql);
            System.out.println("story DB 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(stmt);
        Common.close(conn);

        if (result == 1) return true;
        else return false;
    }

    
    // 스토리 검색 - 필터 리스트



}

