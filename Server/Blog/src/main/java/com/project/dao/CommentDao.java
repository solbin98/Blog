package com.project.dao;

import com.project.dto.CommentDto;
import com.project.util.CommentWriterDto;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CommentDao {
    private RowMapper<CommentDto> CommentDtoRowMapper = new RowMapper<CommentDto>() {
        @Override
        public CommentDto mapRow(ResultSet rs, int i) throws SQLException {
            CommentDto commentDto = new CommentDto(
                    rs.getInt("comment_id"),
                    rs.getInt("board_id"),
                    rs.getInt("depth"),
                    rs.getInt("parent"),
                    rs.getString("writer"),
                    rs.getString("password"),
                    rs.getString("content"),
                    rs.getString("date"));
            return commentDto;
        }
    };

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public CommentDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int selectCountByBoardId(int board_id){
        Integer ret = jdbcTemplate.queryForObject("select count(*) from Comment where board_id = ?", Integer.class, board_id);
        return ret;
    }

    public int selectCountByParent(int parent){
        Integer ret = jdbcTemplate.queryForObject("select count(*) from Comment where parent = ?", Integer.class, parent);
        return ret;
    }

    public int selectLastCommentID(){
        Integer ret;
        try{
            ret = jdbcTemplate.queryForObject("select comment_id from Comment ORDER BY comment_id DESC LIMIT 1", Integer.class);
        }
        catch(DataAccessException e) {
            ret = 0;
            System.out.println(e);
        }
        System.out.println(ret + " 결정 ");
        return ret;
    }

    public List<CommentDto> selectAll(){
        List<CommentDto> ret = jdbcTemplate.query("select * from Comment", CommentDtoRowMapper);
        return ret;
    }

    public List<CommentDto> select(int board_id){
        List<CommentDto> ret = jdbcTemplate.query("select * from Comment where board_id = " + board_id, CommentDtoRowMapper);
        return ret;
    }

    public List<CommentDto> selectPaging(PagingVo pagingVo, int board_id){
        int offset = (pagingVo.getNowPage()-1) * pagingVo.getPerPage();
        int limits = pagingVo.getPerPage();
        List<CommentDto> ret = jdbcTemplate.query(
                "select * from Comment where board_id = ? order by parent asc limit ?, ? ",
                CommentDtoRowMapper,
                board_id, offset, limits);;
        return ret;
    }

    public int selectWriterInfo(CommentWriterDto commentWriterInfo, int comment_id){
        Integer result = jdbcTemplate.queryForObject(
                "select count(*) from Comment where comment_id = ? and writer = ? and password = ?",
                Integer.class,
                comment_id, commentWriterInfo.getId(), commentWriterInfo.getPassword());
        return result;
    }

    public void insert(CommentDto commentDto){
        jdbcTemplate.update("insert into Comment (board_id, depth, parent, writer, password, content, date) values (?,?,?,?,?,?,?)",
                commentDto.getBoard_id(),
                commentDto.getDepth(),
                commentDto.getParent(),
                commentDto.getWriter(),
                commentDto.getPassword(),
                commentDto.getContent(),
                commentDto.getDate()
                );
    }

    public void update(CommentDto commentDto){
        jdbcTemplate.update("update Comment set content = ? where comment_id = ?",
                commentDto.getContent(),
                commentDto.getComment_id()
        );
    }

    public void updateParent(int comment_id){
        jdbcTemplate.update("update Comment set parent = ? where comment_id = ?", comment_id, comment_id);
    }
    public void delete(int comment_id){
        jdbcTemplate.update("delete from Comment where comment_id = ?", comment_id);
    }
    public void deleteByBoardID(int board_id) { jdbcTemplate.update("delete from Comment where board_id = ?", board_id); }
}
