package com.project.dao;

import com.project.dto.BoardDto;
import com.project.util.PagingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BoardDao {
    private RowMapper<BoardDto> boardDtoRowMapper = new RowMapper<BoardDto>() {
        @Override
        public BoardDto mapRow(ResultSet rs, int i) throws SQLException {
            BoardDto boardDto = new BoardDto(
                    rs.getInt("board_id"),
                    rs.getInt("category_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getTimestamp("date").toLocalDateTime());
            return boardDto;
        }
    };


    private JdbcTemplate jdbcTemplate;
    @Autowired
    public BoardDao(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource); }

    public List<BoardDto> selectAll(){
        List<BoardDto> ret = jdbcTemplate.query("select * from Board", boardDtoRowMapper );
        return ret;
    }

    public List<BoardDto> select(int board_id){
        List<BoardDto> ret = jdbcTemplate.query(
                "select * from Board where board_id = ?",
                boardDtoRowMapper,
                board_id);
        return ret;
    }

    public List<BoardDto> selectPaging(PagingVo pagingVo, int category_id){
        int offset = (pagingVo.getNowPage()-1) * pagingVo.getPerPage();
        int limits = pagingVo.getPerPage();
        List<BoardDto> ret = jdbcTemplate.query(
                "select * from Board where category_id = ? order by board_id DESC limit ?, ? ",
                boardDtoRowMapper,
                category_id, offset, limits);
        return ret;
    }

    public int selectCount(){
        Integer ret = jdbcTemplate.queryForObject("select count(*) from Board", Integer.class);
        return ret;
    }

    public int selectCountCategory(int category_id){
        Integer ret = jdbcTemplate.queryForObject(
                "select count(*) from Board where category_id = ?",
                Integer.class,
                category_id);
        return ret;
    }

    public void insert(BoardDto boardDto){
        jdbcTemplate.update(
                "insert into Board values(?,?,?,?)",
                boardDto.getBoard_id(),
                boardDto.getCategory_id(),
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getDate());
    }

    public void update(BoardDto boardDto){
        jdbcTemplate.update(
                "update Board set(?,?,?,?) where board_id = ?",
                boardDto.getBoard_id(),
                boardDto.getCategory_id(),
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getDate(),
                boardDto.getBoard_id());
    }

    public void delete(int board_id){
        jdbcTemplate.update("delete * from Board where board_id = " + board_id);
    }
}