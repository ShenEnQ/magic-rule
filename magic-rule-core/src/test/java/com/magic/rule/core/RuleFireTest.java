package com.magic.rule.core;

import com.magic.rule.core.api.Rule;
import com.magic.rule.core.bean.DefaultRuleEngine;
import com.magic.rule.core.bean.Facts;
import com.magic.rule.core.bean.RuleSet;
import com.magic.rule.core.handler.RuleProxyHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class RuleFireTest extends BaseTest {

    @Value("${client.rule.package}")
    private String rulePackage;

    @Test
    public void testFire(){
        List<Rule> ruleList = RuleProxyHandler.createRules(rulePackage);
        DefaultRuleEngine ruleEngine = new DefaultRuleEngine();
        RuleSet ruleSet = new RuleSet(ruleList);
        ruleEngine.fire(ruleSet,new Facts());
    }

}
