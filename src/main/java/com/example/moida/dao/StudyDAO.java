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
    private PreparedStatement pStmt = null;

    public List<StudyVO> studySelect() {
        List<StudyVO> studyList = new ArrayList<>();
        try (Connection conn = Common.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT S.STUDY_ID, S.STUDY_CATEGORY, U.USER_NAME, S.STUDY_USER_LIMIT, S.STUDY_DEADLINE, S.STUDY_USER_COUNT, S.STUDY_NAME, S.STUDY_INTRO, T.TAG_NAME " +
                    "FROM STUDY_INFO S " +
                    "JOIN TAG_LIST T ON S.STUDY_ID = T.STUDY_ID " +
                    "JOIN USER_INFO U ON U.USER_ID = S.STUDY_MGR_ID";
            try (ResultSet rs = stmt.executeQuery(sql)) {

                int prevStudyId = -1;
                StudyVO study = null;

                while (rs.next()) {
                    int studyId = rs.getInt("STUDY_ID");
                    if (prevStudyId != studyId) {
                        if (study != null) studyList.add(study);
                        study = new StudyVO();
                        study.setStudy_id(studyId);
                        study.setStudy_category(rs.getString("STUDY_CATEGORY"));
                        study.setUser_name(rs.getString("USER_NAME"));
                        study.setStudy_user_limit(rs.getInt("STUDY_USER_LIMIT"));
                        study.setStudy_deadline(rs.getDate("STUDY_DEADLINE"));
                        study.setStudy_user_count(rs.getInt("STUDY_USER_COUNT"));
                        study.setStudy_name(rs.getString("STUDY_NAME"));
                        study.setStudy_intro(rs.getString("STUDY_INTRO"));
                        study.setTag_name("#" + rs.getString("TAG_NAME"));

                        prevStudyId = studyId;
                    } else {
                        study.setTag_name(study.getTag_name() + " #" + rs.getString("TAG_NAME"));
                    }
                }
                studyList.add(study);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studyList;
    }
}



//    public boolean studyRegister(String study_mgr_id, String study_name, String study_category, String tag_list, int study_user_limit, Date study_deadline, String study_chat_url, String study_info, Clob study_content{
//        int result = 0;
//        boolean success = false;
//        try {
//            String sql = "INSERT INTO STUDY_INFO(STUDY_ID, STUDY_MGR_ID, STUDY_NAME, STUDY_CATEGORY, STUDY_USER_LIMIT, STUDY_DEADLINE, STUDY_CHAT_URL, STUDY_INTRO, STUDY_CONTENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            try {
//                conn = Common.getConnection();
//                pStmt = conn.prepareStatement(sql);
//                pStmt.setString(1, study_mgr_id);
//                pStmt.setString(2, study_name);
//                pStmt.setString(3, study_category);
//                pStmt.setString(4, tag_list);
//                pStmt.setInt(5, study_user_limit);
//                pStmt.setDate(6, study_deadline);
//                pStmt.setString(7, study_chat_url);
//                pStmt.setString(8, study_info);
//                pStmt.setClob(9, study_content);
//                result = pStmt.executeUpdate();
//                System.out.println("스터디 생성이 완료되었습니다." + result);
//                success = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                Common.close(pStmt);
//                Common.close(conn);
//            }
//        }
//    }
