package com.project.service;

import com.project.dao.BoardDao;
import com.project.dao.BoardTagDao;
import com.project.dto.BoardDto;
import com.project.util.PagingVo;

import javax.sql.DataSource;
import java.util.List;

public class BoardService {
    private BoardDao boardDao;

    public BoardService(DataSource dataSource) {
        this.boardDao = new BoardDao(dataSource);
    }

    public int getTotal(int category_id){return boardDao.selectCountCategory(category_id);}
    public int getTotal(){return boardDao.selectCount();}
    public List<BoardDto> getAllBoard(){ return boardDao.selectAll();}
    public BoardDto getBoard(int board_id) { return boardDao.select(board_id); }
    public List<BoardDto> getBoardPaging(PagingVo pagingVo, int category_id) { return boardDao.selectPaging(pagingVo, category_id);}
    public void addBoard(BoardDto boardDto) { boardDao.insert(boardDto);}
    public void updateBoard(BoardDto boardDto) { boardDao.update(boardDto); }
    public void deleteBoard(int board_id) { boardDao.delete(board_id); }
}
