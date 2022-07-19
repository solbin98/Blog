package com.project.service;

import com.project.dao.BoardDao;
import com.project.dao.CategoryDao;

import com.project.dto.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;


@Service
public class CategoryService {
    private CategoryDao categoryDao;
    private BoardDao boardDao;

    public CategoryService(DataSource dataSource){
        this.categoryDao = new CategoryDao(dataSource);
        this.boardDao = new BoardDao(dataSource);
    }

    public List<CategoryDto> getAllCategory(){
        List<CategoryDto> ret = categoryDao.selectAll();
        for(int i=0;i<ret.size();i++){
            int cid = ret.get(i).getCategory_id();
            ret.get(i).setTotal(boardDao.selectCountCategory(cid));
        }
        return ret;
    }

    public int getCategoryTotal(int category_id){
      return categoryDao.selectCount(category_id);
    };

    public void addCategory(CategoryDto categoryDto){
        categoryDao.insert(categoryDto);
    }
    public void updateCategory(CategoryDto categoryDto){
        categoryDao.update(categoryDto);
    }
    public void deleteCategory(int category_num){
        categoryDao.deleteById(category_num);
    }
    public void deleteAllCategory() {
        categoryDao.deleteAll();
    }
}
