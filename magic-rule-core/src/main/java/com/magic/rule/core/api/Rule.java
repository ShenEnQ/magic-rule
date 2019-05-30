package com.magic.rule.core.api;

import com.magic.rule.core.bean.Facts;

public interface Rule extends Comparable<Rule> {
    String DEFAULT_NAME = "rule";

    String DEFAULT_DESCRIPTION = "description";

    int DEFAULT_PRIORITY = Integer.MAX_VALUE - 1;

    String getName();

    String getDescription();


    /**
     * The higher the value, the lower the priority of execution
     * @return
     */
    int getPriority();

    /**
     * whether the rule be executed
     * @param facts
     * @return true or false
     */
    boolean evaluate(Facts facts);

    /**
     * the real logic of the rule
     * @param facts
     * @throws Exception
     */
    void execute(Facts facts) throws Exception;

    default int compareTo(final Rule rule) {
        if (getPriority() < rule.getPriority()) {
            return -1;
        } else if (getPriority() > rule.getPriority()) {
            return 1;
        } else {
            return getName().compareTo(rule.getName());
        }
    }
}
