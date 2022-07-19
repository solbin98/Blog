package com.project.dao;

import com.project.dto.CategoryDto;
import com.project.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
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
                    rs.getInt("inner_num"),
                    rs.getInt("depth"),
                    rs.getInt("parent"),
                    rs.getString("child"),
                    rs.getString("writer"),
                    rs.getString("password"),
                    rs.getString("content"),
                    rs.getTimestamp("date").toLocalDateTime());
            return commentDto;
        }
    };

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public CommentDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<CommentDto> selectAll(){
        List<CommentDto> ret = jdbcTemplate.query("select * from Comment", CommentDtoRowMapper);
        return ret;
    }

    public List<CommentDto> select(int board_id){
        List<CommentDto> ret = jdbcTemplate.query("select * from Comment where board_id = " + board_id, CommentDtoRowMapper);
        return ret;
    }

    public void insert(CommentDto commentDto){
        jdbcTemplate.update("insert into Comment (?,?,?,?,?,?,?,?,?)",
                commentDto.getBoard_id(),
                commentDto.getInner_num(),
                commentDto.getDepth(),
                commentDto.getParent(),
                commentDto.getWriter(),
                commentDto.getPassword(),
                commentDto.getContent(),
                commentDto.getDate()
                );
    }

    public void update(CommentDto commentDto){
        jdbcTemplate.update("update Comment set (?,?,?,?,?,?,?,?,?)",
                commentDto.getBoard_id(),
                commentDto.getInner_num(),
                commentDto.getDepth(),
                commentDto.getParent(),
                commentDto.getWriter(),
                commentDto.getPassword(),
                commentDto.getContent(),
                commentDto.getDate()
        );
    }

    public void delete(int comment_id){
        jdbcTemplate.update("delete * from Comment where comment_id = " + comment_id, CommentDtoRowMapper);
    }
}
