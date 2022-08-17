package com.project.service;

import com.project.dao.BoardDao;
import com.project.dto.BoardDto;
import com.project.dto.CategoryDto;
import com.project.util.data.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;

public class BoardService {
    private BoardDao boardDao;
    @Autowired
    CategoryService categoryService;

    public BoardService(DataSource dataSource) {
        this.boardDao = new BoardDao(dataSource);
    }

    public int getLastBoardID() {return boardDao.selectLastBoardID();}
    public int getTotal(int category_id){
        int total = 0;
        total += boardDao.selectCountCategory(category_id);
        List<CategoryDto> children = categoryService.getCategoryByParentID(category_id);
        for(int i=0;i<children.size();i++) {
            total += boardDao.selectCountCategory(children.get(i).getCategory_id());
        }
        return total;
    }
    public int getTotal(){return boardDao.selectCount();}
    public List<BoardDto> getAllBoard(){ return boardDao.selectAll();}
    public BoardDto getBoard(int board_id) { return boardDao.select(board_id); }
    public List<BoardDto> getBoardPaging(PagingVo pagingVo, int category_id) {
        List<CategoryDto> children = categoryService.getCategoryByParentID(category_id);
        List<CategoryDto> family = children;
        CategoryDto parent = new CategoryDto(category_id, 0,0, "");
        family.add(parent);
        return boardDao.selectPaging(pagingVo, family);
    }
    public void addBoard(BoardDto boardDto) { boardDao.insert(boardDto);}
    public void updateBoard(BoardDto boardDto) { boardDao.update(boardDto); }
    public void deleteBoard(int board_id) { boardDao.delete(board_id); }
}
