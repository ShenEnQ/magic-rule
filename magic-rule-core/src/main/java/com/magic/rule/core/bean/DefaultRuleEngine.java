package com.magic.rule.core.bean;

import com.magic.rule.core.api.Rule;
import com.magic.rule.core.api.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DefaultRuleEngine implements RuleEngine {

    private Logger logger = LoggerFactory.getLogger(DefaultRuleEngine.class);

    @Override
    public void fire(RuleSet rules, Facts facts) {
        doFire(rules,facts);
    }

    private void doFire(RuleSet rules,Facts facts){
        for (Rule rule : rules) {
            if(!rule.evaluate(facts)){
                logger.debug("Rule {} evaluate false, skip it!",rule.getName());
                continue;
            }
            try {
                rule.execute(facts);
                logger.debug("Rule {} - priority:{} executed",rule.getName(),rule.getPriority());
            } catch (Exception e) {
                logger.error("Rule {} executed failed",rule.getName(),e);
            }
        }
    }
}
