package com.project.service;

import com.project.dao.BoardTagDao;
import com.project.dto.BoardTagDto;
import com.project.dto.TagDto;

import javax.sql.DataSource;
import java.util.List;

public class BoardTagService {
    private BoardTagDao boardTagDao;

    public BoardTagService(DataSource dataSource) {
        boardTagDao = new BoardTagDao(dataSource);
    }

    public void BoardTagService(DataSource dataSource){
        this.boardTagDao = new BoardTagDao(dataSource);
    }

    public List<BoardTagDto> getAllBoardTag(){
        return boardTagDao.selectAll();
    }
    public List<BoardTagDto> getBoardTag(int board_id){
        return boardTagDao.selectByBoardId(board_id);
    }
    public void addBoardTag(BoardTagDto boardTagDto) {boardTagDao.insert(boardTagDto);}
    public void deleteBoardTag(BoardTagDto boardTagDto){
        boardTagDao.delete(boardTagDto);
    }
    public void deleteBoardByBoardID(int board_id) {boardTagDao.deleteByBoardID(board_id);}
}
