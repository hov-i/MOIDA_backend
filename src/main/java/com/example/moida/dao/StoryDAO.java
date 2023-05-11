package com.example.moida.dao;

import com.example.moida.common.Common;
import com.example.moida.vo.StoryVO;
import com.example.moida.vo.StudyVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StoryDAO {
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pStmt = null;

    public List<StoryVO> StorySelect() {
        List<StoryVO> list = new ArrayList<>();
        try {
            conn = Common.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT ST.STORY_IMG, ST.STORY_NAME, SI.STUDY_NAME FROM STORY ST JOIN STUDY_INFO SI ON ST.STUDY_ID = SI.STUDY_ID";
            rs =stmt.executeQuery(sql);
            while (rs.next()) {
                String story_img = rs.getString("STORY_IMG");
                String story_name = rs.getString("STORY_NAME");
                String study_name = rs.getString("STUDY_NAME");

                StoryVO vo = new StoryVO();
                vo.setStory_img(story_img);
                vo.setStory_name(story_name);
                vo.setStudy_name(study_name);
                list.add(vo);
            }
            Common.close(rs);
            Common.close(stmt);
            Common.close(conn);
        } catch (Exception e) {
            e.printStackTrace();

        }return list;
    }
}
