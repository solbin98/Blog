package com.project.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;

public class BoardDto {
    int board_id;
    int category_id;
    String title;
    String content;
    @DateTimeFormat(pattern = "yyyyMMddHH")
    LocalDateTime date;

    public BoardDto(int board_id, int category_id, String title, String content, LocalDateTime date) {
        this.board_id = board_id;
        this.category_id = category_id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getBoard_id() {
        return board_id;
    }
    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }
    public int getCategory_id() {
        return category_id;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
