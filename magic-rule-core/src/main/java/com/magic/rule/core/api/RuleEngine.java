package com.magic.rule.core.api;

import com.magic.rule.core.bean.Facts;
import com.magic.rule.core.bean.RuleSet;

public interface RuleEngine {
    void fire(RuleSet rules, Facts facts);
}
