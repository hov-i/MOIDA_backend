package com.example.moida.dao;

import com.example.moida.common.Common;
import com.example.moida.vo.StudyVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyDAO {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;

    public List<StudyVO> studySelect() {
        List<StudyVO> studyList = new ArrayList<>();
        try (Connection conn = Common.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT S.STUDY_ID, S.STUDY_CATEGORY, UI.USERNAME, S.STUDY_USER_LIMIT, S.STUDY_DEADLINE, S.STUDY_USER_COUNT, S.STUDY_NAME, S.STUDY_INTRO, T.TAG_NAME " +
                    "FROM STUDY_INFO S " +
                    "JOIN STUDY_TAG_REL STR ON S.STUDY_ID = STR.STUDY_ID " +
                    "JOIN TAGS T ON STR.TAG_ID = T.TAG_ID " +
                    "JOIN USER_INFO UI ON S.STUDY_MGR_ID = UI.USER_ID " +
                    "ORDER BY STUDY_ID DESC";
            try (ResultSet rs = stmt.executeQuery(sql)) {

                int prevStudyId = -1;
                StudyVO study = null;

                while (rs.next()) {
                    int nowStudyId = rs.getInt("STUDY_ID");
                    if (prevStudyId != nowStudyId) {
                        if (study != null) studyList.add(study);
                        study = new StudyVO();
                        study.setStudyId(nowStudyId);
                        study.setStudyCategory(rs.getString("STUDY_CATEGORY"));
                        study.setUserName(rs.getString("USERNAME"));
                        study.setStudyUserLimit(rs.getInt("STUDY_USER_LIMIT"));
                        study.setStudyDeadline(rs.getDate("STUDY_DEADLINE"));
                        study.setStudyUserCount(rs.getInt("STUDY_USER_COUNT"));
                        study.setStudyName(rs.getString("STUDY_NAME"));
                        study.setStudyIntro(rs.getString("STUDY_INTRO"));
                        study.setTagName("#" + rs.getString("TAG_NAME"));

                        prevStudyId = nowStudyId;
                    } else {
                        study.setTagName(study.getTagName() + " #" + rs.getString("TAG_NAME"));
                    }
                }
                studyList.add(study);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studyList;
    }

    public StudyVO getStudyById(int studyId) {
        StudyVO vo = new StudyVO();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT S.*, T.TAG_NAME  FROM STUDY_INFO S ");
            sql.append("JOIN STUDY_TAG_REL STR ON S.STUDY_ID = STR.STUDY_ID ");
            sql.append("JOIN TAGS T ON STR.TAG_ID = T.TAG_ID ");
            sql.append("WHERE S.STUDY_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyId);
            rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                vo.setStudyId(studyId);
                vo.setStudyMgrId(rs.getString("STUDY_MGR_ID"));
                vo.setStudyName(rs.getString("STUDY_NAME"));
                vo.setStudyCategory(rs.getString("STUDY_CATEGORY"));
                vo.setStudyUserLimit(rs.getInt("STUDY_USER_LIMIT"));
                vo.setStudyUserCount(rs.getInt("STUDY_USER_COUNT"));
                vo.setStudyDeadline(rs.getDate("STUDY_DEADLINE"));
                vo.setStudyChatUrl(rs.getString("STUDY_CHAT_URL"));
                vo.setStudyIntro(rs.getString("STUDY_INTRO"));
                vo.setStudyContent(rs.getString("STUDY_CONTENT"));
                vo.setTagName("#" + rs.getString("TAG_NAME"));
                num = 1;
                if (num == 1) vo.setTagName(vo.getTagName() + " #" + rs.getString("TAG_NAME"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    public StudyVO getStudyMem(int studyid) {
        StudyVO vo = new StudyVO();
        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT UI.NICKNAME, UI.INTRO ");
            sql.append("FROM USER_INFO UI ");
            sql.append("JOIN STUDY_USER SU ON UI.USER_ID = SU.USER_ID ");
            sql.append("WHERE SU.STUDY_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyid);
            rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                vo.setStudyId(studyid);
                vo.setUserName(rs.getString("NICKNAME"));
                vo.setStudyName(rs.getString("STUDY_NAME"));
                num = 1;
                if (num == 1) vo.setTagName(vo.getTagName() + " #" + rs.getString("TAG_NAME"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }



}