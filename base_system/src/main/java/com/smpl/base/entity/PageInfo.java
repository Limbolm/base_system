package com.smpl.base.entity;

/**
 *分页实体类
 */
public class PageInfo {
    //初始化数据
    private Integer pageNumber=0;//当前页
    private Integer pageSize=5;//每页条数
    private Integer totalCount;//总条数
    private Integer totalPageNumber;//总页数
    private Integer dbIndex = 0;// 起始行，通常该属性通过pageNo和pageSize计算得到


    public Integer getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(Integer dbIndex) {
        this.dbIndex = dbIndex;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getTotalPageNumber() {
        return totalPageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        int temp=(pageNumber-1)<0?0:(pageNumber-1);
        this.dbIndex = temp * pageNumber;
        this.pageNumber = pageNumber;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalCount(Integer totalCount) {

        if (totalCount%pageSize==0){
            this.pageNumber=totalCount%pageSize;
        }else{
            this.pageNumber=totalCount%pageSize+1;
        }
        this.totalCount = totalCount;
    }

    public void setTotalPageNumber(Integer totalPageNumber) {
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
