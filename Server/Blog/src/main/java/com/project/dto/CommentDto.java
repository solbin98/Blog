package com.project.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDto {
    int comment_id;
    int board_id;
    int depth;
    int parent;
    String writer;
    String password;
    String content;
    String date;

    public CommentDto(int comment_id, int board_id, int depth, int parent, String writer, String password, String content, String date) {
        this.comment_id = comment_id;
        this.board_id = board_id;
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
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    @Override
    public String toString() {
        return "CommentDto{" +
                "comment_id=" + comment_id +
                ", board_id=" + board_id +
                ", depth=" + depth +
                ", parent=" + parent +
                ", writer='" + writer + '\'' +
                ", password='" + password + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                '}';
    }
}
