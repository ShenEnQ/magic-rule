package com.magic.rule.core.handler;

import com.magic.rule.core.annotation.Action;
import com.magic.rule.core.annotation.Condition;
import com.magic.rule.core.annotation.Priority;
import com.magic.rule.core.api.Rule;
import com.magic.rule.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RuleProxyHandler implements InvocationHandler {

    private Object originalObject;

    private RuleProxyHandler(final Object object) {
        this.originalObject = object;
    }


    /**
     * 扫描指定的包，找到包含@Rule注解的类，
     * 并使用动态代理生成新的类。
     *
     * @param ruleBasePackage
     * @return
     */
    public static List<Rule> createRules(String ruleBasePackage) {
        List<Rule> rules = new ArrayList<>();
        try {
            List<String> classList = ClassUtil.getAllClassesByPackageName(ruleBasePackage);
            Rule tmpRule = null;
            for (String className : classList) {
                tmpRule = realCreate(className);
                rules.add(tmpRule);
            }
        } catch (Exception e) {
            log.error("Filed to proxy rule object", e);
        }
        return rules;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        RuleMethodEnum ruleMethodEnum = RuleMethodEnum.valueOf(methodName);
        //Not invoke proxied project's method directly.
        //In order to generate compareTo,equals and hashCoe method if it does not have.
        switch (ruleMethodEnum) {
            case getName:
                return getRuleName();
            case getDescription:
                return getRuleDescription();
            case getPriority:
                return getRulePriority();
            case compareTo:
                return compareToMethod(args);
            case evaluate:
                return evaluateMethod(args);
            case execute:
                return executeMethod(args);
            case equals:
                return equalsMethod(args);
            case hashCode:
                return hashCodeMethod();
            case toString:
                return toStringMethod();
            default:
                return null;
        }

    }

    private static Rule realCreate(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            Class clazz = Class.forName(className);
            if (ClassUtil.isAnnotationPresent(com.magic.rule.core.annotation.Rule.class, clazz)) {
                log.info("=======> Find Rule Class {}", className);
                //
                Object annotatedRule = clazz.newInstance();

                Rule result = (Rule) Proxy.newProxyInstance(Rule.class.getClassLoader(),
                        new Class[]{Rule.class, Comparable.class}, new RuleProxyHandler(annotatedRule));
                return result;
            } else {
                log.warn("Rule annotation not found,ignore it: {} ", className);
            }
        } catch (Exception e) {
            log.error("Failed to proxy {}", className, e);
        }
        return null;
    }


    private String getRuleName() {
        com.magic.rule.core.annotation.Rule rule = getRuleAnnotation();
        return rule.name().equals(Rule.DEFAULT_NAME) ? getTargetClass().getSimpleName() : rule.name();
    }

    private String getRuleDescription() {
        com.magic.rule.core.annotation.Rule rule = getRuleAnnotation();
        return rule.description().equals(Rule.DEFAULT_DESCRIPTION) ? rule.name() : rule.description();
    }

    private int getRulePriority() throws Exception {
        com.magic.rule.core.annotation.Rule rule = getRuleAnnotation();
        int priority = rule.priority();

        //So the priority from the method is higher than the class
        //If multiple priority methods are defined, the last one takes effect
        for (Method method : getTargetClass().getMethods()) {
            if (method.isAnnotationPresent(Priority.class)) {
                priority = (int) method.invoke(this.originalObject);
            }
        }

        return priority;
    }

    private Object compareToMethod(final Object[] args) throws Exception {
        Method compareToMethod = getRealMethod(RuleMethodEnum.compareTo.name());
        if (compareToMethod != null) {
            return compareToMethod.invoke(this.originalObject, args);
        } else {
            Rule otherRule = (Rule) args[0];
            return compareTo(otherRule);
        }

    }

    private boolean evaluateMethod(final Object[] args){
        try{
            Method method = this.getAnnotationMethod(Condition.class);
            return (boolean) method.invoke(this.originalObject,args);
        }catch (Exception e){
            log.error("Rule {} evaluate error, return false. ",getRuleName(),e);
        }
        return false;

    }

    private Object executeMethod(final Object[] args){
        try{
            Method method = this.getAnnotationMethod(Action.class);
            method.invoke(this.originalObject,args[0]);
        }catch (Exception e){
            log.error("Rule {} execute error",getRuleName(),e);
        }
        return null;
    }

    private boolean equalsMethod(final Object[] args) throws Exception {
        if (!(args[0] instanceof Rule)) {
            return false;
        }
        Rule otherRule = (Rule) args[0];
        int otherPriority = otherRule.getPriority();
        int priority = getRulePriority();
        if (priority != otherPriority) {
            return false;
        }
        String otherName = otherRule.getName();
        String name = getRuleName();
        if (!name.equals(otherName)) {
            return false;
        }
        String otherDescription = otherRule.getDescription();
        String description =  getRuleDescription();
        return !(description != null ? !description.equals(otherDescription) : otherDescription != null);
    }

    private int hashCodeMethod() throws Exception {
        int result   = getRuleName().hashCode();
        int priority = getRulePriority();
        String description = getRuleDescription();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + priority;
        return result;
    }

    private String toStringMethod() throws Exception {
        Method[] methods = getClass().getMethods();
        for (Method method : methods) {
            if ("toString".equals(method.getName())) {
                return (String) method.invoke(originalObject);
            }
        }
        return getRuleName();
    }

    //===============Auxiliary method=====================

    private Method getAnnotationMethod(Class clazz){
        Method[] methods = this.getTargetClass().getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(clazz)){
                return method;
            }
        }
        return null;
    }

    private int compareTo(final Rule otherRule) throws Exception {
        int otherPriority = otherRule.getPriority();
        int priority = getRulePriority();
        if (priority < otherPriority) {
            return -1;
        } else if (priority > otherPriority) {
            return 1;
        } else {
            String otherName = otherRule.getName();
            String name = getRuleName();
            return name.compareTo(otherName);
        }
    }

    /**
     * Get method from proxied object
     *
     * @param methodName
     * @return
     */
    private Method getRealMethod(String methodName) {
        Method[] methods = originalObject.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }


    private Class getTargetClass() {
        return this.originalObject.getClass();
    }

    private com.magic.rule.core.annotation.Rule getRuleAnnotation() {
        return ClassUtil.findAnnotation(com.magic.rule.core.annotation.Rule.class, getTargetClass());
    }

}

enum RuleMethodEnum{
    getName,
    getDescription,
    getPriority,
    compareTo,
    evaluate,
    execute,
    equals,
    hashCode,
    toString;

}
