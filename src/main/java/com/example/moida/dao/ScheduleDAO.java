package com.example.moida.dao;

import com.example.moida.common.Common;
import com.example.moida.vo.ScheduleVO;
import com.example.moida.vo.StudyVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;

    public List<ScheduleVO> getStudySc(int studyid) {
        List<ScheduleVO> scList = new ArrayList<>();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SS.STUDY_SC_ID, SS.USER_ID, SS.STUDY_ID, SS.STUDY_SC_DATE, SS.STUDY_SC_CONTENT, SS.STUDY_SC_USER_LIMIT, COUNT(SM.USER_ID) AS STUDY_SC_USER_COUNT ");
            sql.append("FROM STUDY_SCHEDULE SS ");
            sql.append("LEFT JOIN STUDY_SC_MEMBER SM ON SS.STUDY_SC_ID = SM.STUDY_SC_ID ");
            sql.append("WHERE SS.STUDY_ID = ? ");
            sql.append("GROUP BY SS.STUDY_SC_ID, SS.USER_ID, SS.STUDY_ID, SS.STUDY_SC_DATE, SS.STUDY_SC_CONTENT, SS.STUDY_SC_USER_LIMIT ");
            sql.append("ORDER BY SS.STUDY_SC_DATE");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ScheduleVO vo = new ScheduleVO();
                vo.setStudyId(studyid);
                vo.setStudyScId(rs.getInt("STUDY_SC_ID"));
                vo.setUserId(rs.getInt("USER_ID"));
                vo.setStudyScDate(rs.getDate("STUDY_SC_DATE"));
                vo.setStudyScContent(rs.getString("STUDY_SC_CONTENT"));
                vo.setStudyScUserLimit(rs.getInt("STUDY_SC_USER_LIMIT"));
                vo.setStudyScUserCount(rs.getInt("STUDY_SC_USER_COUNT"));
                scList.add(vo);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scList;
    }

    public boolean scheduleInsert(int userId, int studyId, Date studyScDate, String studyScContent, int studyScUSerLimit) {
        int result = 0;
        String sql = "INSERT INTO STUDY_SCHEDULE(STUDY_SC_ID, USER_ID, STUDY_ID, STUDY_SC_DATE, STUDY_SC_CONTENT, STUDY_SC_USER_LIMIT) VALUES (SEQ_STUDY_SC_ID.NEXTVAL, ?, ?, ?, ?, ?) ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, userId);
            pstmt.setInt(2, studyId);
            pstmt.setDate(3, studyScDate);
            pstmt.setString(4, studyScContent);
            pstmt.setInt(5, studyScUSerLimit);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_SCHEDULE DB 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if (result == 1) return true;
        else return false;
    }

    public boolean scheduleMemInsert(int studyScId, int userId) {
        int result = 0;
        String sql = "INSERT INTO STUDY_SC_MEMBER(SC_MEMBER_ID, STUDY_SC_ID, USER_ID) VALUES (SEQ_SC_MEMBER_ID.NEXTVAL, ?, ?) ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, studyScId);
            pstmt.setInt(2, userId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_SC_MEMBER DB 추가 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if (result == 1) return true;
        else return false;
    }

    public boolean scheduleMemDelete(int studyScId, int userId) {
        int result = 0;
        String sql = "DELETE FROM STUDY_SC_MEMBER WHERE STUDY_SC_ID = ? AND USER_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, studyScId);
            pstmt.setInt(2, userId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_SC_MEMBER DB 삭제 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if (result == 1) return true;
        else return false;
    }

    public boolean scheduleDelete(int studyScId) {
        int result = 0;
        String sql = "DELETE FROM STUDY_SCHEDULE WHERE STUDY_SC_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studyScId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_SCHEDULE DB 삭제 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if (result == 1) return true;
        else return false;
    }

    public List<ScheduleVO> getStudyScMem(int studyScId) {
        List<ScheduleVO> scList = new ArrayList<>();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SS.USER_ID, SSM.SC_MEMBER_ID, UI.USERNAME, UI.INTRO, UI.IMG FROM USER_INFO UI  ");
            sql.append("JOIN STUDY_SC_MEMBER SSM ON UI.USER_ID = SSM.USER_ID ");
            sql.append("JOIN STUDY_SCHEDULE SS ON SSM.STUDY_SC_ID = SS.STUDY_SC_ID ");
            sql.append("WHERE SSM.STUDY_SC_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyScId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ScheduleVO vo = new ScheduleVO();
                vo.setStudyScId(studyScId);
                vo.setStudyScMemId(rs.getInt("SC_MEMBER_ID"));
                vo.setUserName(rs.getString("USERNAME"));
                vo.setUserIntro(rs.getString("INTRO"));
                vo.setUserImg(rs.getString("IMG"));
                scList.add(vo);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scList;
    }

    public List<ScheduleVO> getStudyMySc(int userId) {
        List<ScheduleVO> scList = new ArrayList<>();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SS.STUDY_SC_ID, SS.STUDY_SC_DATE, SS.STUDY_SC_CONTENT, SI.STUDY_NAME, SI.STUDY_PROFILE ");
            sql.append("FROM STUDY_SC_MEMBER SSM ");
            sql.append("JOIN STUDY_SCHEDULE SS ON SS.STUDY_SC_ID = SSM.STUDY_SC_ID ");
            sql.append("JOIN STUDY_INFO SI ON SS.STUDY_ID = SI.STUDY_ID ");
            sql.append("WHERE SSM.USER_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ScheduleVO vo = new ScheduleVO();
                vo.setUserId(userId);
                vo.setStudyScId(rs.getInt("STUDY_SC_ID"));
                vo.setStudyScDate(rs.getDate("STUDY_SC_DATE"));
                vo.setStudyScContent(rs.getString("STUDY_SC_CONTENT"));
                vo.setStudyName(rs.getString("STUDY_NAME"));
                vo.setStudyProfile(rs.getString("STUDY_PROFILE"));
                scList.add(vo);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scList;


    }
}