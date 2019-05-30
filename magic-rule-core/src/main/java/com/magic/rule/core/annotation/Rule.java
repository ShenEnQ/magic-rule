package com.magic.rule.core.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Rule {
    String name() default com.magic.rule.core.api.Rule.DEFAULT_NAME;
    String description() default com.magic.rule.core.api.Rule.DEFAULT_DESCRIPTION;

    int priority() default com.magic.rule.core.api.Rule.DEFAULT_PRIORITY;
}
