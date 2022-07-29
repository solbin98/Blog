package com.project.util;

import com.project.dto.TagDto;

import java.util.List;

public class BoardWriteInfoDto {
    List<Integer> tag;
    List<String> image;
    List<String> allImage;
    String content;
    String title;
    String date;
    int categoryID;

    public BoardWriteInfoDto(List<Integer> tag, List<String> image, List<String> allImage, String content, String title, String date, int categoryID) {
        this.tag = tag;
        this.image = image;
        this.allImage = allImage;
        this.content = content;
        this.categoryID = categoryID;
        this.date = date;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Integer> getTag() {
        return tag;
    }

    public void setTag(List<Integer> tag) {
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

    @Override
    public String toString() {
        return "BoardWriteInfoDto{" +
                "tag=" + tag +
                ", image=" + image +
                ", allImage=" + allImage +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", categoryID=" + categoryID +
                '}';
    }
}
