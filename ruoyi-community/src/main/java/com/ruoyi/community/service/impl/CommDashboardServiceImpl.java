package com.ruoyi.community.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.community.mapper.CommApplyMapper;
import com.ruoyi.community.mapper.CommDashboardMapper;
import com.ruoyi.community.mapper.CommEvaluationMapper;
import com.ruoyi.community.service.ICommDashboardService;

/**
 * 看板统计数据聚合
 */
@Service
public class CommDashboardServiceImpl implements ICommDashboardService
{
    @Autowired
    private CommDashboardMapper dashboardMapper;

    @Autowired
    private CommApplyMapper applyMapper;

    @Autowired
    private CommEvaluationMapper evaluationMapper;

    @Override
    public Map<String, Object> loadStatistics(int matterHotLimit, int trendDays)
    {
        Map<String, Object> out = new HashMap<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date beginOfDay = c.getTime();

        int totalApply = dashboardMapper.countAllApply();
        int todayNew = dashboardMapper.countApplyCreatedAfter(beginOfDay);
        int pending = dashboardMapper.countPendingApply();
        Double avgMin = dashboardMapper.avgMinutesFromCreateToFinish();
        Double avgScore = evaluationMapper.selectAvgScore();
        Double satisfactionPct = null;
        if (avgScore != null)
        {
            satisfactionPct = BigDecimal.valueOf(avgScore).multiply(BigDecimal.valueOf(20))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        Map<String, Object> kpi = new HashMap<>();
        kpi.put("totalApply", totalApply);
        kpi.put("todayNew", todayNew);
        kpi.put("pending", pending);
        kpi.put("avgHandleMinutes", avgMin);
        if (avgMin != null)
        {
            kpi.put("avgHandleHours", BigDecimal.valueOf(avgMin).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP).doubleValue());
        }
        kpi.put("avgScore", avgScore);
        kpi.put("satisfactionPercent", satisfactionPct);
        out.put("kpi", kpi);

        out.put("matterHot", applyMapper.countApplyGroupByMatter(Math.max(matterHotLimit, 8)));
        out.put("dailyTrend", dashboardMapper.dailyNewApplyTrend(trendDays > 0 ? trendDays : 7));
        return out;
    }
}
