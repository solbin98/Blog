package com.project.dto;

public class BoardTagDto {
    int tag_id;
    int board_id;

    public BoardTagDto(int tag_id, int board_id) {
        this.tag_id = tag_id;
        this.board_id = board_id;
    }

    public int getTag_id() {
        return tag_id;
    }
    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
    public int getBoard_id() {
        return board_id;
    }
    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }
}
