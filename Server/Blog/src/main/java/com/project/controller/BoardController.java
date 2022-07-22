package com.project.controller;

import com.project.dto.CommentDto;
import com.project.service.BoardService;
import com.project.service.CategoryService;
import com.project.service.CommentService;
import com.project.util.CommentWriterInfo;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {
    @Autowired
    CommentService commentService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardService boardService;


    @GetMapping(value = "boards/{board_id}*")
    public String board (Model model, @PathVariable("board_id") int board_id, @RequestParam("category") int category){

        int total = commentService.getTotal(board_id);
        PagingVo pagingVo = new PagingVo(1,30, total);

        model.addAttribute("comments", commentService.getCommentPaging(pagingVo, board_id));
        model.addAttribute("commentTotal", commentService.getTotal(board_id));
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("board", boardService.getBoard(board_id));
        model.addAttribute("pagingVo", pagingVo);
        return "board";
    }

    @ResponseBody
    @PostMapping(value="boards/{board_id}/comment")
    public Map<String, Object> commentWrite(@PathVariable("board_id") int board_id, CommentDto commentdto){
        commentService.addComment(commentdto);
        int total = commentService.getTotal(board_id);
        PagingVo pagingVo = new PagingVo(1, 30, total);
        pagingVo.setNowPage(pagingVo.getLastPage());

        List<CommentDto> ret = commentService.getCommentPaging(pagingVo, board_id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("comments", ret);
        result.put("pagingVo", pagingVo);
        return result;
    }

    @ResponseBody
    @GetMapping(value="boards/{board_id}/comment*")
    public Map<String, Object> getCommentPaging(@PathVariable("board_id") int board_id, @RequestParam("page") int nowPage){
        Map<String, Object> result = getCommentResultMapObject(nowPage, board_id);
        return result;
    }

    @ResponseBody
    @DeleteMapping(value="boards/{board_id}/comment/{comment_id}*")
    public Map<String, Object> getCommentDelete(@PathVariable("comment_id") int comment_id,
                                                @PathVariable("board_id") int board_id,
                                                @RequestParam("page") int nowPage){
        System.out.println("넘어온 인자 page : " + nowPage + " board_id : " + board_id + " comment_id : " + comment_id);
        int numberOfChild = commentService.getCommentCountByParent(comment_id);
        if(numberOfChild == 0) commentService.deleteComment(comment_id); // 임시로 해놓자..
        Map<String, Object> result = getCommentResultMapObject(nowPage, board_id);
        return result;
    }

    @ResponseBody
    @PutMapping(value="boards/{board_id}/comment/{comment_id}*")
    public Map<String, Object> getCommentUpdate(@PathVariable("comment_id") int comment_id,
                                                @PathVariable("board_id") int board_id,
                                                @RequestParam("page") int nowPage, CommentDto commentDto){

        commentService.updateComment(commentDto);
        Map<String, Object> result = getCommentResultMapObject(nowPage, board_id);
        return result;
    }

    @ResponseBody
    @PostMapping(value="/comment/{comment_id}/writerInfo")
    public Map<String, Object> checkCommentWriterInfo(@PathVariable("comment_id") int comment_id, CommentWriterInfo commentWriterInfo){
        System.out.println(commentWriterInfo.getId() + " 와 " + commentWriterInfo.getPassword() + " 와 아이디 : " + comment_id);
        int res = commentService.checkCommentWriterInfo(commentWriterInfo, comment_id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("checkResult", res);
        return result;
    }

    private Map<String,Object> getCommentResultMapObject(int nowPage, int board_id){
        int total = commentService.getTotal(board_id);
        PagingVo pagingVo = new PagingVo(nowPage, 30, total);

        List<CommentDto> ret = commentService.getCommentPaging(pagingVo, board_id);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("comments", ret);
        result.put("pagingVo", pagingVo);
        return result;
    }
}
