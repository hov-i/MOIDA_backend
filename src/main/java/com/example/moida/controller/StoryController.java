package com.example.moida.controller;

import com.example.moida.dao.*;

import com.example.moida.vo.CommentVO;
import com.example.moida.vo.StoryVO;
import com.example.moida.vo.StudyVO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StoryController {

    // Story 전체 리스트 조회
    @GetMapping("/story")
    public ResponseEntity<List<StoryVO>> storylist() {
        StoryDAO dao = new StoryDAO();
        List<StoryVO> list = dao.StoryVOList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    // Story{userId} 게시글
    @GetMapping("/story/{userId}")
    public ResponseEntity<StoryVO> StoryList(@PathVariable int studyId, @PathVariable int storyId) {
        System.out.println("Story Id : " + storyId);
        StoryDAO storyDAO = new StoryDAO();
        CommentDAO commentDAO = new CommentDAO();
        StoryVO story = storyDAO.getStoryById(storyId);
        List<CommentVO> comments = commentDAO.getCommentsByPostId(storyId);

        story.setComments(comments);
        if (story.getStudyId() < 1 || !(studyId==story.getStudyId())) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(story, HttpStatus.OK);
    }


    // Story 작성 (Insert)
    @GetMapping("/story/post/insert")
    public ResponseEntity<Boolean> storyRegister(@RequestBody Map<String, String> regData) {
        int getUserId = Integer.parseInt(regData.get("userId"));
        int getStudyId = Integer.parseInt(regData.get("studyId"));
        String getTitle = regData.get("title");
        String getContents = regData.get("contents");
        String getImgUrl = regData.get("imgUrl");
        StoryDAO dao = new StoryDAO();
        boolean Result = dao.storyInsert(getUserId, getStudyId, getTitle, getContents, getImgUrl);
        if (Result) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(Result, HttpStatus.OK);
    }


    // Story 수정 (Update)
    @PostMapping("/story/post/update")
    public ResponseEntity<Boolean> storyModifier(@RequestBody Map<String, String> modData) {
        String getTitle = modData.get("title");
        String getContents = modData.get("contents");
        int getStoryId = Integer.parseInt(modData.get("storyId"));
        StoryDAO dao = new StoryDAO();
        boolean result = dao.storyUpdate(getTitle, getContents, getStoryId);
        if (result) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Story 삭제 (Delete)
    @PostMapping("/story/post/delete")
    public ResponseEntity<Boolean> storyDelete(@RequestBody Map<String, String> delData) {
        int storyId = Integer.parseInt(delData.get("storyId"));
        StoryDAO dao = new StoryDAO();
        boolean isTrue = dao.storyDelete(storyId);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }


    // comment 추가

    // comment 수정


}
