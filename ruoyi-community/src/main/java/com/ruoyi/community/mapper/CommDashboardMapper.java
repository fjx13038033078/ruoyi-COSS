package com.ruoyi.community.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/** dashboard aggregations */
public interface CommDashboardMapper
{
    int countAllApply();

    int countApplyCreatedAfter(@Param("from") Date from);

    int countPendingApply();

    Double avgMinutesFromCreateToFinish();

    List<Map<String, Object>> dailyNewApplyTrend(@Param("days") int days);
}
