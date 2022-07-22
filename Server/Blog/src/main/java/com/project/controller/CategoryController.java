package com.project.controller;

import com.example.blog.HelloServlet;
import com.project.dto.CommentDto;
import com.project.service.BoardService;
import com.project.service.CategoryService;
import com.project.service.CommentService;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;


@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BoardService boardService;

    @RequestMapping("main")
    public String func(Model model){
        model.addAttribute("");
        model.addAttribute("categories", categoryService.getAllCategory());
        return "category";
    }

    @GetMapping(value = "category/{category}/boards*")
    public String category(Model model, @PathVariable("category") int category_id, HttpServletRequest rq){
        int nowPage = 1;
        int total = boardService.getTotal(category_id);
        if(rq.getParameter("page") != null) nowPage = Integer.parseInt(rq.getParameter("page"));

        PagingVo pagingVo = new PagingVo(nowPage, 10, total);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("currentCategory", category_id);
        model.addAttribute("boards", boardService.getBoardPaging(pagingVo, category_id));
        model.addAttribute("pagingVo", pagingVo);
        return "category";
    }
}
