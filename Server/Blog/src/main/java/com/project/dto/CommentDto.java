package com.project.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommentDto {
    int comment_id;
    int board_id;
    int inner_num;
    int depth;
    int parent;
    String writer;
    String password;
    String content;
    @DateTimeFormat(pattern = "yyyyMMddHH")
    LocalDateTime date;

    public CommentDto(int comment_id, int board_id, int inner_num, int depth, int parent, String child, String writer, String password, String content, LocalDateTime date) {
        this.comment_id = comment_id;
        this.board_id = board_id;
        this.inner_num = inner_num;
        this.depth = depth;
        this.parent = parent;
        this.writer = writer;
        this.password = password;
        this.content = content;
        this.date = date;
    }

    public int getComment_id() {
        return comment_id;
    }
    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
    public int getBoard_id() {
        return board_id;
    }
    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }
    public int getInner_num() {
        return inner_num;
    }
    public void setInner_num(int inner_num) {
        this.inner_num = inner_num;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public int getParent() {
        return parent;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }
    public String getWriter() {
        return writer;
    }
    public void setWriter(String writer) {
        this.writer = writer;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
