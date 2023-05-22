package com.example.moida.controller;

import com.example.moida.dao.MailDAO;
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
public class MailController {
    @Autowired
    private MailDAO mailDAO;

    @ApiOperation(value = "임시 비밀번호 전송 및 변경", notes = "임시 비밀번호를 생성하여 이메일로 전송하고, 사용자의 비밀번호를 변경합니다.", response = Map.class)
    @PostMapping("/findPw")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> map) {
        System.out.println(map.get("type"));
        System.out.println(map.get("email"));
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        String tempPw = mailDAO.makeCode(10); // 임시 비밀번호 생성

        String username = map.get("username");
        String phone = map.get("phone");
        String email = map.get("email");

        // phone, username, email이 모두 일치하는지 확인
        boolean isMatch = mailDAO.checkPhoneUsernameAndEmailMatch(username, phone, email);
        if (!isMatch) {
            // phone, username, email이 일치하지 않으면 실패 응답 반환
            resultMap.put("message", "FAIL");
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(resultMap, status);
        }

        boolean isEmailSent = mailDAO.sendMail(map.get("type"), map.get("email"), map.get("phone"));
        if (isEmailSent) {
            resultMap.put("message", "SUCCESS");
            resultMap.put("tempPw", tempPw);
        } else {
            resultMap.put("message", "FAIL");
        }

        // 사용자의 비밀번호를 임시 비밀번호로 변경
        boolean isChanged = mailDAO.changePasswordToTemporary(username, tempPw);
        if (isChanged) {
            // 비밀번호 변경 성공
            status = HttpStatus.OK;
        } else {
            // 비밀번호 변경 실패
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }


    @PostMapping("/findId")
    public ResponseEntity<Map<String, Object>> findId(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String phone = request.get("phone");

        // email과 phone이 일치하는지 확인
        boolean isMatch = mailDAO.checkEmailAndPhoneMatch(email, phone);
        if (!isMatch) {
            // email과 phone이 일치하지 않는 경우
            Map<String, Object> response = new HashMap<>();
            response.put("message", "이메일과 전화번호가 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 아이디 찾기 로직 구현
        String username = mailDAO.getUsernameByPhone(phone);
        if (username == null) {
            // 아이디를 찾을 수 없는 경우
            Map<String, Object> response = new HashMap<>();
            response.put("message", "아이디를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        boolean isEmailSent = mailDAO.sendMail("findId", email, phone);
        if (isEmailSent) {
            // 이메일 전송 성공
            Map<String, Object> response = new HashMap<>();
            response.put("message", "아이디를 이메일로 전송하였습니다.");
            return ResponseEntity.ok(response);
        } else {
            // 이메일 전송 실패
            Map<String, Object> response = new HashMap<>();
            response.put("message", "이메일 전송에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}