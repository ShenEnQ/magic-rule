package com.magic.rule.core.annotation;


import java.lang.annotation.*;

/**
 * 每个规则是否执行的现行条件
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Condition {
}
