package com.example.moida.controller;

import com.example.moida.dao.*;

import com.example.moida.vo.CommentVO;
import com.example.moida.vo.StoryCommentVO;
import com.example.moida.vo.StoryVO;
import com.example.moida.vo.StudyVO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class StoryController {

    // Story 전체 리스트 조회
    @GetMapping("/story")
    public ResponseEntity<List<StoryVO>> storylist(@RequestParam(value = "lastId", required = false) Integer lastId) {
        StoryDAO storyDAO = new StoryDAO();
        List<StoryVO> list;
        if (lastId == null) { // lastId가 없는 처음 데이터에는 132개 그 다음 데이터는 lastId값 기준으로 120개씩 보내기
            list = storyDAO.StoryVOList();
        } else {
            list = storyDAO.StoryVOList(lastId);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 스토리 view increase (조회수)
    @PostMapping("/story/view")
    public ResponseEntity<Boolean> storyViewIncrease(@RequestParam(value="storyId") int storyId) {
        System.out.println(storyId);
        StoryDAO dao = new StoryDAO();
        boolean result = dao.viewIncrease(storyId);
        if (result) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Story{userId} 게시글
    @GetMapping("/story/{storyId}")
    public ResponseEntity<StoryVO> viewStory(@PathVariable int storyId) throws SQLException  {
        System.out.println("Story Id : " + storyId);
        StoryDAO storyDAO = new StoryDAO();
        StoryCommentDAO StoryCommentDAO = new StoryCommentDAO();
        StoryVO story = storyDAO.getStoryById(storyId);
        List<StoryCommentVO> comments = StoryCommentDAO.getCommentsByStoryId(storyId);
        story.setComments(comments);

        return new ResponseEntity<>(story, HttpStatus.OK);
    }


    // Story 작성 (Insert)
    @PostMapping("/story/post/insert")
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
    @PostMapping("/story/comment/insert")
    public ResponseEntity<Boolean> commentRegister(@RequestBody Map<String, String> regData) {
        int getUserId = Integer.parseInt(regData.get("userId"));
        int getStoryId = Integer.parseInt(regData.get("storyId"));
        int getParentId = regData.get("parentId") != null ? Integer.parseInt(regData.get("parentId")) : 0;
        String getContents = regData.get("contents");
        StoryCommentDAO dao = new StoryCommentDAO();
        boolean insertResult;
        if (getParentId < 1) {
            insertResult = dao.storyCommentInsert(getUserId, getStoryId, getContents);
        } else { // parentId 가 있을 때 ( 대댓글 등록 )
            insertResult = dao.storyCommentInsert(getUserId, getStoryId, getParentId, getContents);
        }
        if (insertResult) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(insertResult, HttpStatus.OK);
    }


    // comment 수정
    @PostMapping("/story/comment/update")
    public ResponseEntity<Boolean> commentModifier(@RequestBody Map<String, String> modData) {
        int getCommentId = Integer.parseInt(modData.get("commentId"));
        String getContents = modData.get("contents");
        StoryCommentDAO dao = new StoryCommentDAO();
        boolean updateResult = dao.storyCommentUpdate(getCommentId, getContents);
        if (updateResult) System.out.println(HttpStatus.OK);
        return new ResponseEntity<>(updateResult, HttpStatus.OK);
    }


    // comment 삭제
    @PostMapping("/story/comment/delete")
    public ResponseEntity<Boolean> commentDelete(@RequestParam(value = "commentId") int commentId) {
        StoryCommentDAO dao = new StoryCommentDAO();
        boolean isTrue = dao.storyCommentDelete(commentId);
        return new ResponseEntity<>(isTrue, HttpStatus.OK);
    }




}
