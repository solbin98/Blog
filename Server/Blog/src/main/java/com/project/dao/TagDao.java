package com.project.dao;

import com.project.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TagDao {
    private RowMapper<TagDto> TagDtoRowMapper = new RowMapper<TagDto>() {
        @Override
        public TagDto mapRow(ResultSet rs, int i) throws SQLException {
            TagDto tagDto = new TagDto(
                    rs.getInt("tag_id"),
                    rs.getString("name"));
            return tagDto;
        }
    };

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public TagDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<TagDto> selectAll(){
        List<TagDto> ret = jdbcTemplate.query("select * from Tag", TagDtoRowMapper);
        return ret;
    }



    public List<TagDto> selectById(int tag_id){
        List<TagDto> ret = jdbcTemplate.query("select * from Tag where tag_id = " + tag_id , TagDtoRowMapper);
        return ret;
    }

    public void insert(TagDto tag){
        jdbcTemplate.update("insert into Tag values(?,?)", tag.getTag_id(), tag.getName());
    }

    public void update(TagDto tag){
        jdbcTemplate.update("update Tag set(?,?) where tag_id = ?", tag.getTag_id(), tag.getName(), tag.getTag_id());
    }
    public void delete(int tag_id){
        jdbcTemplate.update("select * from Tag where tag_id = " + tag_id);
    }
}
