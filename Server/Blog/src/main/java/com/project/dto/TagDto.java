package com.project.dto;

public class TagDto {
    int tag_id;
    String name;

    public TagDto(int tag_id, String name) {
        this.tag_id = tag_id;
        this.name = name;
    }

    public int getTag_id() {return tag_id;}
    public void setTag_id(int tag_id) {this.tag_id = tag_id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
