package com.smpl.base.entity;

/**
 *分页实体类
 */
public class PageInfo {
    //初始化数据
    private int pageNumber=0;//当前页
    private int pageSize=5;//每页条数
    private int totalCount;//总条数
    private int totalPageNumber;//总页数
    private int dbIndex = 0;// 起始行，通常该属性通过pageNo和pageSize计算得到


    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPageNumber() {
        return totalPageNumber;
    }

    public void setPageNumber(int pageNumber) {
        int temp=(pageNumber-1)<0?0:(pageNumber-1);
        this.dbIndex = temp * pageNumber;
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalCount(int totalCount) {

        if (totalCount%pageSize==0){
            this.pageNumber=totalCount%pageSize;
        }else{
            this.pageNumber=totalCount%pageSize+1;
        }
        this.totalCount = totalCount;
    }

    public void setTotalPageNumber(int totalPageNumber) {
        this.totalPageNumber = totalPageNumber;
    }


    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", totalPageNumber=" + totalPageNumber +
                ", dbIndex=" + dbIndex +
                '}';
    }
}
