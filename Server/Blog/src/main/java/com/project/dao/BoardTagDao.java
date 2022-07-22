package com.project.dao;

import com.project.dto.BoardTagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BoardTagDao {
    private RowMapper<BoardTagDto> boardTagDtoRowMapper = new RowMapper<BoardTagDto>() {
        @Override
        public BoardTagDto mapRow(ResultSet rs, int i) throws SQLException {
            BoardTagDto boardTagDto = new BoardTagDto(
                    rs.getInt("tag_id"),
                    rs.getInt("board_id"));
            return boardTagDto;
        }
    };

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public BoardTagDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<BoardTagDto> selectAll(){
        List<BoardTagDto> ret = jdbcTemplate.query("select * from BoardTag", boardTagDtoRowMapper);
        return ret;
    }

    public List<BoardTagDto> selectByBoardId(int board_id){
        List<BoardTagDto> ret = jdbcTemplate.query("select * from BoardTag where board_id = " + board_id, boardTagDtoRowMapper);
        return ret;
    }

    public void insert(BoardTagDto boardTagDto){
        jdbcTemplate.update("insert into BoardTag values(?,?)", boardTagDto.getTag_id(), boardTagDto.getBoard_id());
    }

    public void delete(BoardTagDto boardTagDto){
        jdbcTemplate.update("delete from BoardTag where board_id = "
                + boardTagDto.getBoard_id()
                + " and tag_id = " + boardTagDto.getTag_id());
    }
}
