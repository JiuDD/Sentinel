package com.alibaba.csp.sentinel.dashboard.repository.metric;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricDto;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://www.cnblogs.com/yinjihuan/p/10574998.html
 * https://www.cnblogs.com/cdfive2018/p/9838577.html
 * https://blog.csdn.net/wk52525/article/details/104587239/
 * https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel
 *
 */
//@Transactional
@Repository("mysqlMetricsRepository")
public class MysqlMetricsRepository implements MetricsRepository<MetricEntity> {

    @PersistenceContext
    private EntityManager em;

    /**
     * Description: 把单个的监控信息持久化
     *  MySQL的默认锁等待时间为50秒。这里定义timeout为500秒，超过50秒即可，保证当前事务最多可耗时50秒
     * @author: JiuDD
     * date: 2022/8/19
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 500)
    @Override
    public void save(MetricEntity metric) {
        if (metric == null || StringUtil.isBlank(metric.getApp())) {
            return;
        }

        MetricDto metricDto = new MetricDto();
        BeanUtils.copyProperties(metric, metricDto);

        //EntityTransaction transaction = em.getTransaction();    //获取事务管理对象
        //transaction.begin();    //开启事务
        em.persist(metricDto);
        //transaction.commit();   //提交事务
        //em.close();             //关闭实体管理
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 500)
    @Override
    public void saveAll(Iterable<MetricEntity> metrics) {
        if (metrics == null) {
            return;
        }

        metrics.forEach(this::save);
    }

    @Override
    public List<MetricEntity> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime) {
        List<MetricEntity> results = new ArrayList<>();
        if (StringUtil.isBlank(app)) {
            return results;
        }

        if (StringUtil.isBlank(resource)) {
            return results;
        }

        StringBuilder hql = new StringBuilder();
        hql.append("FROM MetricDto");
        hql.append(" WHERE app=:app");
        hql.append(" AND resource=:resource");
        hql.append(" AND timestamp>=:startTime");
        hql.append(" AND timestamp<=:endTime");

        Query query = em.createQuery(hql.toString());
        query.setParameter("app", app);
        query.setParameter("resource", resource);
        query.setParameter("startTime", Date.from(Instant.ofEpochMilli(startTime)));
        query.setParameter("endTime", Date.from(Instant.ofEpochMilli(endTime)));

        List<MetricDto> metricDtos = query.getResultList();
        if (CollectionUtils.isEmpty(metricDtos)) {
            return results;
        }

        for (MetricDto metricDto : metricDtos) {
            MetricEntity metricEntity = new MetricEntity();
            BeanUtils.copyProperties(metricDto, metricEntity);
            results.add(metricEntity);
        }
        return results;
    }

    @Override
    public List<String> listResourcesOfApp(String app) {
        List<String> results = new ArrayList<>();
        if (StringUtil.isBlank(app)) {
            return results;
        }

        StringBuilder hql = new StringBuilder();
        hql.append("FROM MetricDto");
        hql.append(" WHERE app=:app");
        hql.append(" AND timestamp>=:startTime");

        long startTime = System.currentTimeMillis() - 1000 * 60;
        Query query = em.createQuery(hql.toString());
        query.setParameter("app", app);
        query.setParameter("startTime", Date.from(Instant.ofEpochMilli(startTime)));

        List<MetricDto> metricDtos = query.getResultList();
        if (CollectionUtils.isEmpty(metricDtos)) {
            return results;
        }

        List<MetricEntity> metricEntities = new ArrayList<>();
        for (MetricDto metricDto : metricDtos) {
            MetricEntity metricEntity = new MetricEntity();
            BeanUtils.copyProperties(metricDto, metricEntity);
            metricEntities.add(metricEntity);
        }

        Map<String, MetricEntity> resourceCount = new HashMap<>(32);

        for (MetricEntity metricEntity : metricEntities) {
            String resource = metricEntity.getResource();
            if (resourceCount.containsKey(resource)) {
                MetricEntity oldEntity = resourceCount.get(resource);
                oldEntity.addPassQps(metricEntity.getPassQps());
                oldEntity.addRtAndSuccessQps(metricEntity.getRt(), metricEntity.getSuccessQps());
                oldEntity.addBlockQps(metricEntity.getBlockQps());
                oldEntity.addExceptionQps(metricEntity.getExceptionQps());
                oldEntity.addCount(1);
            } else {
                resourceCount.put(resource, MetricEntity.copyOf(metricEntity));
            }
        }

        // Order by last minute b_qps DESC.
        return resourceCount.entrySet()
                .stream()
                .sorted((o1, o2) -> {
                    MetricEntity e1 = o1.getValue();
                    MetricEntity e2 = o2.getValue();
                    int t = e2.getBlockQps().compareTo(e1.getBlockQps());
                    if (t != 0) {
                        return t;
                    }
                    return e2.getPassQps().compareTo(e1.getPassQps());
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}