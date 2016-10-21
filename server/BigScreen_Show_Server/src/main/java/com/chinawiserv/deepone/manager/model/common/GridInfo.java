package com.chinawiserv.deepone.manager.model.common;

import java.io.Serializable;

/**
 * Created by zengpzh on 2016/6/29.
 */
public class GridInfo<T> implements Serializable {

    private static final long serialVersionUID = 6510915543347839540L;

    private Integer current;

    private Integer pageSize;

    private Integer total;

    private T filter;

    public Integer getPageSize() {
        if(pageSize == null || pageSize <= 0) pageSize = 10;
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrent() {
        if(current == null || current <= 0) current = 1;
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getTotal() {
        if(total == null || total < 0) total = 0;
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }
}
