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
        String type = map.get("type");
        String email = map.get("email");
        String username = map.get("username");
        String phone = map.get("phone");

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        // phone, username, email이 모두 일치하는지 확인
        boolean isMatch = mailDAO.checkPhoneUsernameAndEmailMatch(username, phone, email);
        if (!isMatch) {
            // phone, username, email이 일치하지 않으면 실패 응답 반환
            resultMap.put("message", "FAIL");
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(resultMap, status);
        }

        String tempPw = mailDAO.makeCode(10); // 임시 비밀번호 생성

        boolean isEmailSent = mailDAO.sendMail(type, email, tempPw, phone);
        if (isEmailSent) {
            resultMap.put("message", "SUCCESS");
            resultMap.put("tempPw", tempPw);

            // 사용자의 비밀번호를 임시 비밀번호로 변경
            boolean isChanged = mailDAO.changePasswordToTemporary(username, tempPw);
            if (!isChanged) {
                resultMap.put("message", "비밀번호 변경에 실패했습니다.");
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                return new ResponseEntity<>(resultMap, status);
            } else {
                resultMap.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            }
        } else {
            resultMap.put("message", "메일 보내는 데 실패했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(resultMap, status);
        }

        status = HttpStatus.OK;
        return new ResponseEntity<>(resultMap, status);
    }





    @ApiOperation(value = "아이디 찾기", notes = "전달된 이메일과 전화번호로 사용자의 아이디를 찾아 이메일로 전송합니다.", response = Map.class)
    @PostMapping("/findId")
    public ResponseEntity<Map<String, Object>> findId(@RequestBody Map<String, String> map) {
        String email = map.get("email");
        String phone = map.get("phone");

        // 이메일과 폰 번호의 일치 여부 확인
        boolean isMatch = mailDAO.checkEmailAndPhoneMatch(email, phone);
        if (isMatch) {
            // 폰 번호로 사용자 아이디 조회
            String username = mailDAO.getUsernameByPhone(phone);
            if (username != null) {
                // 메일 보내기
                boolean isEmailSent = mailDAO.sendMail("findId", email, null, phone);
                if (isEmailSent) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("message", "SUCCESS");
                    resultMap.put("username", username);
                    return new ResponseEntity<>(resultMap, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}