package com.project.dto;

public class CategoryDto {
    int category_id;
    int parent;
    int order_num;
    int total;
    String name;

    public CategoryDto(int category_id, int parent, int order_num, String name) {
        this.category_id = category_id;
        this.parent = parent;
        this.order_num = order_num;
        this.name = name;
    }

    public int getCategory_id() {return category_id;}
    public void setCategory_id(int category_id) {this.category_id = category_id;}
    public int getParent() {return parent;}
    public void setParent(int parent) {this.parent = parent;}
    public int getOrder_num() {return order_num;}
    public void setOrder_num(int order_num) {this.order_num = order_num;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getTotal() { return total;}
    public void setTotal(int total) { this.total = total;}
}
