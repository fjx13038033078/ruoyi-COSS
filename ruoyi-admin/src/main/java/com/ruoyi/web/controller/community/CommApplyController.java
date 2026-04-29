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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.community.domain.CommApply;
import com.ruoyi.community.domain.CommApplyAttachment;
import com.ruoyi.community.service.ICommApplyService;

@RestController
public class CommApplyController extends BaseController
{
    @Autowired
    private ICommApplyService applyService;

    @PreAuthorize("@ss.hasPermi('community:apply:list')")
    @GetMapping("/community/apply/list")
    public TableDataInfo adminList(CommApply query)
    {
        startPage();
        List<CommApply> list = applyService.selectCommApplyList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('community:apply:query')")
    @GetMapping("/community/apply/{applyId}")
    public AjaxResult adminDetail(@PathVariable Long applyId)
    {
        return success(applyService.selectCommApplyById(applyId, true));
    }

    public static class RejectBo
    {
        @NotNull
        private Long applyId;

        @NotBlank
        private String rejectReason;

        public Long getApplyId()
        {
            return applyId;
        }

        public void setApplyId(Long applyId)
        {
            this.applyId = applyId;
        }

        public String getRejectReason()
        {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason)
        {
            this.rejectReason = rejectReason;
        }
    }

    public static class FinishBo
    {
        @NotNull
        private Long applyId;
        private String opinion;
        private String resultFileUrl;

        public Long getApplyId()
        {
            return applyId;
        }

        public void setApplyId(Long applyId)
        {
            this.applyId = applyId;
        }

        public String getOpinion()
        {
            return opinion;
        }

        public void setOpinion(String opinion)
        {
            this.opinion = opinion;
        }

        public String getResultFileUrl()
        {
            return resultFileUrl;
        }

        public void setResultFileUrl(String resultFileUrl)
        {
            this.resultFileUrl = resultFileUrl;
        }
    }

    public static class AcceptBo
    {
        @NotNull
        private Long applyId;

        public Long getApplyId()
        {
            return applyId;
        }

        public void setApplyId(Long applyId)
        {
            this.applyId = applyId;
        }
    }

    @PreAuthorize("@ss.hasPermi('community:apply:audit')")
    @Log(title = "reject apply", businessType = BusinessType.UPDATE)
    @PostMapping("/community/apply/reject")
    public AjaxResult reject(@Validated @RequestBody RejectBo bo)
    {
        Long uid = SecurityUtils.getUserId();
        String nick = SecurityUtils.getUsername();
        applyService.rejectApply(bo.getApplyId(), bo.getRejectReason(), uid, nick);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('community:apply:audit')")
    @Log(title = "accept apply", businessType = BusinessType.UPDATE)
    @PostMapping("/community/apply/accept")
    public AjaxResult accept(@Validated @RequestBody AcceptBo bo)
    {
        Long uid = SecurityUtils.getUserId();
        String nick = SecurityUtils.getUsername();
        applyService.acceptApply(bo.getApplyId(), uid, nick);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('community:apply:audit')")
    @Log(title = "finish apply", businessType = BusinessType.UPDATE)
    @PostMapping("/community/apply/finish")
    public AjaxResult finish(@Validated @RequestBody FinishBo bo)
    {
        Long uid = SecurityUtils.getUserId();
        String nick = SecurityUtils.getUsername();
        applyService.finishApply(bo.getApplyId(), bo.getOpinion(), bo.getResultFileUrl(), uid, nick);
        return success();
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @Log(title = "portal apply submit", businessType = BusinessType.INSERT)
    @PostMapping("/portal/apply")
    public AjaxResult portalSubmit(@Validated @RequestBody CommApply body)
    {
        Long uid = SecurityUtils.getUserId();
        body.setApplicantId(uid);
        List<CommApplyAttachment> atts = body.getAttachmentList();
        return toAjax(applyService.insertPortalSubmit(body, atts));
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @Log(title = "portal apply resubmit", businessType = BusinessType.UPDATE)
    @PutMapping("/portal/apply/resubmit/{applyId}")
    public AjaxResult portalResubmit(@PathVariable Long applyId,
            @Validated @RequestBody CommApply body)
    {
        Long uid = SecurityUtils.getUserId();
        List<CommApplyAttachment> atts = body.getAttachmentList();
        return toAjax(applyService.updateResubmit(applyId, uid, body, atts));
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @GetMapping("/portal/apply/my/list")
    public AjaxResult portalMyList()
    {
        return success(applyService.selectMyApplyList(SecurityUtils.getUserId()));
    }

    @PreAuthorize("@ss.hasRole('resident')")
    @GetMapping("/portal/apply/my/{applyId}")
    public AjaxResult portalMyDetail(@PathVariable Long applyId)
    {
        CommApply row = applyService.selectCommApplyForResident(applyId, SecurityUtils.getUserId(), true);
        if (row == null)
        {
            return error("record not found or forbidden");
        }
        return success(row);
    }
}
