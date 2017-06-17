/**
 * 
 */
package com.octopusdio.api.common.domain;

import java.util.List;

/**
 * @author zhangle
 *
 */
public class ListResult<T> {
    private long count;
    private List<T> list;

    /**
     * Constructor
     *
     * @param count
     * @param list
     */
    public ListResult(long count, List<T> list) {
        this.count = count;
        this.list = list;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
