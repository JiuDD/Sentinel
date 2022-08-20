package com.alibaba.csp.sentinel.dashboard.repository.task;

import com.alibaba.csp.sentinel.dashboard.repository.task.mapper.MetricMapper;
import com.alibaba.csp.sentinel.dashboard.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description:  定时把秒级日志汇总为小时
 */
@Component
public class SummaryApiAccessTask {
    private static final Logger logger = LoggerFactory.getLogger(SummaryApiAccessTask.class);
    @Resource
    private MetricMapper metricMapper;


    /**
     * Description: 定时把秒级日志汇总为小时, 包括 app级、api级
     * @author: JiuDD
     */
    @Scheduled(cron = "10 5 * * * ?")//必需 每hour 执行一次，不可变更
    public void insertSummaryAppBatchHour() {
        Date startDate = DateUtil.getFutureMountHoursStart(new Date(), -1);
        Date endDate = DateUtil.getFutureMountHoursStart(new Date(), 0);
        SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = dateFormat.format(startDate);
        String end = dateFormat.format(endDate);

        //start = "2021-11-03 00:00:00";
        //end = "2021-11-03 19:05:56";

        System.out.println("小时-app级-统计开始:" + startDate);
        final long startTime1 = System.currentTimeMillis();
        /* 方式一  批量直接插入 */
        //`summary_type` int(11) DEFAULT NULL COMMENT '聚合类型：0小时 1天',
        int i = metricMapper.insertSummaryAppBatchHour("0", start, end);
        System.out.println("insert app 数量 = "  + i);
        final long endTime1 = System.currentTimeMillis();
        /* 方式二  按app挨个insert ，效率低 */
        //获取所有应用: ce01-sop-service ce07-doorcloud ce10-mobile-shopping ce16-api-gateway ce16-wholenetmall ce17-api-operating ms02-chengxing-service sentinel-dashboard
        //List<String> allApps = metricMapper.getAllApps();
        //for (String app : allApps) {
        //    MetricEntitySummary metricEntities = metricMapper.getPassQps(app, start, end);
        //    if (null != metricEntities) {
        //        try {
        //            metricMapper.insertSummaryApp("0", metricEntities.getPassQps() + "", metricEntities.getApp());
        //        } catch (Exception e) {
        //            System.out.println(metricEntities);
        //        }
        //    }
        //}
        System.out.println("小时-app级-统计结束, 耗时: " + (endTime1 - startTime1)/1000 + " 秒");
        System.out.println();


        System.out.println("小时-api级-统计开始:" + startDate);
        final long startTime2 = System.currentTimeMillis();
        int j = metricMapper.insertSummaryApiBatchHour("0", start, end);
        System.out.println("insert api 数量 = "  + j);
        final long endTime2 = System.currentTimeMillis();
        System.out.println("小时-api级-统计结束, 耗时: " + (endTime2 - startTime2)/1000 + " 秒");
    }


    /**
     * Description: 定时把小时级日志汇总为天, 包括 app级、api级
     * @author: JiuDD
     */
    @Scheduled(cron = "10 15 2 * * ?")
    public void insertSummaryAppBatchDay() {
        Date startDate = DateUtil.getFutureMountDaysStart(new Date(), -1);
        Date endDate = DateUtil.getFutureMountDaysStart(new Date(), 0);
        SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = dateFormat.format(startDate);
        String end = dateFormat.format(endDate);

        //start = "2021-11-03 00:00:00";
        //end = "2021-11-04 00:00:00";

        System.out.println("天-app级-统计开始:" + startDate);
        final long startTime3 = System.currentTimeMillis();
        //`summary_type` int(11) DEFAULT NULL COMMENT '聚合类型：0小时 1天',
        int i = metricMapper.insertSummaryAppBatchDay("1", start, end);
        System.out.println("insert app 数量 = "  + i);
        final long endTime3 = System.currentTimeMillis();
        System.out.println("天-app级-统计结束, 耗时: " + (endTime3 - startTime3)/1000 + " 秒");

        System.out.println();

        System.out.println("天-api级-统计开始:" + startDate);
        final long startTime4 = System.currentTimeMillis();
        int j = metricMapper.insertSummaryApiBatchDay("1", start, end);
        System.out.println("insert api 数量 = "  + j);
        final long endTime4 = System.currentTimeMillis();
        System.out.println("天-api级-统计结束, 耗时: " + (endTime4 - startTime4)/1000 + " 秒");
    }


    /**
     * Description: 定时删除15天前的秒级历史数据   每天凌晨03:35:35 执行
     * @author: JiuDD
     */
    @Scheduled(cron = "35 35 3 * * ?")
    public void delete20DaysBeforeHistory() {
        Date startDate = DateUtil.getFutureMountDaysStart(new Date(), -31);
        SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String start = dateFormat.format(startDate);

        System.out.println("删除 14天前的秒级历史数据 开始");
        metricMapper.deleteNDaysBeforeHistory(start);
        System.out.println("删除 14天前的秒级历史数据 结束");
    }

}
