package com.project.controller;

import com.project.dto.CommentDto;
import com.project.service.CommentService;
import com.project.util.CommentWriterDto;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @ResponseBody
    @GetMapping(value="boards/{board_id}/comment*")
    public Map<String, Object> getCommentPaging(@PathVariable("board_id") int board_id, @RequestParam("page") int nowPage){
        Map<String, Object> result = getCommentResultMapObject(nowPage, board_id);
        return result;
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
    @DeleteMapping(value="boards/{board_id}/comment/{comment_id}*")
    public Map<String, Object> commentDelete(@PathVariable("comment_id") int comment_id,
                                             @PathVariable("board_id") int board_id,
                                             @RequestHeader("id") String id,
                                             @RequestHeader("password") String password){

        CommentWriterDto commentWriterInfo = new CommentWriterDto(URLDecoder.decode(id), URLDecoder.decode(password));
        int res = commentService.checkCommentWriterInfo(commentWriterInfo, comment_id);
        int numberOfChild = commentService.getCommentCountByParent(comment_id);
        if(numberOfChild == 0 && res == 1) commentService.deleteComment(comment_id);
        Map<String, Object> result = getCommentResultMapObject(1, board_id);
        result.put("resultCode", res);
        return result;
    }


    @ResponseBody
    @PutMapping(value="boards/{board_id}/comment/{comment_id}*")
    public Map<String, Object> commentUpdate(   @PathVariable("comment_id") int comment_id,
                                                @PathVariable("board_id") int board_id,
                                                @RequestParam("page") int nowPage,
                                                @RequestHeader("id") String id,
                                                @RequestHeader("password") String password,
                                                @RequestHeader("content") String content){

        CommentWriterDto commentWriterDto = new CommentWriterDto(URLDecoder.decode(id), URLDecoder.decode(password));
        int res = commentService.checkCommentWriterInfo(commentWriterDto, comment_id);;
        if(res == 1) commentService.updateComment(new CommentDto(comment_id,0,0,0,"", "", content = URLDecoder.decode(content), ""));
        Map<String, Object> result = getCommentResultMapObject(nowPage, board_id);
        result.put("resultCode", res);
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
