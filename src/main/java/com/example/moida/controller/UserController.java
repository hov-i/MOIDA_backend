package com.example.moida.controller;

import com.example.moida.dao.UserInfoDAO;
import com.example.moida.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 아이디, 닉네임 중복 확인
//    @PostMapping("/checkUsername")
//    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
//        boolean isDuplicate = userInfoDAO.checkUsername(username);
//        return new ResponseEntity<>(isDuplicate, HttpStatus.OK);
//    }
//
//    @PostMapping("/checkNickname")
//    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
//        boolean isDuplicate = userInfoDAO.checkNickname(nickname);
//        return new ResponseEntity<>(isDuplicate, HttpStatus.OK);
//    }


    //로그인
    @PostMapping("/login")
    public ResponseEntity<UserInfoVO> memberLogin(@RequestBody Map<String, String> loginData) {
        String userName = loginData.get("userName");
        String pw = loginData.get("pw");
        UserInfoDAO dao = new UserInfoDAO();
        UserInfoVO userInfo = dao.loginCheck(userName, pw);

        if (userInfo != null) {
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    // GET : 가입 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> memberCheck(@RequestParam String userName) {
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.regMemberCheck(userName);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }



    //MY PAGE
    // 프로필 확인
    @GetMapping("/myInfo/{userId}")
    public ResponseEntity<UserInfoVO> getProfile(@PathVariable String userId) {
        UserInfoDAO dao = new UserInfoDAO();
        UserInfoVO userInfo = dao.getMyInfo(userId);

        if (userInfo != null) {
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    // 프로필 변경
    // POST : 폰 번호 변경
    @PostMapping("/phone")
    public ResponseEntity<Boolean> updatePhone(@RequestBody Map<String, String> phoneData) {
        System.out.println(phoneData.get("userId"));
        System.out.println(phoneData.get("phone"));
        String userId = phoneData.get("userId");
        String phone = phoneData.get("phone");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updatePhone(userId, phone);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 이메일 변경
    @PostMapping("/email")
    public ResponseEntity<Boolean> updateEmail(@RequestBody Map<String, String> phoneData) {
        System.out.println(phoneData.get("userId"));
        System.out.println(phoneData.get("email"));
        String userId = phoneData.get("userId");
        String email = phoneData.get("email");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updateEmail(userId, email);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 비번 변경
    @PostMapping("/pw")
    public ResponseEntity<Boolean> updatePassword(@RequestBody Map<String, String> phoneData) {
        System.out.println(phoneData.get("userId"));
        System.out.println(phoneData.get("newPw"));
        String userId = phoneData.get("userId");
        String pw = phoneData.get("pw");
        String newPw = phoneData.get("newPw");
        UserInfoDAO dao = new UserInfoDAO();
        boolean isTrue = dao.updatePassword(userId, pw, newPw);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }

    // POST : 닉네임 변경
    @PostMapping("/nickname")
    public ResponseEntity<Boolean> updateNickname(@RequestBody Map<String, String> nicknameData) {
        System.out.println(nicknameData.get("userId"));
        System.out.println(nicknameData.get("nickname"));
        String userId = nicknameData.get("userId");
        String nickname = nicknameData.get("nickname");

        UserInfoDAO dao = new UserInfoDAO();
        boolean isUpdated = dao.updateNickname(userId, nickname);
        return ResponseEntity.ok(isUpdated);
    }

    // 이미지 url 업로드
    @PostMapping("/img")
    public ResponseEntity<Boolean> uploadImageURL(@RequestBody Map<String, String> imageURLData) {
        System.out.println(imageURLData.get("userId"));
        System.out.println(imageURLData.get("img"));
        String userId = imageURLData.get("userId");
        String img = imageURLData.get("img");

        UserInfoDAO dao = new UserInfoDAO();
        boolean isUpdated = dao.uploadImageURL(userId, img);
        return ResponseEntity.ok(isUpdated);
    }

    // 자기소개 업로드
    @PostMapping("/intro")
    public ResponseEntity<Boolean> uploadIntro(@RequestBody Map<String, String> uploadIntroData) {
        System.out.println(uploadIntroData.get("userId"));
        System.out.println(uploadIntroData.get("intro"));
        System.out.println(uploadIntroData.get("userId").getClass());
        String userId = uploadIntroData.get("userId");
        String intro = uploadIntroData.get("intro");

        UserInfoDAO dao = new UserInfoDAO();
        boolean isUpdated = dao.uploadIntro(userId, intro);
        return ResponseEntity.ok(isUpdated);
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
