package io.github.chensheng.dddboot.microservice.core;

import java.util.List;

public class SortableQuery {
    @QueryCondition(ignore = true)
    private List<OrderItem> orders;

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }
}
