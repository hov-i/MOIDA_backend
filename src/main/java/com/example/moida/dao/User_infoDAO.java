package com.example.moida.dao;

import com.example.moida.common.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class User_infoDAO {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    Scanner sc = new Scanner(System.in);
    boolean isLogIn = false;
    public String loginId = null;


    //회원 가입(OK)
    public boolean memberRegister(String userName, String pw, String pw_confirm, String email, String phone, String nickname) {
        int result = 0;
        boolean success = false;
        if(pw.equals(pw_confirm)) { // 비밀번호가 일치하는 경우
            String sql = "INSERT INTO USER_INFO(USERNAME, PW, PW_CONFIRM, EMAIL, PHONE, NICKNAME, JOIN_DATE) VALUES(?,?,?,?,?,?,SYSDATE)";

            try {
                conn = Common.getConnection();
                pStmt = conn.prepareStatement(sql);
                pStmt.setString(1, userName);
                pStmt.setString(2, pw);
                pStmt.setString(3, pw_confirm);
                pStmt.setString(4, email);
                pStmt.setString(5, phone);
                pStmt.setString(6, nickname);
                result = pStmt.executeUpdate();
                System.out.println("회원가입이 완료되었습니다. " + result);
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Common.close(pStmt);
                Common.close(conn);
            }
        } else { // 비밀번호가 일치하지 않는 경우
            System.out.println("비밀번호가 일치하지 않습니다.");
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

    // 로그인 체크(OK)
    public boolean loginCheck(String userName, String pw) {
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement(); // Statement 객체 얻기
            String sql = "SELECT USERNAME, PW FROM USER_INFO WHERE USERNAME = " + "'" + userName + "'";
            rs = stmt.executeQuery(sql);

            while(rs.next()) { // 읽을 데이터가 있으면 true
                String userNameSql = rs.getString("USERNAME"); // 쿼리문 수행 결과에서 ID값을 가져 옴
                String pwSql = rs.getString("PW");
                System.out.println("USERNAME : " + userNameSql);
                System.out.println("PW : " + pwSql);
                if(userName.equals(userNameSql) && pw.equals(pwSql)) {
                    Common.close(rs);
                    Common.close(stmt);
                    Common.close(conn);
                    return true;
                }
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 아이디 찾기

    // 비밀번호 찾기





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

//    //MY Page
//    // 내 정보 조회
//    public UserInfoVO getMyInfo(String user_id) {
//        UserInfoVO userInfo = null;
//        try {
//            conn = Common.getConnection();
//            String sql = "SELECT * FROM USER_INFO WHERE USER_ID = ?";
//            pStmt = conn.prepareStatement(sql);
//            pStmt.setString(1, user_id);
//            rs = pStmt.executeQuery();
//            if (rs.next()) {
//                userInfo = new UserInfoVO();
//                userInfo.setCnt(rs.getInt("CNT"));
//                userInfo.setUser_id(rs.getString("USER_ID"));
//                userInfo.setUser_pw(rs.getString("USER_PW"));
//                userInfo.setEmail(rs.getString("EMAIL"));
//                userInfo.setPhone(rs.getString("PHONE"));
//                userInfo.setNickname(rs.getString("NICKNAME"));
//                userInfo.setJoinDate(rs.getDate("JOIN_DATE"));
//                userInfo.setWithdrawDate(rs.getDate("WITHDRAW_DATE"));
//                userInfo.setExpireDate(rs.getDate("EXPIRE_DATE"));
//            }
//            Common.close(rs);
//            Common.close(pStmt);
//            Common.close(conn);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return userInfo;
//    }
//
    // 이메일 변경(OK)
    public boolean updateEmail(String userName, String newEmail) {
        boolean result = false;
        try {
            conn = Common.getConnection();
            String sql = "SELECT COUNT(*) FROM USER_INFO WHERE EMAIL = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, newEmail);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // 변경하려는 이메일이 이미 존재하는 경우
                return result;
            }

            sql = "UPDATE USER_INFO SET EMAIL = ? WHERE USERNAME = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, newEmail);
            pStmt.setString(2, userName);
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


    // 비밀번호 변경(OK) - 이건 넣고 싶으면 아예 수정 구조를 변형해야 함...
    public boolean updatePassword(String userName, String pw, String newPw) {
        boolean result = false;
        try {
            conn = Common.getConnection();
            String sql = "SELECT COUNT(*) FROM USER_INFO WHERE USERNAME = ? AND PW = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userName);
            pStmt.setString(2, pw);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // 현재 비밀번호가 일치하지 않는 경우
                return result;
            }

            sql = "UPDATE USER_INFO SET PW = ? WHERE USERNAME = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, newPw);
            pStmt.setString(2, userName);
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
    public boolean updateNickname(String userName, String newNickname) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM USER_INFO WHERE NICKNAME = ?");
            pStmt.setString(1, newNickname);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 0) { // 중복되는 닉네임이 없으면 변경 가능
                pStmt = conn.prepareStatement("UPDATE USER_INFO SET NICKNAME = ? WHERE USERNAME = ?");
                pStmt.setString(1, newNickname);
                pStmt.setString(2, userName);
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
    public boolean updatePhone(String userName, String newPhone) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM USER_INFO WHERE PHONE = ?");
            pStmt.setString(1, newPhone);
            rs = pStmt.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 0) { // 중복되는 폰 번호가 없으면 변경 가능
                pStmt = conn.prepareStatement("UPDATE USER_INFO SET PHONE = ? WHERE USERNAME = ?");
                pStmt.setString(1, newPhone);
                pStmt.setString(2, userName);
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






    //회원 탈퇴
    public boolean memberDelete(String userName, String pw) {
        boolean result = false;

        try {
            conn = Common.getConnection();
            pStmt = conn.prepareStatement("SELECT * FROM USER_INFO WHERE USERNAME = ?");
            pStmt.setString(1, userName);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                String dbPw = rs.getString("PW");
                if (dbPw.equals(pw)) { // 입력한 비밀번호와 일치하면 탈퇴 진행
                    pStmt = conn.prepareStatement("DELETE FROM USER_INFO WHERE USERNAME = ?");
                    pStmt.setString(1, userName);
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
