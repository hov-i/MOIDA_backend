package com.example.moida.dao;

import com.example.moida.common.Common;
import com.example.moida.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class UserInfoDAO {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    Scanner sc = new Scanner(System.in);
    boolean isLogIn = false;
    public String loginId = null;

    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/photo-moida.appspot.com/o/LOGO_imgOnly.png?alt=media&token=28c02812-d6d0-4b84-80f8-ef3103fa8462";
    private static final String DEFAULT_PROFILE_INTRO = "자기 소개를 작성해 주세요.";
    //회원 가입(OK)
    public boolean memberRegister(String userName, String pw, String pwConfirm, String email, String phone, String nickname) {
        int result = 0;
        boolean success = false;

        // 회원 가입 시 기본 프로필 이미지 URL 설정
        String img = DEFAULT_PROFILE_IMAGE_URL;
        String intro = DEFAULT_PROFILE_INTRO;

        String sql = "INSERT INTO USER_INFO(USERNAME, PW, EMAIL, PHONE, NICKNAME, IMG, INTRO, JOIN_DATE) VALUES(?,?,?,?,?,?,?,SYSDATE)";

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userName);
            pStmt.setString(2, pw);
            pStmt.setString(3, email);
            pStmt.setString(4, phone);
            pStmt.setString(5, nickname);
            pStmt.setString(6, img);  // 이미지 URL 추가
            pStmt.setString(7, intro);
            result = pStmt.executeUpdate();

            if (result > 0) {
                System.out.println("회원가입이 완료되었습니다. " + result);
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.close(pStmt);
            Common.close(conn);
        }

        return success;
    }


    // 회원 가입 여부 확인(OK)
    public boolean regMemberCheck(String userName) {
        boolean isNotReg = false;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM USER_INFO WHERE USERNAME = " + "'" + userName +"'";
            rs = stmt.executeQuery(sql);
            if(rs.next()) isNotReg = false;
            else isNotReg = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        Common.close(rs);
        Common.close(stmt);
        Common.close(conn);
        return isNotReg; // 가입 되어 있으면 false, 가입이 안되어 있으면 true
    }

    // 아이디, 닉네임 중복 검사
    public boolean checkUsername(String userName) {
        String query = "SELECT COUNT(*) FROM USER_INFO WHERE USERNAME = ?";
        int count = jdbcTemplate.queryForObject(query, new Object[]{userName}, Integer.class);
        return count > 0;
    }

    public boolean checkNickname(String nickname) {
        String query = "SELECT COUNT(*) FROM USER_INFO WHERE NICKNAME = ?";
        int count = jdbcTemplate.queryForObject(query, new Object[]{nickname}, Integer.class);
        return count > 0;
    }


    // 정보 받아오기
    public UserInfoVO getUserInfo(String userName) {
        UserInfoVO userInfo = null;
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT USERNAME, EMAIL, NICKNAME, USER_ID, PHONE, IMG, INTRO FROM USER_INFO WHERE USERNAME = '" + userName + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String userNameSql = rs.getString("USERNAME");
                String email = rs.getString("EMAIL");
                String nickname = rs.getString("NICKNAME");
                int userId = rs.getInt("USER_ID");
                String phone = rs.getString("PHONE");
                String img = rs.getString("IMG");
                String intro = rs.getString("INTRO");

                userInfo = new UserInfoVO();
                userInfo.setUserName(userNameSql);
                userInfo.setEmail(email);
                userInfo.setNickname(nickname);
                userInfo.setUserId(userId);
                userInfo.setPhone(phone);
                userInfo.setImg(img);
                userInfo.setIntro(intro);
            }

            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    // 로그인
    public UserInfoVO loginCheck(String userName, String pw) {
        UserInfoVO userInfo = null;
        try {
            conn = Common.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT USERNAME, PW, EMAIL, NICKNAME, USER_ID, PHONE, IMG, INTRO FROM USER_INFO WHERE USERNAME = ? AND PW = ?");
            pstmt.setString(1, userName);
            pstmt.setString(2, pw);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userInfo = new UserInfoVO();
                userInfo.setUserName(rs.getString("USERNAME"));
                userInfo.setPw(rs.getString("PW"));
                userInfo.setEmail(rs.getString("EMAIL"));
                userInfo.setNickname(rs.getString("NICKNAME"));
                userInfo.setUserId(rs.getInt("USER_ID"));
                userInfo.setPhone(rs.getString("PHONE"));
                userInfo.setImg(rs.getString("IMG"));
                userInfo.setIntro(rs.getString("INTRO"));
            }

            Common.close(rs);
            Common.close(pstmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }








    // 아이디 찾기

    // 비밀번호 임시 비밀번호로 변경
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean changePasswordToTemporary(String username, String temporaryPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        int rowsAffected = jdbcTemplate.update(sql, temporaryPassword, username);
        return rowsAffected > 0;
    }





//
//    // 회원 정보 삭제 1년 후(매일 새벽 2시 기준) **이건 다시 확인 필요 10일 전 메일 보내게 하고 싶음
//    public void deleteInactiveAccounts() {
//        try {
//            conn = Common.getConnection();
//            pStmt = conn.prepareStatement("SELECT * FROM USER_INFO WHERE LAST_LOGIN <= DATE_SUB(CURRENT_DATE, INTERVAL 1 YEAR)");
//            rs = pStmt.executeQuery();
//
//            List<String> deletedAccounts = new ArrayList<>();
//            int deletedCount = 0;
//
//            while (rs.next()) {
//                String userId = rs.getString("USER_ID");
//                pStmt = conn.prepareStatement("DELETE FROM USER_INFO WHERE USER_ID = ?");
//                pStmt.setString(1, userId);
//                pStmt.executeUpdate();
//                deletedAccounts.add(userId);
//                deletedCount++;
//            }
//
//            Common.close(rs);
//            Common.close(pStmt);
//            Common.close(conn);
//
//            // 로그에 자동 삭제된 계정 목록과 삭제된 계정 수를 출력합니다.
//            String logMessage = "Deleted " + deletedCount + " inactive accounts: " + deletedAccounts.toString();
//            Logger.getLogger(getClass().getName()).info(logMessage);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//

    //MY Page
    // 내 정보 조회
    public UserInfoVO getMyInfo(String userId) {
        UserInfoVO userInfo = null;
        try {
            conn = Common.getConnection();
            String sql = "SELECT * FROM USER_INFO WHERE USER_ID = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userId);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                userInfo = new UserInfoVO();
                userInfo.setNickname(rs.getString("NICKNAME"));
                userInfo.setEmail(rs.getString("EMAIL"));
                userInfo.setPhone(rs.getString("PHONE"));
                userInfo.setImg(rs.getString("IMG"));
                userInfo.setIntro(rs.getString("INTRO"));
                userInfo.setJoinDate(rs.getDate("JOIN_DATE"));
            }
            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }




    // 프로필 수정
    // 이메일 변경(OK)
    public boolean updateEmail(String userId, String email) {
        boolean result = false;
        try {
            conn = Common.getConnection();
            String sql = "SELECT COUNT(*) FROM USER_INFO WHERE EMAIL = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, email);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // 변경하려는 이메일이 이미 존재하는 경우
                return result;
            }

            sql = "UPDATE USER_INFO SET EMAIL = ? WHERE USER_ID = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, email);
            pStmt.setString(2, userId);
            int cnt = pStmt.executeUpdate();
            if (cnt > 0) {
                result = true;
            }
            Common.close(pStmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    // 비밀번호 변경(OK)
    public boolean updatePassword(String userId, String pw, String newPw) {
        boolean result = false;
        try {
            conn = Common.getConnection();
            String sql = "SELECT COUNT(*) FROM USER_INFO WHERE USER_ID = ? AND PW = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userId);
            pStmt.setString(2, pw);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // 현재 비밀번호가 일치하지 않는 경우
                return result;
            }

            sql = "UPDATE USER_INFO SET PW = ? WHERE USER_ID = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, newPw);
            pStmt.setString(2, userId);
            int cnt = pStmt.executeUpdate();
            if (cnt > 0) {
                result = true;
            }
            Common.close(pStmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 닉네임 변경(OK)
    public boolean updateNickname(String userId, String nickname) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM USER_INFO WHERE NICKNAME = ?");
            pStmt.setString(1, nickname);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 0) { // 중복되는 닉네임이 없으면 변경 가능
                pStmt = conn.prepareStatement("UPDATE USER_INFO SET NICKNAME = ? WHERE USER_ID = ?");
                pStmt.setString(1, nickname);
                pStmt.setString(2, userId);
                pStmt.executeUpdate();
                result = true;
            }

            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    // 핸드폰 번호 변경(OK)
    public boolean updatePhone(String userId, String phone) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM USER_INFO WHERE PHONE = ?");
            pStmt.setString(1, phone);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 0) { // 중복되는 폰 번호가 없으면 변경 가능
                pStmt = conn.prepareStatement("UPDATE USER_INFO SET PHONE = ? WHERE USER_ID = ?");
                pStmt.setString(1, phone);
                pStmt.setString(2, userId);
                pStmt.executeUpdate();
                result = true;
            }

            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 이미지 url 업로드
    public boolean uploadImageURL(String userId, String img) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("UPDATE USER_INFO SET IMG = ? WHERE USER_ID = ?");
            pStmt.setString(1, img);
            pStmt.setString(2, userId);
            pStmt.executeUpdate();
            result = true;

            Common.close(pStmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // 자기소개 업로드
    public boolean uploadIntro(String userId, String intro) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("UPDATE USER_INFO SET Intro = ? WHERE USER_ID = ?");
            pStmt.setString(1, intro);
            pStmt.setString(2, userId);
            pStmt.executeUpdate();
            result = true;

            Common.close(pStmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }




    //회원 탈퇴
    public boolean memberDelete(String userId, String pw) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("SELECT * FROM USER_INFO WHERE USER_ID = ?");
            pStmt.setString(1, userId);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String dbPw = rs.getString("PW");
                if (dbPw.equals(pw)) { // 입력한 비밀번호와 일치하면 탈퇴 진행
                    pStmt = conn.prepareStatement("DELETE FROM USER_INFO WHERE USER_ID = ?");
                    pStmt.setString(1, userId);
                    pStmt.executeUpdate();
                    result = true;
                }
            }

            Common.close(rs);
            Common.close(pStmt);
            Common.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
