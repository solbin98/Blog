package com.project.controller;



import javax.servlet.http.HttpSession;
import com.project.dto.BoardDto;
import com.project.dto.BoardTagDto;
import com.project.dto.TagDto;
import com.project.service.*;
import com.project.util.data.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BoardTagService boardTagService;

    @RequestMapping("/main")
    public String func(Model model, HttpSession session){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "category";
    }

    @GetMapping(value = "category/{category}/boards*")
    public String category(Model model, @PathVariable("category") int category_id, HttpServletRequest rq){
        int nowPage = 1;
        int total = boardService.getTotal(category_id);
        if(rq.getParameter("page") != null) nowPage = Integer.parseInt(rq.getParameter("page"));
        PagingVo pagingVo = new PagingVo(nowPage, 10, total);

        List<BoardDto> boards = boardService.getBoardPaging(pagingVo, category_id);

        Map<String, List<TagDto>> tags = getTagMap(boards);

        model.addAttribute("tags", tags);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("currentCategory", category_id);
        model.addAttribute("boards", boards);
        model.addAttribute("pagingVo", pagingVo);
        return "category";
    }

    @GetMapping(value = "category")
    @ResponseBody
    public Map<String,Object> getCategories(){
        Map<String, Object> ret = new HashMap<>();
        ret.put("categories", categoryService.getAllCategory());
        return ret;
    }

    public List<TagDto> getTags(int board_id){
        List<BoardTagDto> boardTagDtos = boardTagService.getBoardTag(board_id);
        List<TagDto> tags = tagService.getTagsByBoardTags(boardTagDtos);
        return tags;
    }

    public Map<String, List<TagDto>> getTagMap(List<BoardDto> boards){
        Map<String, List<TagDto>> tags = new HashMap<>();
        for(int i=0;i<boards.size();i++){
            int boardId = boards.get(i).getBoard_id();
            String boardID = Integer.toString(boardId);
            tags.put(boardID, getTags(boardId));
        }
        return tags;
    }
}
