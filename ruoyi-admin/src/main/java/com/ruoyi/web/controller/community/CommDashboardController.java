package com.ruoyi.web.controller.community;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.community.service.ICommDashboardService;

@RestController
public class CommDashboardController extends BaseController
{
    @Autowired
    private ICommDashboardService dashboardService;

    @PreAuthorize("@ss.hasPermi('community:dashboard:query')")
    @GetMapping("/community/dashboard/statistics")
    public AjaxResult statistics(Integer matterHotLimit, Integer trendDays)
    {
        int hl = matterHotLimit == null ? 8 : matterHotLimit;
        int td = trendDays == null ? 7 : trendDays;
        Map<String, Object> data = dashboardService.loadStatistics(hl, td);
        return success(data);
    }
}
