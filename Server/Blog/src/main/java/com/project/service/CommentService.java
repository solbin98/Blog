package com.project.service;

import com.project.dao.CommentDao;
import com.project.dto.CommentDto;
import com.project.util.CommentWriterDto;
import com.project.util.PagingVo;

import javax.sql.DataSource;
import java.util.List;

public class CommentService {
    private CommentDao commentDao;

    public CommentService(DataSource dataSource) {
        commentDao = new CommentDao(dataSource);
    }

    public void CommentService(DataSource dataSource){
        this.commentDao = new CommentDao(dataSource);
    }

    public int getTotal(int board_id) { return commentDao.selectCountByBoardId(board_id);}
    public List<CommentDto> getAllComment(){ return commentDao.selectAll();}
    public List<CommentDto> getCommentByBoardId(int board_id) { return commentDao.select(board_id); }
    public int checkCommentWriterInfo(CommentWriterDto commentWriterInfo, int comment_id) { return commentDao.selectWriterInfo(commentWriterInfo, comment_id);}
    public int getCommentCountByParent(int parent) { return commentDao.selectCountByParent(parent); }
    public List<CommentDto> getCommentPaging(PagingVo pagingVo, int board_id) {
        return commentDao.selectPaging(pagingVo, board_id); }
    public void addComment(CommentDto commentDto) { commentDao.insert(commentDto);}
    public void updateComment(CommentDto commentDto) { commentDao.update(commentDto); }
    public void deleteComment(int comment_id) { commentDao.delete(comment_id); }
    public void deleteByBoardID(int board_id) { commentDao.deleteByBoardID(board_id);}
}
