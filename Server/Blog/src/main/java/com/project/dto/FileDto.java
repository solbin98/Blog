package com.project.dto;

public class FileDto {
    int file_id;
    int board_id;
    String path;
    String name;
    String type;
    String size;

    public FileDto(int file_id, int board_id, String path, String name, String type, String size) {
        this.file_id = file_id;
        this.board_id = board_id;
        this.path = path;
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public int getFile_id() {return file_id;}
    public void setFile_id(int file_id) {this.file_id = file_id;}
    public int getBoard_id() {return board_id;}
    public void setBoard_id(int board_id) {this.board_id = board_id;}
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public String getSize() {return size;}
    public void setSize(String size) {this.size = size;}
}
