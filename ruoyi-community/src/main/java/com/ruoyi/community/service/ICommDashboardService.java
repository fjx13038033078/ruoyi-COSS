package com.ruoyi.community.service;

import java.util.Map;

/**
 * 社区数据看板统计
 */
public interface ICommDashboardService
{
    /** KPI、事项热度、近7日新增办件曲线 */
    Map<String, Object> loadStatistics(int matterHotLimit, int trendDays);
}
