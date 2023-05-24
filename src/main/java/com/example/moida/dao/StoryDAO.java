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


    // 스토리 메인  - 전체 리스트 초기 게시물 132개
    // 3개씩 4줄보여지는 것  어떤 정보를 필요한가? 스토리 아이디, 스토리 제목, 스터디 이름, 스토리 사진, 그 스터디의 카테고리
    public List<StoryVO> StoryVOList() {
        List<StoryVO> list = new ArrayList<>();
        String sql =    "SELECT * " +
                "FROM ( SELECT T.*, ROWNUM AS RN " +
                "FROM ( " +
                "SELECT S.STORY_ID, S.TITLE, SI.STUDY_NAME, S.IMG_URL, SI.STUDY_CATEGORY " +
                "FROM STORY S INNER JOIN STUDY_INFO SI " +
                "ON S.STUDY_ID = SI.STUDY_ID " +
                "ORDER BY STORY_ID DESC " +
                ") T )" +
                "WHERE RN BETWEEN 0 AND 132";

        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int storyId = rs.getInt("STORY_ID");
                String title = rs.getString("TITLE");
                String imgUrl = rs.getString("IMG_URL");
                String studyName = rs.getString("STUDY_NAME");
                String category = rs.getString("STUDY_CATEGORY");

                StoryVO vo = new StoryVO();

                vo.setCategory(category);
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

    // LastID 이전 게시물 120개
    public List<StoryVO> StoryVOList(int lastId) {
        List<StoryVO> list = new ArrayList<>();
        String sql =    "SELECT * " +
                "FROM ( SELECT T.*, ROWNUM AS RN " +
                "FROM ( " +
                "SELECT S.STORY_ID, S.TITLE, SI.STUDY_NAME, S.IMG_URL, SI.STUDY_CATEGORY " +
                "FROM STORY S INNER JOIN STUDY_INFO SI " +
                "ON S.STUDY_ID = SI.STUDY_ID " +
                "WHERE STORY_ID < ? " +
                "ORDER BY STORY_ID DESC " +
                ") T )" +
                "WHERE RN BETWEEN 0 AND 120";
        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, lastId);
            rs = pStmt.executeQuery();

            while (rs.next()) {
                int storyId = rs.getInt("STORY_ID");
                String imgUrl = rs.getString("IMG_URL");
                String title = rs.getString("TITLE");
                String studyName = rs.getString("STUDY_NAME");
                String category = rs.getString("STUDY_CATEGORY");

                StoryVO vo = new StoryVO();

                vo.setStoryId(storyId);
                vo.setImgUrl(imgUrl);
                vo.setTitle(title);
                vo.setStudyName(studyName);
                vo.setCategory(category);

                list.add(vo);
            }
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("lastId = " + lastId);
        return list;
    }
    // 스토리 메인  - 전체 리스트 초기 게시물 132개
//    public List<StoryVO> StoryVOList() {
//        List<StoryVO> list = new ArrayList<>();
//        try {
//                conn = Common.getConnection();
//                stmt = conn.createStatement();
//
//            String sql = "SELECT S.STORY_ID, S.IMG_URL, SI.STUDY_CATEGORY, S.TITLE, SI.STUDY_NAME " +
//                         "FROM STORY S " +
//                         "JOIN STUDY_INFO SI ON S.STUDY_ID = SI.STUDY_ID " +
//                         "ORDER BY STORY_ID DESC";
//
//            rs = stmt.executeQuery(sql);
//
//            while (rs.next()) {
//                int storyId = rs.getInt("STORY_ID");
//                String category = rs.getString("STUDY_CATEGORY");
//                String imgUrl = rs.getString("IMG_URL");
//                String title  = rs.getString("TITLE");
//                String studyName = rs.getString("STUDY_NAME");
//
//                StoryVO vo = new StoryVO();
//
//                vo.setCategory(category);
//                vo.setStoryId(storyId);
//                vo.setImgUrl(imgUrl);
//                vo.setTitle(title);
//                vo.setStudyName(studyName);
//
//                list.add(vo);
//            }
//            Common.close(rs);
//            Common.close(pStmt);
//            Common.close(conn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } return list;
//    }

    // LastID 이전 게시물 120개
//    public List<StoryVO> StoryVOList(int lastId) {
//        List<StoryVO> list = new ArrayList<>();
//
//        try {
//            conn = Common.getConnection();
//            StringBuilder sql = new StringBuilder();
//
//            sql.append("SELECT * FROM ( SELECT T.*, ROWNUM AS RN FROM ( ");
//            sql.append("SELECT S.*, S.IMG_URL, S.TITLE, SI.STUDY_NAME, SI.STUDY_CATEGORY ");
//            sql.append("FROM STORY S");
//            sql.append("JOIN STUDY_INFO SI ON S.STUDY_ID = SI.STUDY_ID ");
//            sql.append("WHERE STORY_ID < ? ");
//            sql.append("ORDER BY STORY_ID DESC) T ) WHERE RN BETWEEN 0 AND 120 ");
//
//
//            pStmt = conn.prepareStatement(sql.toString());
//            pStmt.setInt(1, lastId);
//            rs = pStmt.executeQuery();
//
//            while (rs.next()) {
//                int storyId = rs.getInt("STORY_ID");
//                String imgUrl = rs.getString("IMG_URL");
//                String title  = rs.getString("TITLE");
//                String studyName = rs.getString("STUDY_NAME");
//
//                StoryVO vo = new StoryVO();
//
//                vo.setStoryId(storyId);
//                vo.setImgUrl(imgUrl);
//                vo.setTitle(title);
//                vo.setStudyName(studyName);
//
//                list.add(vo);
//            }
//            Common.close(rs);
//            Common.close(pStmt);
//            Common.close(conn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("lastId = " + lastId);
//        return list;
//    }



//    // 스토리 포스트 {storyId} (조회수)
//    public StoryVO getStoryById(int storyId) {
//
//        StoryVO vo = new StoryVO();
//
//
//        try {
//            conn = Common.getConnection();
//
//            // 포스트 조회 쿼리문
//            StringBuilder sql = new StringBuilder();
//
//            sql.append("SELECT S.*, U.IMG AS USER_IMG_URL, U.NICKNAME, SI.STUDY_PROFILE, SI.STUDY_NAME, SI.STUDY_INTRO");
//            sql.append("FROM STORY S");
//            sql.append("INNER JOIN USER_INFO U ON S.USER_ID = U.USER_ID ");
//            sql.append("JOIN STUDY_INFO SI ON S.STUDY_ID = SI.STUDY_ID");
//            sql.append("WHERE STORY_ID = ? ");
//
//            pStmt = conn.prepareStatement(sql.toString());
//            pStmt.setInt(1, storyId);
//            rs = pStmt.executeQuery();
//
//            if  (rs.next()) {
//                    vo.setStoryId(storyId);
//                    vo.setTitle(rs.getString("TITLE"));
//                    vo.setUserId(rs.getInt("USER_ID"));
//                    vo.setUserImgUrl(rs.getString("USER_IMG_URL"));
//                    vo.setNickname(rs.getString("NICKNAME"));
////                    vo.setStoryLike(rs.getInt("RECOMMEND"));
//                    vo.setRegTime(rs.getString("REG_TIME"));
//
//                    // Study Info
//                    vo.setStudyId(rs.getInt("STUDY_ID"));
//                    vo.setStudyProfile(rs.getString("STUDY_PROFILE"));
//                    vo.setStudyName(rs.getString("STUDY_NAME"));
//                    vo.setStudyIntro(rs.getString("STUDY_INTRO"));
//
//                    vo.setImgUrl(rs.getString("IMG_URL"));
//                    vo.setContents(rs.getString("CONTENTS"));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Common.close(rs);
//        Common.close(pStmt);
//        Common.close(conn);
//
//        return vo;
//    }


    // 스토리 조회
    // 스토리에 보여야 하는것들
    // 스토리 제목, 내용, 이미지, 스터디이름, 스터디 태그, 작성자 이름
    public StoryVO getStoryById(int storyId) {
        StoryVO vo = new StoryVO();
        String sql = "SELECT S.*, U.IMG AS USER_IMG_URL, U.NICKNAME, SI.STUDY_PROFILE, SI.STUDY_NAME, SI.STUDY_INTRO " +
                "FROM STORY S " +
                "INNER JOIN USER_INFO U ON S.USER_ID = U.USER_ID " +
                "JOIN STUDY_INFO SI ON S.STUDY_ID = SI.STUDY_ID " +
                "WHERE STORY_ID = " + storyId;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                vo.setStoryId(storyId);
                vo.setTitle(rs.getString("TITLE"));
                vo.setUserId(rs.getInt("USER_ID"));
                vo.setUserImgUrl(rs.getString("USER_IMG_URL"));
                vo.setNickname(rs.getString("NICKNAME"));
//                    vo.setStoryLike(rs.getInt("RECOMMEND"));
                vo.setRegTime(rs.getString("REG_TIME"));

                // Study Info
                vo.setStudyId(rs.getInt("STUDY_ID"));
                vo.setStudyProfile(rs.getString("STUDY_PROFILE"));
                vo.setStudyName(rs.getString("STUDY_NAME"));
                vo.setStudyIntro(rs.getString("STUDY_INTRO"));

                vo.setImgUrl(rs.getString("IMG_URL"));
                vo.setContents(rs.getString("CONTENTS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(pStmt);
        Common.close(conn);

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

