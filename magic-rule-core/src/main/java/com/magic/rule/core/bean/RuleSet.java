package com.magic.rule.core.bean;


import com.magic.rule.core.api.Rule;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * 规则集
 */
public class RuleSet implements Iterable<Rule> {

    private Set<Rule> rules = initRules(null);

    public RuleSet(Collection<Rule> rules) {
        this.rules = initRules(rules);
    }

    public RuleSet(Rule... rules ) {
        Collections.addAll(this.rules, rules);
    }

    public void register(Rule rule){
        Objects.requireNonNull(rule);
        this.rules.add(rule);
    }

    public void registerAll(List<Rule> rules){
        Objects.requireNonNull(rules);
        this.rules.addAll(rules);
    }

    public void unRegister(Rule rule){
        Objects.requireNonNull(rule);
        this.rules.remove(rule);
    }

    public boolean isEmpty() {
        return rules.isEmpty();
    }

    /**
     * Clear rules.
     */
    public void clear() {
        rules.clear();
    }

    public int size(){
        return rules.size();
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    @Override
    public void forEach(Consumer<? super Rule> action) {
        this.rules.forEach(action);
    }

    @Override
    public Spliterator<Rule> spliterator() {
        return rules.spliterator();
    }

    private Set<Rule> initRules(Collection<Rule> ruleSet){
        Set<Rule> rules = new TreeSet<>(new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                return o2.compareTo(o1);
            }
        });
        if(!CollectionUtils.isEmpty(ruleSet)){
            rules.addAll(ruleSet);
        }
        return rules;
    }
}
