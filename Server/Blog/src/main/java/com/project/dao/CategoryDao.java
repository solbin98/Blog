package com.project.dao;
import com.project.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDao {
    private RowMapper<CategoryDto> CategoryDtoRowMapper = new RowMapper<CategoryDto>() {
        @Override
        public CategoryDto mapRow(ResultSet rs, int i) throws SQLException {
            CategoryDto categoryDto = new CategoryDto(
                    rs.getInt("category_id"),
                    rs.getInt("parent"),
                    rs.getInt("order_num"),
                    rs.getString("name"));
            return categoryDto;
        }
    };

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public CategoryDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int selectCount(){
        Integer ret = jdbcTemplate.queryForObject("select count(*) from Category", Integer.class);
        return ret;
    }

    public int selectCount(int category_id){
        Integer ret = jdbcTemplate.queryForObject(
                "select count(*) from Category where category_id = ?",
                Integer.class,
                category_id);
        return ret;
    }

    public List<CategoryDto> selectAll(){
        List<CategoryDto> ret = jdbcTemplate.query("select * from Category", CategoryDtoRowMapper);
        return ret;
    }

    public List<CategoryDto> deleteAll(){
        List<CategoryDto> ret = jdbcTemplate.query("delete * from Category", CategoryDtoRowMapper);
        return ret;
    }

    public void deleteById(int category_id){
        jdbcTemplate.update("delete * from Category where category_id = " + category_id);
    }

    public void insert(CategoryDto categoryDto){
        jdbcTemplate.update("insert into Category (?,?,?,?)",
                                    categoryDto.getCategory_id(),
                                    categoryDto.getParent(),
                                    categoryDto.getOrder_num(),
                                    categoryDto.getName());
    }

    public void update(CategoryDto categoryDto){
        jdbcTemplate.update("update Category set (?,?,?,?) where category_id = ?",
                categoryDto.getCategory_id(),
                categoryDto.getParent(),
                categoryDto.getOrder_num(),
                categoryDto.getName(),
                categoryDto.getCategory_id());
    }

    public void delete(int category_id){
        jdbcTemplate.update("delete from Category where category_id = " + category_id);
    }
}
