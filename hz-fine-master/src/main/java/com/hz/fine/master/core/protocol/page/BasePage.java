package com.hz.fine.master.core.protocol.page;

/**
 * @Description 分页的基础属性
 * @Author yoko
 * @Date 2019/11/25 11:39
 * @Version 1.0
 */
public class BasePage {

    private Integer pageSize;
    private Integer pageNumber;
    private Integer start;
    private Integer end;

    private Integer rowCount;

    private Page page = new Page();

    protected void doPage(){
        page.setRowCount(rowCount);
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getStart() {
        if (this.pageNumber != null && this.pageNumber > 0 && this.pageSize != null && this.pageSize > 0 ){
            int start_ = (this.pageNumber - 1) * this.pageSize;
            return start_;
        }else{
            return start;
        }
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        if (this.pageSize != null && this.pageSize > 0 ){
            int end_ = this.pageSize;
            return end_;
        }else{
            return end;
        }
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
        this.doPage();
    }
}
