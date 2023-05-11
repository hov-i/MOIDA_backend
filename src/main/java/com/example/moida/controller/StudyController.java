package com.example.moida.controller;

import com.example.moida.dao.StudyDAO;
import com.example.moida.vo.StudyVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StudyController {

    @GetMapping("/study")
    public ResponseEntity<List<StudyVO>> studylist() {
        StudyDAO dao = new StudyDAO();
        List<StudyVO> list = dao.studySelect();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    @PostMapping("/new")
//    public ResponseEntity<Boolean> studyRegister( Map<String, String> regData) throws ParseException, SQLException {
//        String getStudyMgrId = regData.get("study_mgr_id");
//        String getStudyName = regData.get("study_name");
//        String getStudyCategory = regData.get("study_category");
//        String getTagList = regData.get("tag_list");
//
//        String getStudyUserLimit = regData.get("study_user_limit");
//        int studyUserLimit = Integer.parseInt(getStudyUserLimit);
//
//        String getStudyDeadline = regData.get("study_deadline");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date studyDeadline = dateFormat.parse(getStudyDeadline);
//        java.sql.Date sqlDate = new java.sql.Date(studyDeadline.getTime());
//
//        String getStudyChatUrl = regData.get("study_chat_url");
//        String getStudyInfo = regData.get("study_info");
//
//        String getStudyContent = regData.get("study_content");
//        Clob studyContent = new SerialClob(getStudyContent.toCharArray());
//        StudyDAO dao = new StudyDAO();
//        boolean isTrue = dao.studyRegister(getStudyMgrId, getStudyName, getStudyCategory, getTagList, studyUserLimit, sqlDate,getStudyChatUrl, getStudyInfo, studyContent);
//        return new ResponseEntity<>(isTrue, HttpStatus.OK);
//    }

}
