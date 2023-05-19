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
            sql.append("SELECT SS.STUDY_SC_ID, SS.STUDY_SC_DATE, SS.STUDY_ID, SS.STUDY_SC_CONTENT, SS.STUDY_SC_USER_LIMIT, COUNT(UI.USER_ID) ");
            sql.append("FROM STUDY_SCHEDULE SS ");
            sql.append("JOIN STUDY_SC_MEMBER SM ON SS.STUDY_SC_ID = SM.STUDY_SC_ID ");
            sql.append("JOIN USER_INFO UI ON SM.USER_ID = UI.USER_ID ");
            sql.append("WHERE SS.STUDY_ID = ? ");
            sql.append("GROUP BY SS.STUDY_SC_ID, SS.STUDY_SC_DATE, SS.STUDY_ID, SS.STUDY_SC_CONTENT, SS.STUDY_SC_USER_LIMIT");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyid);
            rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                vo.setStudyId(studyid);
                vo.setStudyScId(rs.getInt("STUDY_SC_ID"));
                vo.setStudyScDate(rs.getDate("STUDY_SC_DATE"));
                vo.setStudyScContent(rs.getString("STUDY_SC_CONTENT"));
                vo.setStudyScUserLimit(rs.getInt("STUDY_SC_USER_LIMIT"));
                vo.setStudyScUserCount(rs.getInt("COUNT(UI.USER_ID)"));
                scList.add(vo);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scList;
    }



}