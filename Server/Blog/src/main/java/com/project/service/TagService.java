package com.project.service;

import com.project.dao.TagDao;
import com.project.dto.BoardTagDto;
import com.project.dto.TagDto;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagService {
    private TagDao tagDao;
    public TagService(DataSource dataSource){
        this.tagDao = new TagDao(dataSource);
    }

    public List<TagDto> getTagsByBoardTags(List<BoardTagDto> boardTags) {
        List<TagDto> ret = new ArrayList<>();
        for(int i=0; i < boardTags.size(); i++){
            int tagID = boardTags.get(i).getTag_id();
            TagDto tmp = getTag(tagID);
            ret.add(tmp);
        }
        return ret;
    };

    public int getLastTagID(){
        return tagDao.selectLastTagID();
    }

    public List<TagDto> getTagsByTagNames(List<String> tagNames){
        List<TagDto> ret = new ArrayList();
        for(int i=0;i<tagNames.size();i++){
            String name = tagNames.get(i);
            System.out.println("name : " + name);
            TagDto tagDto = getTagByName(name);
            if(tagDto == null) {
                addTag(new TagDto(0, name));
                int lastID = getLastTagID();
                ret.add(new TagDto(lastID, name));
            }
            else ret.add(tagDto);
        }
        return ret;
    }

    public TagDto getTagByName(String name){ return tagDao.selectByName(name); }
    public List<TagDto> getAllTag(){
        return tagDao.selectAll();
    }
    public TagDto getTag(int tag_id){
        return tagDao.selectById(tag_id);
    }
    public void addTag(TagDto tagDto){ tagDao.insert(tagDto); }
    public void updateTag(TagDto tagDto){ tagDao.update(tagDto); }
    public void deleteTag(int tag_id){ tagDao.delete(tag_id); }
}
