package com.example.moida.controller;

import com.example.moida.dao.PostDAO;
import com.example.moida.dao.ScheduleDAO;
import com.example.moida.dao.StudyDAO;
import com.example.moida.vo.ScheduleVO;
import com.example.moida.vo.StudyVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.rowset.serial.SerialClob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Clob;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StudyController {

    // 스터디 리스트
    @GetMapping("/study")
    public ResponseEntity<List<StudyVO>> studylist() {
        StudyDAO dao = new StudyDAO();
        List<StudyVO> list = dao.studySelect();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    // 스터디룸 메인
    @GetMapping("/study/studyRoom/Main/{studyId}")
    public ResponseEntity<StudyVO> viewStudy(@PathVariable int studyId) {
        System.out.println("Study Id : " + studyId);
        StudyDAO dao = new StudyDAO();
        StudyVO studyInfo = dao.getStudyById(studyId);
        return new ResponseEntity<>(studyInfo, HttpStatus.OK);
    }

    // 스터디룸 멤버 리스트
    @GetMapping("/study/studyRoom/Member/{studyId}")
    public ResponseEntity<List<StudyVO>> viewStudyMem(@PathVariable int studyId) {
        System.out.println("Study Id : " + studyId);
        StudyDAO dao = new StudyDAO();
        List<StudyVO> list = dao.getStudyMem(studyId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 스터디룸 일정 리스트
    @GetMapping("/study/studyRoom/Schedule/{studyId}")
    public ResponseEntity<List<ScheduleVO>> viewStudySc(@PathVariable int studyId) {
        System.out.println("Study Id : " + studyId);
        ScheduleDAO dao = new ScheduleDAO();
        List<ScheduleVO> list = dao.getStudySc(studyId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //스터디 작성
    @PostMapping("/study/create")
    public ResponseEntity<Boolean> studyRegister(@RequestBody Map<String, String> regData) throws ParseException {
        String getUserId = regData.get("userId");
        int UserId = Integer.parseInt(getUserId);
        String getStudyName = regData.get("studyName");
        String getstudyCategory = regData.get("studyCategory");

        String getstudyUserLimit = regData.get("studyUserLimit");
        int studyUserLimit = Integer.parseInt(getstudyUserLimit);

        String getstudyChatUrl = regData.get("studyChatUrl");
        String getstudyIntro = regData.get("studyIntro");
        String getstudyContent = regData.get("studyContent");

        String getstudyDeadline = regData.get("studyDeadline");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date studyDeadline = format.parse(getstudyDeadline);
        java.sql.Date sqlDate = new java.sql.Date(studyDeadline.getTime());

        String getstudyProfile = regData.get("studyProfile");
        List<String> getTagList = Arrays.asList(regData.get("tagName").split(","));
        StudyDAO dao = new StudyDAO();
        boolean isTrue = dao.studyInsert(UserId, getStudyName, getstudyCategory, studyUserLimit, getstudyChatUrl, getstudyIntro, getstudyContent, sqlDate, getstudyProfile, getTagList);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    //스터디 일정 작성
    @PostMapping("/study/studyRoom/Schedule/{studyId}")
    public ResponseEntity<Boolean> scheduleRegister(@RequestBody Map<String, String> regData) throws ParseException {
        String getUserId = regData.get("userId");
        int UserId = Integer.parseInt(getUserId);

        String getStudyId = regData.get("studyId");
        int StudyId = Integer.parseInt(getStudyId);

        String getstudyScDate = regData.get("studyScDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date studyScDate = format.parse(getstudyScDate);
        java.sql.Date sqlDate = new java.sql.Date(studyScDate.getTime());

        String getstudyScContent = regData.get("studyScContent");

        String getstudyScUserLimit = regData.get("studyScUserLimit");
        int studyScUserLimit = Integer.parseInt(getstudyScUserLimit);

        ScheduleDAO dao = new ScheduleDAO();
        boolean isTrue = dao.scheduleInsert(UserId, StudyId, sqlDate, getstudyScContent, studyScUserLimit);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    @PostMapping("/study/studyRoom/Schedule/MemberInser")
    public ResponseEntity<Boolean> scheduleMemRegister(@RequestBody Map<String, String> regData) throws ParseException {
        String getStudyScId = regData.get("studyScId");
        int studyScId = Integer.parseInt(getStudyScId);

        String getUserId = regData.get("userId");
        int userId = Integer.parseInt(getUserId);

        ScheduleDAO dao = new ScheduleDAO();
        boolean isTrue = dao.scheduleMemInsert(studyScId, userId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    @PostMapping("/study/studyRoom/Schedule/MemberDelete")
    public ResponseEntity<Boolean> scheduleMemDelete(@RequestBody Map<String, String> regData) throws ParseException {
        String getStudyScId = regData.get("studyScId");
        int studyScId = Integer.parseInt(getStudyScId);

        String getUserId = regData.get("userId");
        int userId = Integer.parseInt(getUserId);

        ScheduleDAO dao = new ScheduleDAO();
        boolean isTrue = dao.scheduleMemDelete(studyScId, userId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }



}
