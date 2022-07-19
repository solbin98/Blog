package com.project.service;

import com.project.dao.TagDao;
import com.project.dto.TagDto;

import javax.sql.DataSource;
import java.util.List;

public class TagService {
    private TagDao tagDao;
    public TagService(DataSource dataSource){
        this.tagDao = new TagDao(dataSource);
    }

    public List<TagDto> getAllTag(){
        return tagDao.selectAll();
    }
    public List<TagDto> getTag(int tag_id){
        return tagDao.selectById(tag_id);
    }
    public void addTag(TagDto tagDto){ tagDao.insert(tagDto); }
    public void updateTag(TagDto tagDto){ tagDao.update(tagDto); }
    public void deleteTag(int tag_id){ tagDao.delete(tag_id); }
}
