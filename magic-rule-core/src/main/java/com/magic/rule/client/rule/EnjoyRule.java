package com.magic.rule.client.rule;

import com.magic.rule.core.annotation.Action;
import com.magic.rule.core.annotation.Condition;
import com.magic.rule.core.annotation.Priority;
import com.magic.rule.core.annotation.Rule;
import com.magic.rule.core.bean.Facts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Rule(name = "enjoyRule",description = "this is enjoy rule",priority = 2)
public class EnjoyRule {

    @Condition
    public boolean condition(Facts facts){
        log.info("============> Rule condition run {}",this.getClass().getName());
        return true;
    }


    @Action
    public void run(Facts facts){
        log.info("============> Rule execute run {}",this.getClass().getName());
    }

    @Priority
    public int priority(){
        return 1;
    }
}
