package com.project.controller;

import com.project.dto.*;
import com.project.service.*;
import com.project.util.BoardWriteInfoDto;
import com.project.util.CommentWriterDto;
import com.project.util.ImageDto;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class BoardController {
    @Autowired
    CommentService commentService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardTagService boardTagService;
    @Autowired
    TagService tagService;
    @Autowired
    FileService fileService;


    @GetMapping(value = "boards/{board_id}*")
    public String getBoard (Model model, @PathVariable("board_id") int board_id, @RequestParam("category") int category){
        int total = commentService.getTotal(board_id);
        PagingVo pagingVo = new PagingVo(1,30, total);

        model.addAttribute("tags", getTags(board_id));
        model.addAttribute("commentTotal", commentService.getTotal(board_id));
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("board", boardService.getBoard(board_id));
        model.addAttribute("pagingVo", pagingVo);
        return "board";
    }

    @PostMapping(value = "boards/image")
    @ResponseBody
    public Map<String, Object> addImageToEditor(ImageDto imageDto, Model model) throws IOException {
        MultipartFile file = imageDto.getImage()[0];

        String originalName = file.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        String uuid = UUID.randomUUID().toString();

        String uploadPath = "C:\\springTest";
        String uuidName = uuid + "_" + fileName;
        String saveFileName = uploadPath + File.separator + uuidName;
        Path savePath = Paths.get(saveFileName);
        file.transferTo(savePath);

        fileService.addFile(new FileDto(0, 0, saveFileName, uuidName , file.getContentType(), Long.toString(file.getSize())));

        Map<String, Object> result = new HashMap<>();
        result.put("name", uuidName);
        return result;
    }

    @PostMapping(value = "boards")
    @ResponseBody
    public String writeBoard(BoardWriteInfoDto boardWriteInfoDto) {
        int categoryID = boardWriteInfoDto.getCategoryID();
        String title = boardWriteInfoDto.getTitle();
        String content = boardWriteInfoDto.getContent();
        String date = boardWriteInfoDto.getDate();
        boardService.addBoard(new BoardDto(0, categoryID, title, content, date, 0));
        List<String> resultImageList = boardWriteInfoDto.getImage();
        for(int i=0;i<resultImageList.size();i++){
            int board_id = boardService.getLastBoardID();
            fileService.updateBoardId(board_id, resultImageList.get(i));
        }
        return "boardWrite";
    }

    @PutMapping(value = "boards")
    @ResponseBody
    public String updateBoard(BoardWriteInfoDto boardWriteInfoDto) {

        return "boardWrite";
    }

    @GetMapping(value="board-write")
    public String writeBoardPage(){
        return "boardWrite";
    }


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

    public List<TagDto> getTags(int board_id){
        List<BoardTagDto> boardTagDtos = boardTagService.getBoardTag(board_id);
        List<TagDto> tags = tagService.getBoardTag(boardTagDtos);
        return tags;
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

    private void printf(String a){
        System.out.println(a);
    }
}
