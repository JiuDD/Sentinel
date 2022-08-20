//package com.alibaba.csp.sentinel.dashboard.repository.task;
//
//import com.alibaba.csp.sentinel.dashboard.repository.task.mapper.MetricMapper;
//import com.alibaba.csp.sentinel.dashboard.util.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * description:  2022年8月份 《小时级 api》 、《天级 api》 的数据没有统计到 t_sentinel_metric_summary、此类用来在本地运行补数
// */
//@Component
//public class SummaryApiAccessTask2 implements CommandLineRunner {
//    private static final Logger logger = LoggerFactory.getLogger(SummaryApiAccessTask2.class);
//    @Resource
//    private MetricMapper metricMapper;
//
//
//    /**
//     * Description: 《小时级 api》 的数据没有统计到 t_sentinel_metric_summary，此方法用来补数
//     * @author: JiuDD
//     * date: 2022/8/17 18:31
//     */
//    @Override
//    public void run(String... args) throws Exception {
//        String a = "2022-08-01 00";
//        Date startDateHour = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH").parse(a);
//
//        while (true) {
//            if (startDateHour.after(new Date())) {
//                return;
//            }
//            Date endDateHour = DateUtil.getFutureMountHoursStart(startDateHour, 1);
//
//            SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String start = dateFormat.format(startDateHour);
//            String end = dateFormat.format(endDateHour);
//
//            //`summary_type` int(11) DEFAULT NULL COMMENT '聚合类型：0小时 1天',
//            System.out.println("小时-api级-统计开始:" + start);
//            int j = metricMapper.insertSummaryApiBatchHour("0", start, end);
//            System.out.println("insert api 数量 = "  + j);
//            System.out.println("小时-api级-统计结束:" + start);
//            startDateHour = endDateHour;
//        }
//    }
//
//    /**
//     * Description: 《天级 api》 的数据没有统计到 t_sentinel_metric_summary，此方法用来补数
//     * @author: JiuDD
//     * date: 2022/8/17 19:31
//     */
//    @Override
//    public void run(String... args) throws Exception {
//        String a = "2022-08-01 00:00:00";
//        Date startDate = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(a);
//
//        while (true) {
//            if (startDate.after(new Date())) {
//                return;
//            }
//            Date endDate = DateUtil.getFutureMountDaysStart(startDate, 1);
//
//            SimpleDateFormat dateFormat = DateUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String start = dateFormat.format(startDate);
//            String end = dateFormat.format(endDate);
//
//            //`summary_type` int(11) DEFAULT NULL COMMENT '聚合类型：0小时 1天',
//            System.out.println("天-api级-统计开始:" + startDate);
//            int j = metricMapper.insertSummaryApiBatchDay("1", start, end);
//            System.out.println("insert api 数量 = "  + j);
//            System.out.println("天-api级-统计结束");
//            startDate = endDate;
//        }
//    }
//}
