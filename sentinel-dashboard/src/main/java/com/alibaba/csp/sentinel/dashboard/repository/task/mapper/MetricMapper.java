package com.alibaba.csp.sentinel.dashboard.repository.task.mapper;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntitySummary;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface MetricMapper {

    @Select("<script>" +
            "select * from t_sentinel_metric" +
            "        <where> " +
            "           app = #{app} " +
            "           and  gmt_create  <![CDATA[ >= ]]>  #{start} " +
            "           and  gmt_create  <![CDATA[ <  ]]>  #{end} " +
            "        </where>" +
            "</script>")
    @Results(id = "BaseResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "app", column = "app"),
            @Result(property = "timestamp", column = "timestamp"),
            @Result(property = "resource", column = "resource"),
            @Result(property = "passQps", column = "pass_qps"),
            @Result(property = "successQps", column = "success_qps"),
            @Result(property = "blockQps", column = "block_qps"),
            @Result(property = "exceptionQps", column = "exception_qps"),
            @Result(property = "rt", column = "rt"),
            @Result(property = "count", column = "_count"),
            @Result(property = "resourceCode", column = "resource_code")
    })
    List<MetricEntity> getMetricEntities(@Param("app") String app, @Param("start") String start, @Param("end") String end);

    @Select("SELECT DISTINCT app FROM t_sentinel_metric")
    @ResultType(String.class)
    List<String> getAllApps();

    @Select("<script>" +
            "select COALESCE (SUM(pass_qps), 0) pass_qps, app from t_sentinel_metric" +
            "        <where> " +
            "           app = #{app} " +
            "           and  gmt_create  <![CDATA[ >= ]]>  #{start} " +
            "           and  gmt_create  <![CDATA[ <  ]]>  #{end} " +
            "        </where>" +
            " GROUP BY app " +
            "</script>")
    @Results(id = "BaseSummaryResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "gmtCreate", column = "gmt_create"),
            @Result(property = "gmtModified", column = "gmt_modified"),
            @Result(property = "app", column = "app"),
            @Result(property = "timestamp", column = "timestamp"),
            @Result(property = "resource", column = "resource"),
            @Result(property = "passQps", column = "pass_qps"),
            @Result(property = "successQps", column = "success_qps"),
            @Result(property = "blockQps", column = "block_qps"),
            @Result(property = "exceptionQps", column = "exception_qps"),
            @Result(property = "rt", column = "rt"),
            @Result(property = "count", column = "_count"),
            @Result(property = "summaryType", column = "summary_type"),
            @Result(property = "resourceCode", column = "resource_code")
    })
    MetricEntitySummary getPassQps(@Param("app") String app, @Param("start") String start, @Param("end") String end);

    @Insert({"<script>" +
            "INSERT INTO t_sentinel_metric_summary " +
            "       (pass_qps, app, summary_type, gmt_create) " +
            "VALUES  " +
            "       (#{passQps}, #{app}, #{summaryType}, DATE_FORMAT(#{start}, \"%Y-%m-%d %H\")  ) " +
            "</script>"})
    //@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertSummaryApp(@Param("summaryType") String summaryType, @Param("passQps") String passQps, @Param("app") String app);

    @Insert({"<script>" +
            "   INSERT INTO t_sentinel_metric_summary " +
            "       (pass_qps, app, summary_type, gmt_create) " +
            "       (   SELECT SUM(pass_qps) pass_qps, app,  #{summaryType}, DATE_FORMAT(#{start}, \"%Y-%m-%d %H\")" +
            "           FROM t_sentinel_metric " +
            "           WHERE " +
            "               gmt_create  <![CDATA[ >= ]]>  #{start}  " +
            "               AND gmt_create  <![CDATA[ <  ]]>  #{end}  " +
            "           GROUP BY app) " +
            "</script>"})
    //app-小时
    int insertSummaryAppBatchHour(@Param("summaryType") String summaryType, @Param("start") String start, @Param("end") String end);
    @Insert({"<script>" +
            "   INSERT INTO t_sentinel_metric_summary " +
            "       (pass_qps, app, resource, summary_type, gmt_create) " +
            "       (   SELECT SUM(pass_qps) pass_qps, app, resource, #{summaryType}, DATE_FORMAT(#{start}, \"%Y-%m-%d %H\")" +
            "           FROM t_sentinel_metric " +
            "           WHERE " +
            "               gmt_create  <![CDATA[ >= ]]>  #{start}  " +
            "               AND gmt_create  <![CDATA[ <  ]]>  #{end}  " +
            "           GROUP BY app, resource) " +
            "</script>"})
    //api-小时
    int insertSummaryApiBatchHour(@Param("summaryType") String summaryType, @Param("start") String start, @Param("end") String end);



    @Insert({"<script>" +
            "   INSERT INTO t_sentinel_metric_summary " +
            "       (pass_qps, app, summary_type, gmt_create) " +
            "       (   SELECT SUM(pass_qps) pass_qps, app,  #{summaryType}, DATE_FORMAT(#{start}, \"%Y-%m-%d\")" +
            "           FROM t_sentinel_metric_summary " +
            "           WHERE " +
            "               gmt_create  <![CDATA[ >= ]]>  #{start}  " +
            "               AND gmt_create  <![CDATA[ <  ]]>  #{end}  " +
            "               AND summary_type  =  0  " +
            "               AND resource  is null  " +
            "           GROUP BY app) " +
            "</script>"})
    //app-天
    int insertSummaryAppBatchDay(@Param("summaryType") String summaryType, @Param("start") String start, @Param("end") String end);
    @Insert({"<script>" +
            "   INSERT INTO t_sentinel_metric_summary " +
            "       (pass_qps, app, resource, summary_type, gmt_create) " +
            "       (   SELECT SUM(pass_qps) pass_qps, app, resource, #{summaryType}, DATE_FORMAT(#{start}, \"%Y-%m-%d\")" +
            "           FROM t_sentinel_metric_summary " +
            "           WHERE " +
            "               gmt_create  <![CDATA[ >= ]]>  #{start}  " +
            "               AND gmt_create  <![CDATA[ <  ]]>  #{end}  " +
            "               AND summary_type  =  0  " +
            "               AND resource  is not null  " +
            "           GROUP BY app, resource) " +
            "</script>"})
    //api-天
    int insertSummaryApiBatchDay(@Param("summaryType") String summaryType, @Param("start") String start, @Param("end") String end);



    @Delete({"<script>" +
            "DELETE FROM t_sentinel_metric WHERE gmt_create  <![CDATA[ < ]]>  #{start}  " +
            "</script>"})
    int deleteNDaysBeforeHistory(@Param("start") String start);


}
