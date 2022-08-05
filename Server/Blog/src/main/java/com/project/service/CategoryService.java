package com.project.service;

import com.project.dao.BoardDao;
import com.project.dao.CategoryDao;

import com.project.dto.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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
        int [] totalList = new int [ret.size()+1];

        for(int i=0;i<ret.size();i++){
            int cid = ret.get(i).getCategory_id();
            int parent = ret.get(i).getParent();
            int total = boardDao.selectCountCategory(cid);
            totalList[cid] += total;
            totalList[parent] += total;
        }

        for(int i=0;i< ret.size();i++){
            int cid = ret.get(i).getCategory_id();
            ret.get(i).setTotal(totalList[cid]);
        }

        return ret;
    }

    public List<CategoryDto> getCategoryByParentID(int parent_id) { return categoryDao.selectByParentID(parent_id); }
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
