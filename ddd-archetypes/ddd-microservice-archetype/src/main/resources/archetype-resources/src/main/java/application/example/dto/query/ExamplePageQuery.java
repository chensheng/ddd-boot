#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.dto.query;

import io.github.chensheng.dddboot.microservice.core.*;
import lombok.Data;
import ${package}.domain.example.valueobject.ExampleStatus;

@Data
public class ExamplePageQuery extends PageQuery {
    @QuerySortable
    private String username;

    @QueryCondition(column = "username", operator = ConditionOperator.like)
    private String usernameLike;

    @QuerySortable(order = OrderType.DESC)
    private ExampleStatus status;

    @Override
    protected Long getMaxSize() {
        return 20l;
    }
}
