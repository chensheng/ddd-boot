package io.github.chensheng.dddboot.microservice.core;

public class PageQuery extends SortableQuery {
    @QueryCondition(ignore = true)
    private long size = 10;

    @QueryCondition(ignore = true)
    private long current = 1;

    /**
     * 最大分页大小；默认为null，表示不限制；可重写该方法，限制分页大小。
     * @return 最大分页大小
     */
    protected Long getMaxSize() {
        return null;
    }

    public long getSize() {
        Long maxSize = getMaxSize();
        if (maxSize == null || size <= maxSize) {
            return size;
        }

        return maxSize;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }
}
