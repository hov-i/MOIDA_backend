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
            String sql = "SELECT S.STUDY_ID, S.STUDY_CATEGORY, UI.USERNAME, UI.USER_ID, S.STUDY_USER_LIMIT, S.STUDY_DEADLINE, S.STUDY_USER_COUNT, S.STUDY_NAME, S.STUDY_INTRO, S.STUDY_PROFILE, T.TAG_NAME " +
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
                        study.setUserId(rs.getInt("USER_ID"));
                        study.setStudyUserLimit(rs.getInt("STUDY_USER_LIMIT"));
                        study.setStudyDeadline(rs.getDate("STUDY_DEADLINE"));
                        study.setStudyUserCount(rs.getInt("STUDY_USER_COUNT"));
                        study.setStudyName(rs.getString("STUDY_NAME"));
                        study.setStudyIntro(rs.getString("STUDY_INTRO"));
                        study.setStudyProfile(rs.getString("STUDY_PROFILE"));
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


    public List<StudyVO> myStudySelect(int userId) {

        List<StudyVO> studyList = new ArrayList<>();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT S.STUDY_ID, S.STUDY_CATEGORY, UI.USERNAME, SU.USER_ID, S.STUDY_USER_LIMIT, S.STUDY_DEADLINE, S.STUDY_USER_COUNT, S.STUDY_NAME, S.STUDY_INTRO, S.STUDY_PROFILE, T.TAG_NAME ");
            sql.append("FROM STUDY_INFO S ");
            sql.append("JOIN STUDY_TAG_REL STR ON S.STUDY_ID = STR.STUDY_ID ");
            sql.append("JOIN TAGS T ON STR.TAG_ID = T.TAG_ID ");
            sql.append("JOIN STUDY_USER SU ON S.STUDY_ID = SU.STUDY_ID ");
            sql.append("JOIN USER_INFO UI ON SU.USER_ID = UI.USER_ID ");
            sql.append("WHERE SU.USER_ID  = ? ");
            sql.append("ORDER BY STUDY_ID DESC");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            int num = 0;
            try  {

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
                        study.setUserId(rs.getInt("USER_ID"));
                        study.setStudyUserLimit(rs.getInt("STUDY_USER_LIMIT"));
                        study.setStudyDeadline(rs.getDate("STUDY_DEADLINE"));
                        study.setStudyUserCount(rs.getInt("STUDY_USER_COUNT"));
                        study.setStudyName(rs.getString("STUDY_NAME"));
                        study.setStudyIntro(rs.getString("STUDY_INTRO"));
                        study.setStudyProfile(rs.getString("STUDY_PROFILE"));
                        study.setTagName("#" + rs.getString("TAG_NAME"));

                        prevStudyId = nowStudyId;
                    } else {
                        study.setTagName(study.getTagName() + " #" + rs.getString("TAG_NAME"));
                    }
                }
                studyList.add(study);
                System.out.println(studyList);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studyList;
    }

    public List<StudyVO> getMyCreateStudy(int userId) {

        List<StudyVO> studyList = new ArrayList<>();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT S.*, T.TAG_NAME, UI.NICKNAME FROM STUDY_INFO S ");
            sql.append("JOIN STUDY_TAG_REL STR ON S.STUDY_ID = STR.STUDY_ID ");
            sql.append("JOIN TAGS T ON STR.TAG_ID = T.TAG_ID ");
            sql.append("JOIN USER_INFO UI ON S.STUDY_MGR_ID = UI.USER_ID ");
            sql.append("WHERE S.STUDY_MGR_ID = ? ");
            sql.append("ORDER BY S.STUDY_ID DESC");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            int num = 0;
            try  {
                int prevStudyId = -1;
                StudyVO study = null;

                while (rs.next()) {
                    int nowStudyId = rs.getInt("STUDY_ID");
                    if (prevStudyId != nowStudyId) {
                        if (study != null) studyList.add(study);
                        study = new StudyVO();
                        study.setStudyId(nowStudyId);
                        study.setStudyMgrId(rs.getInt("STUDY_MGR_ID"));
                        study.setStudyName(rs.getString("STUDY_NAME"));
                        study.setStudyCategory(rs.getString("STUDY_CATEGORY"));
                        study.setStudyUserLimit(rs.getInt("STUDY_USER_LIMIT"));
                        study.setStudyUserCount(rs.getInt("STUDY_USER_COUNT"));
                        study.setStudyDeadline(rs.getDate("STUDY_DEADLINE"));
                        study.setStudyChatUrl(rs.getString("STUDY_CHAT_URL"));
                        study.setStudyIntro(rs.getString("STUDY_INTRO"));
                        study.setStudyContent(rs.getString("STUDY_CONTENT"));
                        study.setUserName(rs.getString("NICKNAME"));
                        study.setStudyProfile(rs.getString("STUDY_PROFILE"));
                        study.setTagName("#" + rs.getString("TAG_NAME"));

                        prevStudyId = nowStudyId;
                    } else {
                        study.setTagName(study.getTagName() + " #" + rs.getString("TAG_NAME"));
                    }
                }
                studyList.add(study);
                System.out.println(studyList);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
        Common.close(rs);
        Common.close(pstmt);
        Common.close(conn);
    }
        return studyList;
    }

    public StudyVO getStudyById(int studyId) {

        StudyVO vo = new StudyVO();

        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT S.*, T.TAG_NAME, UI.NICKNAME FROM STUDY_INFO S ");
            sql.append("JOIN STUDY_TAG_REL STR ON S.STUDY_ID = STR.STUDY_ID ");
            sql.append("JOIN TAGS T ON STR.TAG_ID = T.TAG_ID ");
            sql.append("JOIN USER_INFO UI ON S.STUDY_MGR_ID = UI.USER_ID ");
            sql.append("WHERE S.STUDY_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyId);
            rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                if (num == 1) vo.setTagName(vo.getTagName() + " #" + rs.getString("TAG_NAME"));
                else {
                    vo.setStudyId(studyId);
                    vo.setStudyMgrId(rs.getInt("STUDY_MGR_ID"));
                    vo.setStudyName(rs.getString("STUDY_NAME"));
                    vo.setStudyCategory(rs.getString("STUDY_CATEGORY"));
                    vo.setStudyUserLimit(rs.getInt("STUDY_USER_LIMIT"));
                    vo.setStudyUserCount(rs.getInt("STUDY_USER_COUNT"));
                    vo.setStudyDeadline(rs.getDate("STUDY_DEADLINE"));
                    vo.setStudyChatUrl(rs.getString("STUDY_CHAT_URL"));
                    vo.setStudyIntro(rs.getString("STUDY_INTRO"));
                    vo.setStudyContent(rs.getString("STUDY_CONTENT"));
                    vo.setUserName(rs.getString("NICKNAME"));
                    vo.setStudyProfile(rs.getString("STUDY_PROFILE"));
                    vo.setTagName("#" + rs.getString("TAG_NAME"));
                }
                num = 1;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    public List<StudyVO> getStudyMem(int studyid) {
        List<StudyVO> memList = new ArrayList<>();
        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT SI.STUDY_MGR_ID, UI.USER_ID, UI.NICKNAME, UI.INTRO, UI.IMG ");
            sql.append("FROM USER_INFO UI ");
            sql.append("JOIN STUDY_USER SU ON UI.USER_ID = SU.USER_ID ");
            sql.append("JOIN STUDY_INFO SI ON SU.STUDY_ID = SI.STUDY_ID ");
            sql.append("WHERE SU.STUDY_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyid);
            rs = pstmt.executeQuery();
            int num = 0;
            while (rs.next()) {
                StudyVO vo = new StudyVO();
                vo.setStudyId(studyid);
                vo.setStudyMgrId(rs.getInt("STUDY_MGR_ID"));
                System.out.println(rs.getInt("STUDY_MGR_ID"));
                vo.setUserId(rs.getInt("USER_ID"));
                System.out.println(rs.getInt("USER_ID"));
                vo.setUserName(rs.getString("NICKNAME"));
                System.out.println(rs.getString("NICKNAME"));
                vo.setUserIntro(rs.getString("INTRO"));
                System.out.println(rs.getString("INTRO"));
                vo.setUserImg(rs.getString("IMG"));
                System.out.println(rs.getString("INTRO"));
                memList.add(vo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            Common.close(rs);
            Common.close(pstmt);
            Common.close(conn);
    }
        return memList;
    }
    public boolean StudyMemOk(int studyid, int userId) {
        List<StudyVO> memList = new ArrayList<>();
        StudyVO vo = new StudyVO();
        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT STUDY_ID, USER_ID FROM STUDY_USER ");
            sql.append("WHERE STUDY_ID = ? AND USER_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, studyid);
            pstmt.setInt(2, userId);
            rs = pstmt.executeQuery();
            int num = 0;
            if(rs.next()) return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean StudyScisOk(int studyScid, int userId) {
        List<StudyVO> memList = new ArrayList<>();
        StudyVO vo = new StudyVO();
        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM STUDY_SC_MEMBER SSM  ");
            sql.append("JOIN USER_INFO UI ON UI.USER_ID = SSM.USER_ID ");
            sql.append("WHERE UI.USER_ID = ? AND SSM.STUDY_SC_ID = ? ");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, userId);
            pstmt.setInt(2, studyScid);
            rs = pstmt.executeQuery();
            int num = 0;
            if(rs.next()) return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public boolean studyInsert(int userId, String studyName, String studyCategory, int studyUserLimit, String studyChatUrl, String studyIntro, String studyContent, Date sqlDate, String studyProfile, List<String> getTagList) {
        int result = 0;
        try {
            conn = Common.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO STUDY_INFO (STUDY_ID, STUDY_MGR_ID, STUDY_NAME, STUDY_CATEGORY, STUDY_USER_LIMIT, STUDY_USER_COUNT, STUDY_CHAT_URL, STUDY_INTRO, STUDY_CONTENT, STUDY_DEADLINE, STUDY_PROFILE) ");
            sql.append("VALUES (SEQ_STUDY_ID.NEXTVAL, ?, ?, ?, ?, 1, ?, ?, ?, ?, ?)");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, userId);
            pstmt.setString(2, studyName);
            pstmt.setString(3, studyCategory);
            pstmt.setInt(4, studyUserLimit);
            pstmt.setString(5, studyChatUrl);
            pstmt.setString(6, studyIntro);
            pstmt.setString(7, studyContent);
            pstmt.setDate(8, sqlDate);
            pstmt.setString(9, studyProfile);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_INFO DB 결과 확인: " + result);

            if (result != 1) {
                return false;
            }

            sql = new StringBuilder();
            sql.append("INSERT INTO STUDY_USER (STUDY_USER_ID, STUDY_ID, USER_ID, REG_DATE) ");
            sql.append("VALUES (SEQ_STUDY_USER_ID.NEXTVAL, SEQ_STUDY_ID.CURRVAL, ?, SYSDATE)");

            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setInt(1, userId);
            result = pstmt.executeUpdate();
            System.out.println("STUDY_USER DB 결과 확인: " + result);
            if (result != 1) {
                return false;
            }
            System.out.println(getTagList);
            for (String tagName : getTagList) {
                sql = new StringBuilder();
                sql.append("INSERT INTO TAGS (TAG_ID, TAG_NAME) ");
                sql.append("SELECT SEQ_TAG_ID.NEXTVAL, ? ");
                sql.append("FROM DUAL ");
                sql.append("WHERE NOT EXISTS (SELECT 1 FROM TAGS WHERE TAG_NAME = ?)");

                pstmt = conn.prepareStatement(sql.toString());
                pstmt.setString(1, tagName);
                pstmt.setString(2, tagName);

                result = pstmt.executeUpdate();
                System.out.println("TAGS DB 결과 확인: " + result);

                sql = new StringBuilder();
                sql.append("INSERT INTO STUDY_TAG_REL (STUDY_ID, TAG_ID) ");
                sql.append("VALUES (SEQ_STUDY_ID.CURRVAL, (SELECT TAG_ID FROM TAGS WHERE TAG_NAME = ?))");

                pstmt = conn.prepareStatement(sql.toString());
                pstmt.setString(1, tagName);

                result = pstmt.executeUpdate();
                System.out.println("STUDY_TAG_REL DB 결과 확인: " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Common.close(pstmt);
            Common.close(conn);
        }

        return result == 1;
    }

    public boolean studyMgrNext (int studyId, int memId) {
        int result = 0;
        String sql = "UPDATE STUDY_INFO SET STUDY_MGR_ID = ? WHERE STUDY_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memId);
            pstmt.setInt(2, studyId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_INFO DB 업데이트 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }

    public boolean studyMemDelete (int studyId, int userId) {
        int result = 0;
        String sql = "DELETE FROM STUDY_USER WHERE STUDY_ID = ? AND USER_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studyId);
            pstmt.setInt(2, userId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_USER DB 삭제 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }
    public boolean studyJoinInsert (int studyId, int userId) {
        int result = 0;
        String sql = "INSERT INTO STUDY_USER(STUDY_USER_ID, STUDY_ID, USER_ID, REG_DATE) VALUES(SEQ_STUDY_USER_ID.NEXTVAL, ?, ?, SYSDATE) ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, studyId);
            pstmt.setInt(2, userId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_USER DB 추가 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }

    public boolean studyJoinMemUpdate (int studyId) {
        int result = 0;
        String sql = "UPDATE STUDY_INFO SET STUDY_USER_COUNT = STUDY_USER_COUNT+1 WHERE STUDY_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, studyId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_INFO MemCnt+ DB 추가 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }
    public boolean studyJoinMemDisCount (int studyId) {
        int result = 0;
        String sql = "UPDATE STUDY_INFO SET STUDY_USER_COUNT = STUDY_USER_COUNT-1 WHERE STUDY_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, studyId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_INFO MemCnt- DB 추가 결과 확인 : " + result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }
    public boolean studyOut (int studyId, int userId) {
        int result = 0;
        String sql = "DELETE FROM STUDY_USER WHERE STUDY_ID = ? AND USER_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studyId);
            pstmt.setInt(2, userId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY_USER DB 삭제 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }

    public boolean studyDrop (int studyId, int userId) {
        int result = 0;
        String sql = "DELETE FROM STUDY_INFO WHERE STUDY_ID = ? AND STUDY_MGR_ID = ? ";
        try {
            conn = Common.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studyId);
            pstmt.setInt(2, userId);

            result = pstmt.executeUpdate();
            System.out.println("STUDY DB 삭제 결과 확인 : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Common.close(pstmt);
        Common.close(conn);
        if(result == 1) return true;
        else return false;
    }
}