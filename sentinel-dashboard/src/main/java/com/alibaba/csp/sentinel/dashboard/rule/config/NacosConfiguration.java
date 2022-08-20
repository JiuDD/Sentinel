package com.alibaba.csp.sentinel.dashboard.rule.config;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * description: Nacos相关配置
 * @author: JiuDongDong
 * date: 2022/8/19.
 */
@EnableConfigurationProperties(NacosPropertiesConfiguration.class)
@Configuration
public class NacosConfiguration {

    /**
     * Description: 注入 Converter 转换器，将 FlowRuleEntity 转换成 FlowRule
     * @author: JiuDD
     * @return com.alibaba.csp.sentinel.datasource.Converter<java.util.List<com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity>,java.lang.String>
     * date: 2022/8/19 11:00
     */
    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    /**
     * Description: 注入 Converter 转换器，将 FlowRule 反向转换成 FlowRuleEntity
     * @author: JiuDD
     * @return com.alibaba.csp.sentinel.datasource.Converter<java.lang.String,java.util.List<com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity>>
     * date: 2022/8/19 11:01
     */
    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    /**
     * Description: 注入 Nacos 配置服务 ConfigService，用于使用nacos的API去操作Nacos
     * @author: JiuDD
     * @param nacosProperties nacos配置参数
     * @return com.alibaba.nacos.api.config.ConfigService
     * date: 2022/8/19 11:02
     */
    @Bean
    public ConfigService nacosConfigService(NacosPropertiesConfiguration nacosProperties) throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosProperties.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, nacosProperties.getNamespace());
        return ConfigFactory.createConfigService(properties);
    }


}
