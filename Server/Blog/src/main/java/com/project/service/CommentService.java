package com.project.service;

import com.project.dao.BoardDao;
import com.project.dao.CommentDao;
import com.project.dto.BoardDto;
import com.project.dto.CommentDto;

import javax.sql.DataSource;
import java.util.List;

public class CommentService {
    private CommentDao commentDao;

    public void CommentService(DataSource dataSource){
        this.commentDao = new CommentDao(dataSource);
    }

    public List<CommentDto> getAllComment(){ return commentDao.selectAll();}
    public List<CommentDto> getBoard(int comment_id) { return commentDao.select(comment_id); }
    public void addBoard(CommentDto commentDto) { commentDao.insert(commentDto);}
    public void updateBoard(CommentDto commentDto) { commentDao.update(commentDto); }
    public void deleteBoard(int comment_id) { commentDao.delete(comment_id); }
}
