package com.alibaba.csp.sentinel.dashboard.rule.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * description: 加载Nacos的地址等信息，用于 sentinel 外部Nacos数据源，放置 流控配置信息
 * @author: JiuDongDong
 * date: 2022/8/19.
 */
@ConfigurationProperties(prefix = "sentinel.nacos")
public class NacosPropertiesConfiguration {
    private String serverAddr;
    private String dataId;
    private String groupId = NacosConstants.DEFAULT_GROUP;
    private String namespace;

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
