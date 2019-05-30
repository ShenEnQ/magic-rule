package com.magic.rule.core;

import com.magic.rule.core.api.Rule;
import com.magic.rule.core.handler.RuleProxyHandler;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassScanTest extends BaseTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${client.rule.package}")
    private String rulePackage;

    @Test
    public void testScan() throws IOException {

        rulePackage = rulePackage.replaceAll("\\.","/");

        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = resolver.getResources("classpath*:" + rulePackage + "/**/*.class");

        List<String> classList = new ArrayList<>();
        for (Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            classList.add(reader.getClassMetadata().getClassName());
        }
    }

    @Test
    public void testProxyRule(){
        List<Rule> rules = RuleProxyHandler.createRules(rulePackage);
        for (Rule rule : rules) {
            System.out.println(rule.getName()+","+rule.getDescription() + "," + rule.getPriority());
        }
    }

}
