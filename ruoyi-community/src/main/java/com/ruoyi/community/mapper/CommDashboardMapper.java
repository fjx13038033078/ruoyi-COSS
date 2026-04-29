package com.ruoyi.community.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 数据看板聚合查询（无独立业务表），主要统计 {@code comm_apply}。<br/>
 * 平均星级另见 {@link CommEvaluationMapper#selectAvgScore()}。<br/>
 * 对应 Mapper XML：<code>mapper/community/CommDashboardMapper.xml</code>。
 */
public interface CommDashboardMapper
{
    /** 申办总件数。 */
    int countAllApply();

    /**
     * 指定时间之后创建的申办数；通常传入「当日 0 点」计算今日新增。
     *
     * @param from 时间下限（含）
     */
    int countApplyCreatedAfter(@Param("from") Date from);

    /**
     * 待处理量：状态为待初审或办理中（{@code status in ('0','2')}），与业务字典一致。
     */
    int countPendingApply();

    /**
     * 已办结办件从创建到办结的平均分钟数（仅 {@code status='3'} 且起止时间非空）。<br/>
     * MySQL 使用 {@code TIMESTAMPDIFF(MINUTE, ...)}，无有效数据时为 null。
     */
    Double avgMinutesFromCreateToFinish();

    /**
     * 近若干天<strong>每日新建</strong>申办量，返回折线图用序列。<br/>
     * XML 中以 {@code date_sub(curdate(), interval (days-1) day)} 截取窗口，按日历日分组。
     *
     * @param days 回溯天数（含今天）
     * @return 每行含 {@code dt}（日期字符串）、{@code cnt}
     */
    List<Map<String, Object>> dailyNewApplyTrend(@Param("days") int days);
}
