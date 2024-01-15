package org.example.application.example.dto.query;

import io.github.chensheng.dddboot.microservice.core.*;
import lombok.Data;
import org.example.domain.example.valueobject.ExampleStatus;

@Data
public class ExampleListQuery extends ListQuery {
    @QuerySortable
    private String username;

    @QueryCondition(column = "username", operator = ConditionOperator.like)
    private String usernameLike;

    @QuerySortable(order = OrderType.DESC)
    private ExampleStatus status;

    @Override
    protected Long getMaxLimit() {
        return 10l;
    }
}
