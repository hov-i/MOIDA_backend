package com.example.moida.controller;

import com.example.moida.dao.MailDAO;
import com.example.moida.dao.UserInfoDAO;
import com.example.moida.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController

public class UserController {
    //회원가입
    @PostMapping("/new")
    public ResponseEntity<Boolean> memberRegister(@RequestBody Map<String, String> regData) {
        String userName = regData.get("userName");
        String pw = regData.get("pw");
        String pwConfirm = regData.get("pwConfirm");
        String email = regData.get("email");
        String phone = regData.get("phone");
        String nickname = regData.get("nickname");
        UserInfoDAO dao = new UserInfoDAO();

        // 비밀번호 확인과 동일하지 않으면 안 받음
        if (!pw.equals(pwConfirm)) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

        boolean isTrue = dao.memberRegister(userName, pw, pwConfirm, email, phone, nickname);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> memberLogin(@RequestBody Map<String, String> loginData) {
        String userName = loginData.get("userName");
        String pw = loginData.get("pw");
        UserInfoDAO dao = new UserInfoDAO();
        boolean result = dao.loginCheck(userName, pw);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // GET : 가입 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> memberCheck(@RequestParam String userName) {
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.regMemberCheck(userName);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // 이메일 보내기
    @Autowired
    private MailDAO mailDAO;

    @ApiOperation(value = "임시 비밀번호 전송", notes = "전송한 임시 비밀번호를 반환한다.", response = Map.class)
    @PostMapping("/sendmail")
    public ResponseEntity<Map<String, Object>> sendMail(@RequestBody Map<String, String> map) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        String temporaryPassword = mailDAO.makeCode(10); // 임시 비밀번호 생성

        boolean isEmailSent = Boolean.parseBoolean(mailDAO.sendMail(map.get("type"), map.get("email")));
        if (isEmailSent) {
            resultMap.put("message", "SUCCESS");
            resultMap.put("temporaryPassword", temporaryPassword);
        } else {
            resultMap.put("message", "FAIL");
        }
        status = HttpStatus.ACCEPTED;

        return new ResponseEntity<>(resultMap, status);
    }



    // 비밀번호 찾기





    //MY PAGE
    // 프로필 확인
//    @GetMapping("/myInfo")
//    public ResponseEntity<UserInfoVO> getMyInfo(@RequestParam String userId) {
//        System.out.println("USER_ID : " + userId);
//        UserInfoVO dao = new UserInfoVO();
//        List<UserInfoVO> list = dao.getMyInfo(userId);
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }



    // 프로필 변경
    // POST : 폰 번호 변경
    @PostMapping("/phone")
    public ResponseEntity<Boolean> updatePhone(@RequestBody Map<String, String> phoneData) {
        String userName = phoneData.get("userName");
        String newPhone = phoneData.get("newPhone");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updatePhone(userName, newPhone);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 이메일 변경
    @PostMapping("/email")
    public ResponseEntity<Boolean> updateEmail(@RequestBody Map<String, String> phoneData) {
        String userId = phoneData.get("userId");
        String newEmail = phoneData.get("newEmail");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updateEmail(userId, newEmail);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 비번 변경
    @PostMapping("/pw")
    public ResponseEntity<Boolean> updatePassword(@RequestBody Map<String, String> phoneData) {
        String userId = phoneData.get("userId");
        String pw = phoneData.get("pw");
        String newPw = phoneData.get("newPw");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updatePassword(userId, pw, newPw);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 닉네임 변경
    @PostMapping("/nickname")
    public ResponseEntity<Boolean> updateNickname(@RequestBody Map<String, String> phoneData) {
        String userId = phoneData.get("userId");
        String newNickname = phoneData.get("newNickname");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updateNickname(userId, newNickname);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }


    // POST : 회원 탈퇴(the last)
    @PostMapping("/del")
    public ResponseEntity<Boolean> memberDelete(@RequestBody Map<String, String> delData) {
        String userId = delData.get("userId");
        String pw = delData.get("pw");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.memberDelete(userId, pw);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : test
    @PostMapping("/myPage")
    public ResponseEntity<Boolean> myPage(@RequestBody Map<String, String> userName) {
        System.out.println(userName);
//        String userName = delData.get("userName");
//        String pw = delData.get("pw");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = true;
        //dao.memberDelete(userName, pw);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }
}
