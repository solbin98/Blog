package com.project.controller;

import com.project.dto.CategoryDto;
import com.project.service.BoardService;
import com.project.service.CategoryService;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BoardService boardService;

    @RequestMapping("main")
    public String func(Model model){
        model.addAttribute("");
        model.addAttribute("categories", categoryService.getAllCategory());
        return "test";
    }

    @GetMapping(value = "category/{category}/boards*")
    public String dbtest(Model model, @PathVariable("category") int category_id, HttpServletRequest rq){
        int nowPage = 1;
        int total = boardService.getTotal(category_id);
        if(rq.getParameter("page") != null) nowPage = Integer.parseInt(rq.getParameter("page"));
        System.out.println("pageNumber : " + nowPage);
        System.out.println("pageNumber : " + rq.getParameter("page"));
        PagingVo pagingVo = new PagingVo(nowPage, 10, total);

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("currentCategory", category_id);
        model.addAttribute("boards", boardService.getBoardPaging(pagingVo, category_id));
        model.addAttribute("pagingVo", pagingVo);
        return "test";
    }

}
