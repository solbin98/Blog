package com.project.controller;

import com.project.dto.CommentDto;
import com.project.service.CommentService;
import com.project.util.data.CommentWriterDto;
import com.project.util.data.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.util.*;

@Controller
public class CommentController {
    int commentNumberPerPage = 30;
    @Autowired
    CommentService commentService;
    @Autowired
    MessageSource messageSource;

    private final Locale locale = Locale.getDefault();

    @ResponseBody
    @GetMapping(value="boards/{board_id}/comment*")
    public Map<String, Object> getCommentPaging(@PathVariable("board_id") int board_id, @RequestParam("page") int nowPage){
        Map<String, Object> result = new HashMap<>();
        addCommentDataToMapObject(result, nowPage, board_id);
        result.put("resultCode", 1);
        return result;
    }

    @ResponseBody
    @PostMapping(value="boards/{board_id}/comment")
    public Object commentWrite(    @PathVariable("board_id") int board_id,
                                   @RequestParam(value = "page", required = false) int nowPage,
                                   @Valid @ModelAttribute CommentDto commentdto,
                                   BindingResult bindingResult){

        Map<String, Object> result = new HashMap<>();

        if(bindingResult.hasErrors()){
            String errorMessage = getErrorMessageFromObjectErrorList(bindingResult.getAllErrors());
            result.put("resultCode", 0);
            result.put("errorMessage", errorMessage);
            return result;
        }

        commentService.addComment(commentdto);
        if(commentdto.getParent() == 0){
            int lastCommentID = commentService.getLastCommentID();
            commentService.updateCommentOnlyParent(lastCommentID);
        }

        int total = commentService.getTotal(board_id);
        int lastPage = (int)Math.ceil(((double)total / (double)commentNumberPerPage));
        if(nowPage == 0) nowPage = lastPage;

        addCommentDataToMapObject(result, nowPage, board_id);
        result.put("resultCode", 1);
        return result;
    }

    @ResponseBody
    @DeleteMapping(value="boards/{board_id}/comment/{comment_id}*")
    public Map<String, Object> commentDelete(@PathVariable("comment_id") int comment_id,
                                             @PathVariable("board_id") int board_id,
                                             @RequestParam("page") int nowPage,
                                             @RequestHeader("id") String id,
                                             @RequestHeader("password") String password
    ){
        Map<String, Object> result = new HashMap<>();

        CommentWriterDto commentWriterInfo = new CommentWriterDto(URLDecoder.decode(id), URLDecoder.decode(password));
        int res = commentService.checkCommentWriterInfo(commentWriterInfo, comment_id);
        int numberOfChild = commentService.getCommentCountByParent(comment_id);
        String errorMessage = "";

        if(numberOfChild <= 1 && res == 1) commentService.deleteComment(comment_id);
        else {
            if(numberOfChild >= 2) errorMessage += messageSource.getMessage("CommentHasChildren", null, locale);
            if(res == 0)           errorMessage += messageSource.getMessage("CommentWriterInfo", null, locale);
        }

        addCommentDataToMapObject(result, nowPage, board_id);
        result.put("resultCode", res);
        result.put("errorMessage", errorMessage);
        return result;
    }

    @ResponseBody
    @PutMapping(value="boards/{board_id}/comment/{comment_id}*")
    public Object commentUpdate(   @PathVariable("comment_id") int comment_id,
                                                @PathVariable("board_id") int board_id,
                                                @RequestParam("page") int nowPage,
                                                @RequestHeader("id") String id,
                                                @RequestHeader("password") String password,
                                                @RequestHeader("content") String content){

        Map<String, Object> result = new HashMap<>();

        CommentWriterDto commentWriterDto = new CommentWriterDto(URLDecoder.decode(id), URLDecoder.decode(password));
        int res = commentService.checkCommentWriterInfo(commentWriterDto, comment_id);
        String errorMessage = "";

        if(res == 1) commentService.updateComment(new CommentDto(comment_id,0,0,0,"", "", content = URLDecoder.decode(content), ""));
        else errorMessage = messageSource.getMessage("CommentWriterCheck", null, locale);

        addCommentDataToMapObject(result, nowPage, board_id);
        result.put("resultCode", res);
        result.put("errorMessage", errorMessage);
        return result;
    }


    private void addCommentDataToMapObject(Map<String, Object> result, int nowPage, int board_id){
        int total = commentService.getTotal(board_id);
        PagingVo pagingVo = new PagingVo(nowPage, commentNumberPerPage, total);
        List<CommentDto> ret = commentService.getCommentPaging(pagingVo, board_id);
        result.put("comments", ret);
        result.put("pagingVo", pagingVo);
    }

    private String getErrorMessageFromObjectErrorList(List<ObjectError> list){
        String errorMessage = "";
        for(int i=0;i<list.size();i++)
            errorMessage += list.get(i).getDefaultMessage() + "\n";
        return errorMessage;
    }
}
