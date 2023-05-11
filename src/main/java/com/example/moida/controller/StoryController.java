package com.example.moida.controller;

import com.example.moida.dao.StoryDAO;
import com.example.moida.dao.StudyDAO;
import com.example.moida.vo.StoryVO;
import com.example.moida.vo.StudyVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StoryController {
    @GetMapping("/story")
    public ResponseEntity<List<StoryVO>> storylist() {
        StoryDAO dao = new StoryDAO();
        List<StoryVO> list = dao.StorySelect();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
