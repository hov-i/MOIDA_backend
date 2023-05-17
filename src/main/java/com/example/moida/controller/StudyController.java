package com.example.moida.controller;

import com.example.moida.dao.StudyDAO;
import com.example.moida.vo.StudyVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialClob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.sql.Clob;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StudyController {

    @GetMapping("/study")
    public ResponseEntity<List<StudyVO>> studylist() {
        StudyDAO dao = new StudyDAO();
        List<StudyVO> list = dao.studySelect();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/study/studyRoom/Main/{studyId}")
    public ResponseEntity<StudyVO> viewStudy(@PathVariable int studyId) {
        System.out.println("Study Id : " + studyId);
        StudyDAO dao = new StudyDAO();
        StudyVO studyInfo = dao.getStudyById(studyId);
        return new ResponseEntity<>(studyInfo, HttpStatus.OK);
    }

    @GetMapping("/study/studyRoom/Member/{studyId}")
    public ResponseEntity<StudyVO> viewStudyMem(@PathVariable int studyId) {
        System.out.println("Study Id : " + studyId);
        StudyDAO dao = new StudyDAO();
        StudyVO studyInfo = dao.getStudyById(studyId);
        return new ResponseEntity<>(studyInfo, HttpStatus.OK);
    }



}
