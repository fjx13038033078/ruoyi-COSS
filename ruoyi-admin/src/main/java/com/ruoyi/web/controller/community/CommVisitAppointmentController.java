package com.ruoyi.web.controller.community;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.community.domain.CommVisitAppointment;
import com.ruoyi.community.service.ICommVisitAppointmentService;

@RestController
public class CommVisitAppointmentController extends BaseController
{
    @Autowired
    private ICommVisitAppointmentService visitService;

    @PreAuthorize("@ss.hasPermi('community:visit:list')")
    @GetMapping("/community/visit/list")
    public TableDataInfo adminList(CommVisitAppointment query)
    {
        startPage();
        List<CommVisitAppointment> list = visitService.selectVisitList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('community:visit:list')")
    @GetMapping("/community/visit/{visitId}")
    public AjaxResult adminDetail(@PathVariable Long visitId)
    {
        return success(visitService.selectByVisitId(visitId));
    }

    public static class CompleteBo
    {
        @NotNull
        private Long visitId;

        @NotBlank
        private String summary;

        public Long getVisitId()
        {
            return visitId;
        }

        public void setVisitId(Long visitId)
        {
            this.visitId = visitId;
        }

        public String getSummary()
        {
            return summary;
        }

        public void setSummary(String summary)
        {
            this.summary = summary;
        }
    }

    public static class AcceptVisitBo
    {
        @NotNull
        private Long visitId;

        public Long getVisitId()
        {
            return visitId;
        }

        public void setVisitId(Long visitId)
        {
            this.visitId = visitId;
        }
    }

    @PreAuthorize("@ss.hasPermi('community:visit:handle')")
    @Log(title = "visit accept", businessType = BusinessType.UPDATE)
    @PostMapping("/community/visit/accept")
    public AjaxResult accept(@Validated @RequestBody AcceptVisitBo bo)
    {
        Long uid = SecurityUtils.getUserId();
        String nick = SecurityUtils.getUsername();
        visitService.acceptVisit(bo.getVisitId(), uid, nick);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('community:visit:handle')")
    @Log(title = "visit complete", businessType = BusinessType.UPDATE)
    @PostMapping("/community/visit/complete")
    public AjaxResult complete(@Validated @RequestBody CompleteBo bo)
    {
        Long uid = SecurityUtils.getUserId();
        String nick = SecurityUtils.getUsername();
        visitService.completeVisit(bo.getVisitId(), bo.getSummary(), uid, nick);
        return success();
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @Log(title = "portal visit submit", businessType = BusinessType.INSERT)
    @PostMapping("/portal/visit")
    public AjaxResult portalSubmit(@Validated @RequestBody CommVisitAppointment body)
    {
        body.setApplicantId(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(body.getApplicantName()))
        {
            body.setApplicantName(SecurityUtils.getLoginUser().getUser().getNickName());
        }
        if (StringUtils.isEmpty(body.getPhone()))
        {
            body.setPhone(SecurityUtils.getLoginUser().getUser().getPhonenumber());
        }
        return toAjax(visitService.insertVisit(body));
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @GetMapping("/portal/visit/my/list")
    public AjaxResult portalMyVisits()
    {
        return success(visitService.selectMyVisits(SecurityUtils.getUserId()));
    }
}
