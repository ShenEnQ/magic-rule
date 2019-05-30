package com.magic.rule.core.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
    //多个action的执行优先级，数字越大优先级越大
    int order() default 1;
}
