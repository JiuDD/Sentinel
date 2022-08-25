package com.alibaba.csp.sentinel.dashboard.rule.app;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.config.NacosConstants;
import com.alibaba.csp.sentinel.dashboard.rule.config.NacosPropertiesConfiguration;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: 规则发布类。  在 Sentinel Dashboard 上修改完配置之后，此发布类将 新的流控规则 持久化到 Nacos 中。
 * @author: JiuDongDong
 * date: 2022/8/19.
 */
@Service
public class FlowRuleNacosPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {
    private static Logger logger = LoggerFactory.getLogger(FlowRuleNacosPublisher.class);
    @Autowired
    private NacosPropertiesConfiguration nacosProperties;
    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<FlowRuleEntity>, String> converter;

    @Override
    public void publish(String appName, List<FlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(appName, "appName cannot be empty");
        if (null == rules) {
            return;
        }
        //拼接配置id, 规则：dataId= appName + "-sentinel-flow".
        // "-sentinel-flow"是自定义的后缀，微服务配置sentinel的nacos数据源时，dataId要以此后缀结尾，
        // 示例：spring.cloud.sentinel.datasource -nacos.data-id: find-api-sentinel-flow  (参考find_alibaba项目的find-api模块的配置)
        String dataId = new StringBuilder(appName).append(NacosConstants.DATA_ID_POSTFIX).toString();
        String convert = converter.convert(rules);
        //这里的参数不需要指定namespace，因为 构造ConfigService实例时，是基于namespace的，参考：com.alibaba.csp.sentinel.dashboard.rule.config.NacosConfiguration.nacosConfigService
        configService.publishConfig(dataId, nacosProperties.getGroupId(), convert);
    }

}
