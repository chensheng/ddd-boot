package io.github.chensheng.dddboot.microservice.core;

public enum ConditionOperator {
    /** 等于 */
    eq,
    /** 不等于 */
    ne,
    /** 大于 */
    gt,
    /** 大于等于 */
    ge,
    /** 小于 */
    lt,
    /** 小于等于 */
    le,
    /** like */
    like,
    /** not like */
    not_like,
    /** in */
    in,
    /** not in */
    not_in;
}
