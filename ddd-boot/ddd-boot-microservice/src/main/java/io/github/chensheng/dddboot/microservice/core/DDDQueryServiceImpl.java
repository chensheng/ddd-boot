package io.github.chensheng.dddboot.microservice.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.web.core.BizException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

public abstract class DDDQueryServiceImpl<E extends DDDEntity, D extends IDataObject, R, C extends DDDConvertor<E, D, R>, M extends BaseMapper<D>> implements DDDQueryService<R> {
    @Autowired
    protected C convertor;

    @Autowired
    protected M mapper;

    @Override
    public Page<R> page(PageQuery query) {
        List<ColumnInfo> columns = doResolveColumns(query);
        QueryWrapper<D> queryWrapper = doCreateQueryWrapper(columns);
        List<OrderItem> orderItems = doCreateOrderItems(columns, query);
        Page<D> dataObjectPage = new Page<D>(query.getCurrent(), query.getSize());
        dataObjectPage.setOrders(orderItems);
        mapper.selectPage(dataObjectPage, queryWrapper);

        Page<R> resultPage = new Page<R>();
        resultPage.setCurrent(dataObjectPage.getCurrent());
        resultPage.setSize(dataObjectPage.getSize());
        resultPage.setPages(dataObjectPage.getPages());
        resultPage.setTotal(dataObjectPage.getTotal());
        resultPage.setOrders(dataObjectPage.getOrders());

        if(CollectionUtil.isNotEmpty(dataObjectPage.getRecords())) {
            List<R> records = new ArrayList<R>();
            for(D dataObject : dataObjectPage.getRecords()) {
                R record = convertor.toResult(dataObject);
                records.add(record);
            }
            resultPage.setRecords(records);
        }
        return resultPage;
    }

    @Override
    public List<R> list(ListQuery query) {
        List<ColumnInfo> columns = doResolveColumns(query);
        QueryWrapper<D> queryWrapper = doCreateQueryWrapper(columns);
        List<OrderItem> orderItems = doCreateOrderItems(columns, query);

        for(OrderItem orderItem : orderItems) {
            if(orderItem.isAsc()) {
                queryWrapper.orderByAsc(orderItem.getColumn());
            } else {
                queryWrapper.orderByDesc(orderItem.getColumn());
            }
        }
        Long limit = query.getLimit();
        if(limit != null) {
            queryWrapper.last("limit " + limit);
        }

        List<D> dataObjectList = mapper.selectList(queryWrapper);
        List<R> resultList = new ArrayList<R>();
        if(CollectionUtil.isNotEmpty(dataObjectList)) {
            for(D dataObject : dataObjectList) {
                R result = convertor.toResult(dataObject);
                resultList.add(result);
            }
        }
        return resultList;
    }

    @Override
    public R detail(Long id) {
        if(id == null) {
            throw new BizException("记录不存在");
        }

        D dataObject = mapper.selectById(id);
        if(dataObject == null) {
            throw new BizException("记录不存在");
        }

        return convertor.toResult(dataObject);
    }

    private List<OrderItem> doCreateOrderItems(List<ColumnInfo> columns, SortableQuery query) {
        if(CollectionUtil.isEmpty(columns)) {
            return Collections.emptyList();
        }

        Map<String, OrderItem> defaultSortMap = new HashMap<String, OrderItem>();
        Set<String> sortableColumns = new HashSet<String>();
        for(ColumnInfo columnInfo : columns) {
            QuerySortable sortable = columnInfo.getSortable();
            if(sortable == null) {
                continue;
            }

            sortableColumns.add(columnInfo.getName());
            if(sortable.order() == OrderType.ASC) {
                defaultSortMap.put(columnInfo.getName(), OrderItem.asc(columnInfo.getName()));
            } else if(sortable.order() == OrderType.DESC) {
                defaultSortMap.put(columnInfo.getName(), OrderItem.desc(columnInfo.getName()));
            }
        }

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        if(CollectionUtil.isNotEmpty(query.getOrders())) {
            for(io.github.chensheng.dddboot.microservice.core.OrderItem item : query.getOrders()) {
                if(TextUtil.isBlank(item.getColumn())) {
                    continue;
                }

                if(!sortableColumns.contains(item.getColumn())) {
                    continue;
                }

                if(item.isAsc()) {
                    orderItems.add(OrderItem.asc(item.getColumn()));
                } else {
                    orderItems.add(OrderItem.desc(item.getColumn()));
                }
                defaultSortMap.remove(item.getColumn());
            }
        }

        if(defaultSortMap.size() > 0) {
            orderItems.addAll(defaultSortMap.values());
        }

        return orderItems;
    }

    private QueryWrapper<D> doCreateQueryWrapper(List<ColumnInfo> columns) {
        QueryWrapper<D> queryWrapper = new QueryWrapper<D>();
        if(CollectionUtil.isEmpty(columns)) {
            return queryWrapper;
        }

        for(ColumnInfo column : columns) {
            QueryCondition queryCondition = column.getCondition();
            if(queryCondition != null && queryCondition.ignore()) {
                continue;
            }

            Object queryValue = column.getValue();
            if(isEmpty(queryValue) && (queryCondition == null || !queryCondition.allowEmpty())) {
                continue;
            }

            ConditionOperator operator;
            if(queryCondition != null && queryCondition.operator() != null) {
                operator = queryCondition.operator();
            } else {
                operator = ConditionOperator.eq;
            }
            doAddQueryCondition(queryWrapper, column.getName(), operator, queryValue);
        }

        return queryWrapper;
    }

    private void doAddQueryCondition(QueryWrapper<D> queryWrapper, String column, ConditionOperator operator, Object val) {
        if(operator == ConditionOperator.eq) {
            queryWrapper.eq(column, val);
        } else if(operator == ConditionOperator.ne) {
            queryWrapper.ne(column, val);
        } else if(operator == ConditionOperator.gt) {
            queryWrapper.gt(column, val);
        } else if(operator == ConditionOperator.ge) {
            queryWrapper.ge(column, val);
        } else if(operator == ConditionOperator.lt) {
            queryWrapper.lt(column, val);
        } else if(operator == ConditionOperator.le) {
            queryWrapper.le(column, val);
        } else if(operator == ConditionOperator.like) {
            queryWrapper.like(column, val);
        } else if(operator == ConditionOperator.not_like) {
            queryWrapper.notLike(column, val);
        } else if(operator == ConditionOperator.in) {
            if(val instanceof Collection) {
                queryWrapper.in(column, (Collection<?>) val);
            } else {
                queryWrapper.in(column, (Object[])val);
            }
        } else if(operator == ConditionOperator.not_in) {
            if(val instanceof Collection) {
                queryWrapper.notIn(column, (Collection<?>) val);
            } else {
                queryWrapper.notIn(column, (Object[])val);
            }
        }
    }

    private boolean isEmpty(Object value) {
        if(value == null) {
            return true;
        }

        if(value instanceof String) {
            return TextUtil.isBlank((String)value);
        }

        if(value instanceof Collection) {
            return CollectionUtil.isEmpty((Collection)value);
        }

        return false;
    }

    private List<ColumnInfo> doResolveColumns(Object query) {
        Class<?> queryClass = query.getClass();
        Field[] fields = queryClass.getDeclaredFields();
        if(fields == null || fields.length == 0) {
            return Collections.EMPTY_LIST;
        }

        List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
        for(Field field : fields) {
            QueryCondition queryCondition = field.getDeclaredAnnotation(QueryCondition.class);
            QuerySortable querySortable = field.getDeclaredAnnotation(QuerySortable.class);

            Object queryValue = null;
            try {
                field.setAccessible(true);
                queryValue = field.get(query);
            } catch (IllegalAccessException e) {
            }

            String column;
            if(queryCondition != null && TextUtil.isNotBlank(queryCondition.column())) {
                column = queryCondition.column();
            } else {
                column = TextUtil.camelToUnderscore(field.getName());
            }

            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.condition = queryCondition;
            columnInfo.sortable = querySortable;
            columnInfo.value = queryValue;
            columnInfo.name = column;
            columns.add(columnInfo);
        }
        return columns;
    }

    @Data
    public static class ColumnInfo {
        QueryCondition condition;

        QuerySortable sortable;

        String name;

        Object value;
    }
}
