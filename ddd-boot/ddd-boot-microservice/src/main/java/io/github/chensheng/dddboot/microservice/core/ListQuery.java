package io.github.chensheng.dddboot.microservice.core;

public class ListQuery extends SortableQuery {
    private Long limit;

    /**
     * 最多允许返回的记录数；默认为null，表示不限制
     * @return 最大记录数
     */
    protected Long getMaxLimit() {
        return null;
    }

    public Long getLimit() {
        Long maxLimit = getMaxLimit();
        if(maxLimit == null || limit == null) {
            return limit;
        }

        if(limit <= maxLimit) {
            return limit;
        }

        return maxLimit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
