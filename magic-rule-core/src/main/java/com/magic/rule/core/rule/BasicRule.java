package com.magic.rule.core.rule;

import com.magic.rule.core.api.Rule;
import com.magic.rule.core.bean.Facts;

public class BasicRule implements Rule {

    private String name;

    private String description;

    private int priority;


    public BasicRule() {
        this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
    }

    public BasicRule(final String name, final String description, final int priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }


    @Override
    public boolean evaluate(Facts facts) {
        return true;
    }

    @Override
    public void execute(Facts facts) throws Exception {

    }


    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BasicRule basicRule = (BasicRule) o;

        if (priority != basicRule.priority)
            return false;
        if (!name.equals(basicRule.name))
            return false;
        return !(description != null ? !description.equals(basicRule.description) : basicRule.description != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + priority;
        return result;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
