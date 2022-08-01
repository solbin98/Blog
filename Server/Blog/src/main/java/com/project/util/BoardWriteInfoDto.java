package com.project.util;

import com.project.dto.TagDto;

import java.util.List;

public class BoardWriteInfoDto {
    List<String> tag;
    List<String> image;
    List<String> allImage;
    int board_id;
    String content;
    String title;
    String date;
    String type;
    int categoryID;

    public BoardWriteInfoDto(int board_id, String type, List<String> tag, List<String> image, List<String> allImage, String content, String title, String date, int categoryID) {
        this.tag = tag;
        this.image = image;
        this.allImage = allImage;
        this.content = content;
        this.categoryID = categoryID;
        this.date = date;
        this.title = title;
        this.type = type;
        this.board_id = board_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getAllImage() {
        return allImage;
    }

    public void setAllImage(List<String> allImage) {
        this.allImage = allImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int category) {
        this.categoryID = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }
}
