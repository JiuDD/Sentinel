package com.alibaba.csp.sentinel.dashboard.repository.task.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.alibaba.csp.sentinel.dashboard.repository.task.mapper"})
public class MapperConfig {
}
