//package com.example.moida.dao;
//
//import com.example.moida.common.Common;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//public class BoardDAO {
//    Connection conn = null;
//    Statement stmt = null;
//    PreparedStatement pstmt = null;
//    ResultSet rSet = null;
//    public boolean boardInsert(int studyId, int userId, String boardTitle, String boardContent) {
//        int result = 0;
//        String sql = "INSERT INTO BOARD VALUES(SEQ_BOARD.NEXTVAL, ?, ?, ?, ?, SYSDATE) ";
//        try {
//            conn = Common.getConnection();
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setInt(1, studyId);
//            pstmt.setInt(2, userId);
//            pstmt.setString(3, boardTitle);
//            pstmt.setString(4, boardContent);
//
//            result = pstmt.executeUpdate();
//            System.out.println("board insert DB 결과 확인 : " + result);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Common.close(pstmt);
//        Common.close(conn);
//
//        if (result == 1) return true;
//        else return false;
//    }
//}
