package com.project.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.time.LocalDateTime;

public class BoardDto {
    int board_id;
    int category_id;

    String title;

    String content;
    String date;
    int view;

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public BoardDto(int board_id, int category_id, String title, String content, String date, int view) {
        this.board_id = board_id;
        this.category_id = category_id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.view = view;
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "board_id=" + board_id +
                ", category_id=" + category_id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", view=" + view +
                '}';
    }
}
