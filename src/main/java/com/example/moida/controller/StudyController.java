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
        System.out.println(list.toString());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 스터디룸 멤버 OK
    @GetMapping("/study/studyRoom/memIsOk/{studyId}/{userId}")
    public ResponseEntity<Boolean> StudyMemisOk(@PathVariable int studyId, @PathVariable int userId) {
        System.out.println("Study Id : " + studyId);
        StudyDAO dao = new StudyDAO();
        boolean isOk = dao.StudyMemOk(studyId, userId);
        System.out.println(isOk);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    // 스터디룸 일정 참가 OK
    @GetMapping("/study/studyRoom/scIsOk/{studyScId}/{userId}")
    public ResponseEntity<Boolean> StudyScisOk(@PathVariable int studyScId, @PathVariable int userId) {
        System.out.println("Study Id : " + studyScId);
        StudyDAO dao = new StudyDAO();
        boolean isOk = dao.StudyScisOk(studyScId, userId);
        System.out.println(isOk);
        return new ResponseEntity<>(isOk, HttpStatus.OK);
    }

    // 스터디룸 일정 리스트
    @GetMapping("/study/studyRoom/Schedule/{studyId}")
    public ResponseEntity<List<ScheduleVO>> viewStudySc(@PathVariable int studyId) {
        System.out.println("Study Id : " + studyId);
        ScheduleDAO dao = new ScheduleDAO();
        List<ScheduleVO> list = dao.getStudySc(studyId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 스터디룸 나의 일정 리스트
    @GetMapping("/study/mySc/{userId}")
    public ResponseEntity<List<ScheduleVO>> viewStudyMySc(@PathVariable int userId) {
        System.out.println("UserId : " + userId);
        ScheduleDAO dao = new ScheduleDAO();
        List<ScheduleVO> list = dao.getStudyMySc(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //스터디 페이지 나의 스터디
    @GetMapping("/study/myStudyList/{userId}")
    public ResponseEntity<List<StudyVO>> viewMyStudy(@PathVariable int userId) throws SQLException {
        System.out.println("UserId : " + userId);
        StudyDAO dao = new StudyDAO();
        List<StudyVO> list = dao.myStudySelect(userId);
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

    //스터디 일정 참가
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

    //스터디 일정 참가 취소
    @DeleteMapping("/study/studyRoom/Schedule/MemberDelete")
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

    //스터디 일정 멤버 리스트
    @GetMapping("/study/studyRoom/Schedule/MemberGet/{studyScId}")
    public ResponseEntity<List<ScheduleVO>> viewStudyScMem(@PathVariable int studyScId) {
        System.out.println("StudyScId : " + studyScId);
        ScheduleDAO dao = new ScheduleDAO();
        List<ScheduleVO> list = dao.getStudyScMem(studyScId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    //스터디 일정 삭제
    @DeleteMapping("/study/studyRoom/ScheduleDelete")
    public ResponseEntity<Boolean> scheduleDelete(@RequestBody Map<String, String> regData) throws ParseException {
        String getStudyScId = regData.get("studyScId");
        int studyScId = Integer.parseInt(getStudyScId);
        System.out.println("스터디 일정 삭제");
        ScheduleDAO dao = new ScheduleDAO();
        boolean isTrue = dao.scheduleDelete(studyScId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }
    // 스터디 권한 넘기기
    @PatchMapping("/study/studyRoom/Member/MemberNext/{studyId}")
    public ResponseEntity<Boolean> studyMemPatch(@PathVariable int studyId, @RequestBody Map<String, String> memId) throws ParseException {
        String getMemId = memId.get("memId");
        int MemId = Integer.parseInt(getMemId);

        StudyDAO dao = new StudyDAO();
        boolean isTrue = dao.studyMgrNext(studyId, MemId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // 스터디 강퇴
    @DeleteMapping("/study/studyRoom/Member/MemberDelete")
    public ResponseEntity<Boolean> studyMemDelete(@RequestBody Map<String, String> regData) throws ParseException {
        String getStudyId = regData.get("studyId");
        int studyId = Integer.parseInt(getStudyId);

        String getUserId = regData.get("userId");
        int userId = Integer.parseInt(getUserId);
        System.out.println("StudyID : " + studyId);
        System.out.println("userId : " + userId);

        StudyDAO dao = new StudyDAO();
        boolean isTrue = dao.studyMemDelete(studyId, userId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // 스터디 가입
    @PostMapping("/study/studyRoom/Main/studyInsert")
    public ResponseEntity<Boolean> studyJoin(@RequestBody Map<String, String> regData) throws ParseException {
        String getStudyId = regData.get("studyId");
        int studyId = Integer.parseInt(getStudyId);

        String getUserId = regData.get("userId");
        int userId = Integer.parseInt(getUserId);

        StudyDAO dao = new StudyDAO();
        boolean isTrue = dao.studyJoinInsert(studyId, userId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // 스터디 나가기
    @DeleteMapping("/study/studyRoom/Main/studyOut")
    public ResponseEntity<Boolean> studyOut(@RequestBody Map<String, String> regData) throws ParseException {
        String getStudyId = regData.get("studyId");
        int studyId = Integer.parseInt(getStudyId);

        String getUserId = regData.get("userId");
        int userId = Integer.parseInt(getUserId);
        System.out.println("StudyID : " + studyId);
        System.out.println("userId : " + userId);

        StudyDAO dao = new StudyDAO();
        boolean isTrue = dao.studyOut(studyId, userId);
        if (isTrue) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

}
