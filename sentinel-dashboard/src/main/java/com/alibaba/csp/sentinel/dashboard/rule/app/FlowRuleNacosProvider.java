package com.alibaba.csp.sentinel.dashboard.rule.app;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.config.NacosConstants;
import com.alibaba.csp.sentinel.dashboard.rule.config.NacosPropertiesConfiguration;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 动态从 Nacos 配置中心获取流控规则
 * @author: JiuDongDong
 * date: 2022/8/19.
 */
@Service
public class FlowRuleNacosProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {
    private static Logger logger = LoggerFactory.getLogger(FlowRuleNacosProvider.class);
    @Autowired
    private NacosPropertiesConfiguration nacosProperties;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<String, List<FlowRuleEntity>> converter;


    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        //拼接配置id, 规则：dataId= appName + "-sentinel-flow".
        // "-sentinel-flow"是自定义的后缀，微服务配置sentinel的nacos数据源时，dataId要以此后缀结尾，
        // 示例：spring.cloud.sentinel.datasource -nacos.data-id: find-api-sentinel-flow  (参考find_alibaba项目的find-api模块的配置)
        String dataId = new StringBuilder(appName).append(NacosConstants.DATA_ID_POSTFIX).toString();
        //这里的参数不需要指定namespace，因为 构造ConfigService实例时，是基于namespace的，参考：com.alibaba.csp.sentinel.dashboard.rule.config.NacosConfiguration.nacosConfigService
        String rules = configService.getConfig(dataId, nacosProperties.getGroupId(), 3000);
        logger.info("pull FlowRule from Nacos Config Center, appName= {}, dataId= {}, rules from nacos= {}", appName, dataId, rules);
        if (StringUtils.isEmpty(rules)) {
            return new ArrayList<>();
        }
        List<FlowRuleEntity> flowRuleEntities = converter.convert(rules);
        return flowRuleEntities;
    }
}
