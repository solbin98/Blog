package com.project.util.data;

public class PagingVo {
    private int nowPage;
    private int leftMostPage;
    private int rightMostPage;
    private int lastPage;
    private int perPage;
    private int total;
    boolean leftPossible = false;
    boolean rightPossible = false;

    public PagingVo(int nowPage, int perPage, int total) {
        this.nowPage = nowPage;
        this.perPage = perPage;
        this.total = total;
        calcLastPage();
        calcLeftRightMostPage();
        if(leftMostPage > 1) leftPossible = true;
        if(rightMostPage > lastPage) rightMostPage = lastPage;
        if(rightMostPage < lastPage) rightPossible = true;
    }

    private void calcLastPage(){
        this.lastPage = (int)Math.ceil(((double)total / (double)perPage));
    }

    private void calcLeftRightMostPage(){
        this.rightMostPage = (int) Math.ceil((((double)nowPage * 1.0)/10.0))*10;
        leftMostPage = rightMostPage - 9;
    }

    public int getNowPage() {return nowPage;}
    public void setNowPage(int nowPage) {this.nowPage = nowPage;}
    public int getLeftMostPage() {return leftMostPage;}
    public void setLeftMostPage(int leftMostPage) {this.leftMostPage = leftMostPage;}
    public int getRightMostPage() {return rightMostPage;}
    public void setRightMostPage(int rightMostPage) {this.rightMostPage = rightMostPage;}
    public int getLastPage() {return lastPage;}
    public void setLastPage(int lastPage) {this.lastPage = lastPage;}
    public int getPerPage() {return perPage;}
    public void setPerPage(int perPage) { this.perPage = perPage; }
    public int getTotal() {return total;}
    public void setTotal(int total) {this.total = total;}
    public boolean isLeftPossible() {return leftPossible;}
    public void setLeftPossible(boolean leftPossible) {this.leftPossible = leftPossible;}
    public boolean isRightPossible() {return rightPossible;}
    public void setRightPossible(boolean rightPossible) {this.rightPossible = rightPossible;}

    @Override
    public String toString() {
        return "PagingVo{" +
                "nowPage=" + nowPage +
                ", leftMostPage=" + leftMostPage +
                ", rightMostPage=" + rightMostPage +
                ", lastPage=" + lastPage +
                ", perPage=" + perPage +
                ", total=" + total +
                ", leftPossible=" + leftPossible +
                ", rightPossible=" + rightPossible +
                '}';
    }
}
