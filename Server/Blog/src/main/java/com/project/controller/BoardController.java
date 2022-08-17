package com.project.controller;

import com.project.dto.*;
import com.project.service.*;
import com.project.util.data.BoardWriteInfoDto;
import com.project.util.data.ImageDto;
import com.project.util.data.PagingVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

    private final Logger log = LoggerFactory.getLogger(getClass());

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
    public Map<String, Object> saveImage(ImageDto imageDto, Model model) throws IOException {
        MultipartFile file = imageDto.getImage()[0];

        String originalName = file.getOriginalFilename();
        String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
        System.out.println("fileName : " + fileName);
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

    @DeleteMapping(value="boards/{board_id}")
    @ResponseBody
    public Map<String, Object> deleteBoard(@PathVariable("board_id") int board_id){
        commentService.deleteByBoardID(board_id);
        boardTagService.deleteBoardByBoardID(board_id);
        boardService.deleteBoard(board_id);
        List<FileDto> list = fileService.getFileByBoardID(board_id);
        for(int i=0;i< list.size();i++){
            String fileName = list.get(i).getName();
            fileService.updateBoardId(0, fileName);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", 1);
        return result;
    }

    @PostMapping(value = "boards")
    @ResponseBody
    public Object writeOrUpdateBoard(@Valid @ModelAttribute BoardWriteInfoDto boardWriteInfoDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return bindingResult.getAllErrors();
        }

        BoardDto boardDto = convertBoardWriteInfoDtoToBoardDto(boardWriteInfoDto);
        String type = boardWriteInfoDto.getType();

        if(type.equals("write")) boardService.addBoard(boardDto);
        else if(type.equals("update")) boardService.updateBoard(boardDto);

        int board_id = boardService.getLastBoardID();
        if(type.equals("update")) {
            board_id = boardWriteInfoDto.getBoard_id();
            boardTagService.deleteBoardByBoardID(board_id);
        }

        List<String> tagNames = boardWriteInfoDto.getTag();
        List<TagDto> tagDtoToAdd = tagService.getTagsByTagNames(tagNames);
        for(int i=0;i<tagDtoToAdd.size();i++){
            boardTagService.addBoardTag(new BoardTagDto(tagDtoToAdd.get(i).getTag_id(), board_id));
        }

        List<String> resultImageList = new ArrayList<>();
        if(boardWriteInfoDto.getImage() != null) resultImageList = boardWriteInfoDto.getImage();
        for(int i=0;i<resultImageList.size();i++){
            fileService.updateBoardId(board_id, resultImageList.get(i));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", 1);
        return result;
    }

    @GetMapping(value="board-write-page")
    public String writeBoardPage(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("board_id", 0);
        model.addAttribute("content", "");
        model.addAttribute("type","write");
        return "boardWrite";
    }

    @GetMapping(value = "board-update-page/{board_id}")
    public String updateBoardPage(@PathVariable("board_id")int board_id, Model model){
        BoardDto boardDto = boardService.getBoard(board_id);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("boardID", board_id);
        model.addAttribute("type", "update");
        model.addAttribute("title", boardDto.getTitle());
        model.addAttribute("content", boardDto.getContent());
        model.addAttribute("categoryID",boardDto.getCategory_id());
        model.addAttribute("tags", TagNameString(getTags(board_id)));
        return "boardWrite";
    }

    public List<TagDto> getTags(int board_id){
        List<BoardTagDto> boardTagDtos = boardTagService.getBoardTag(board_id);
        List<TagDto> tags = tagService.getTagsByBoardTags(boardTagDtos);
        return tags;
    }

    public String TagNameString(List<TagDto> tag){
        String tagString = "";
        for(int i=0;i<tag.size();i++) {
            tagString += tag.get(i).getName();
            if(i != tag.size()-1) tagString += " ";
        }
        return tagString;
    }

    public BoardDto convertBoardWriteInfoDtoToBoardDto(BoardWriteInfoDto boardWriteInfoDto){
        int boardID = boardWriteInfoDto.getBoard_id();
        int categoryID = boardWriteInfoDto.getCategoryID();
        String title = boardWriteInfoDto.getTitle();
        String content = boardWriteInfoDto.getContent();
        String date = boardWriteInfoDto.getDate();

        BoardDto boardDto = new BoardDto(boardID, categoryID, title, content, date, 0);
        return boardDto;
    }
}
