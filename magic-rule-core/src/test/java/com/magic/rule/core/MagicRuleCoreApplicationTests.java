package com.magic.rule.core;

import com.magic.rule.core.api.Rule;
import com.magic.rule.core.bean.DefaultRuleEngine;
import com.magic.rule.core.bean.Facts;
import com.magic.rule.core.bean.RuleSet;
import com.magic.rule.core.rule.BasicRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MagicRuleCoreApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testDefaultEngine(){
		DefaultRuleEngine defaultRuleEngine = new DefaultRuleEngine();
		RuleSet rules = new RuleSet();

		rules.register(new BasicRule("testRule1","",4));
		rules.register(new BasicRule("testRule2","",3));
		rules.register(new BasicRule("testRule3","",2));
		rules.register(new BasicRule("testRule4","",1));


		defaultRuleEngine.fire(rules,new Facts());
	}

}
