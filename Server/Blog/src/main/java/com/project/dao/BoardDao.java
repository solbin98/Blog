package com.project.dao;

import com.project.dto.BoardDto;
import com.project.dto.CategoryDto;
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
                    rs.getString("date"),
                    rs.getInt("view"));
            return boardDto;
        }
    };


    private JdbcTemplate jdbcTemplate;
    @Autowired
    public BoardDao(DataSource dataSource){ jdbcTemplate = new JdbcTemplate(dataSource); }

    public int selectLastBoardID(){
        Integer ret = jdbcTemplate.queryForObject("select board_id from board ORDER BY board_id DESC LIMIT 1", Integer.class);
        return ret;
    }

    public List<BoardDto> selectAll(){
        List<BoardDto> ret = jdbcTemplate.query("select * from Board", boardDtoRowMapper );
        return ret;
    }

    public BoardDto select(int board_id){
        BoardDto ret = jdbcTemplate.queryForObject(
                "select * from Board where board_id = ?",
                boardDtoRowMapper,
                board_id);
        return ret;
    }

    public List<BoardDto> selectPaging(PagingVo pagingVo, List<CategoryDto> family){
        int offset = (pagingVo.getNowPage()-1) * pagingVo.getPerPage();
        int limits = pagingVo.getPerPage();
        String sql = "select * from Board where ";
        for(int i=0;i<family.size();i++){
            sql += "category_id = " + family.get(i).getCategory_id() + " ";
            if(i != family.size()-1) sql += " or ";
        }
        sql += "order by board_id DESC limit ?, ? ";
        List<BoardDto> ret = jdbcTemplate.query(sql, boardDtoRowMapper, offset, limits);
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
        System.out.println(boardDto.toString());
        jdbcTemplate.update(
                "insert into Board(category_id, title, content, date, view) values(?,?,?,?,?)",
                boardDto.getCategory_id(),
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getDate(),
                boardDto.getView()
        );
    }

    public void update(BoardDto boardDto){
        jdbcTemplate.update(
                "update Board set category_id = ?, title=?, content=?, date=?, view=? where board_id = ?",
                boardDto.getCategory_id(),
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getDate(),
                boardDto.getView(),
                boardDto.getBoard_id()
        );
    }

    public void delete(int board_id){
        jdbcTemplate.update("delete from Board where board_id = " + board_id);
    }
}
