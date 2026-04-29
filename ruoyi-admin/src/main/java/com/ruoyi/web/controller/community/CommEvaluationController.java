package com.ruoyi.web.controller.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.community.domain.CommEvaluation;
import com.ruoyi.community.service.ICommEvaluationService;

@RestController
public class CommEvaluationController extends BaseController
{
    @Autowired
    private ICommEvaluationService evaluationService;

    @PreAuthorize("@ss.hasPermi('community:evaluation:list')")
    @GetMapping("/community/evaluation/list")
    public TableDataInfo adminList(CommEvaluation query)
    {
        startPage();
        List<CommEvaluation> list = evaluationService.selectEvaluationList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @Log(title = "portal evaluation", businessType = BusinessType.INSERT)
    @PostMapping("/portal/evaluation")
    public AjaxResult portalEvaluate(@Validated @RequestBody CommEvaluation row)
    {
        return toAjax(evaluationService.submitEvaluation(row, SecurityUtils.getUserId()));
    }
}
