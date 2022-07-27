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

    public List<TagDto> getBoardTag(List<BoardTagDto> boardTags) {
        List<TagDto> ret = new ArrayList<>();
        System.out.println("retSize : " + boardTags.size());
        for(int i=0; i < boardTags.size(); i++){
            int tagID = boardTags.get(i).getTag_id();
            System.out.println(tagID + " 에 의한 검색");
            TagDto tmp = getTag(tagID);
            System.out.println(tmp.getName() + " 가 추가되었습니다.");
            ret.add(tmp);
        }
        return ret;
    };
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
