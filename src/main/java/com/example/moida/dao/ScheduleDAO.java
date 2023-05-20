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
        ScheduleVO vo = new ScheduleVO();
        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SS.STUDY_SC_ID, SS.USER_ID, SS.STUDY_ID, SS.STUDY_SC_DATE, SS.STUDY_SC_CONTENT, SS.STUDY_SC_USER_LIMIT, COUNT(SM.USER_ID) AS STUDY_SC_USER_COUNT ");
            sql.append("FROM STUDY_SCHEDULE SS ");
            sql.append("LEFT JOIN STUDY_SC_MEMBER SM ON SS.STUDY_SC_ID = SM.STUDY_SC_ID ");
            sql.append("WHERE SS.STUDY_ID = ? ");
            sql.append("GROUP BY SS.STUDY_SC_ID, SS.USER_ID, SS.STUDY_ID, SS.STUDY_SC_DATE, SS.STUDY_SC_CONTENT, SS.STUDY_SC_USER_LIMIT");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyid);
            rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
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

    public boolean scheduleInsert (int userId, int studyId, Date studyScDate, String studyScContent, int studyScUSerLimit) {
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

        if(result == 1) return true;
        else return false;
    }




}