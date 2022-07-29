package com.project.dao;

import com.project.dto.CommentDto;
import com.project.dto.FileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FileDao {
    private RowMapper<FileDto> FileDtoRowMapper = new RowMapper<FileDto>() {
        @Override
        public FileDto mapRow(ResultSet rs, int i) throws SQLException {
            FileDto fileDto = new FileDto(
                    rs.getInt("file_id"),
                    rs.getInt("board_id"),
                    rs.getString("path"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("size"));
            return fileDto;
        }
    };

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public FileDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<FileDto> selectAll(){
        List<FileDto> ret = jdbcTemplate.query("select * from File", FileDtoRowMapper);
        return ret;
    }

    public List<FileDto> select(int board_id){
        List<FileDto> ret = jdbcTemplate.query("select * from File where board_id = " + board_id, FileDtoRowMapper);
        return ret;
    }


    public void insert(FileDto fileDto){
        jdbcTemplate.update("insert into File(board_id, path, name, type, size) values (?,?,?,?,?)",
            fileDto.getBoard_id(),
            fileDto.getPath(),
            fileDto.getName(),
            fileDto.getType(),
            fileDto.getSize());
    }

    public void updateBoardID(int boardID, String fileName){
        jdbcTemplate.update("update File set board_id = ? where name = ?", boardID, fileName);
    }

    public void delete(int file_id){
        jdbcTemplate.update("delete from File where fild_id = "+file_id);
    }

}
