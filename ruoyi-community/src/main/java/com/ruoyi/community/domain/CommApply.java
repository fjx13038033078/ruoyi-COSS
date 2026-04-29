package com.ruoyi.community.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.BaseEntity;

/** comm_apply (create_time/update_time inherited from BaseEntity for query range) */
public class CommApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long applyId;

    private String applyNo;

    @NotNull(message = "matterId required")
    private Long matterId;

    private String matterName;

    private Long applicantId;

    @NotBlank(message = "applicantName required")
    private String applicantName;

    private String idCard;

    @NotBlank(message = "phone required")
    private String phone;

    private String applyRemark;

    /** 0 pending 1 rejected 2 processing 3 done */
    private String status;

    private String rejectReason;

    private String opinion;

    private String resultFileUrl;

    private Long handlerId;

    private String handlerName;

    private Date assignTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    private List<CommApplyAttachment> attachmentList;

    public Long getApplyId()
    {
        return applyId;
    }

    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public String getApplyNo()
    {
        return applyNo;
    }

    public void setApplyNo(String applyNo)
    {
        this.applyNo = applyNo;
    }

    public Long getMatterId()
    {
        return matterId;
    }

    public void setMatterId(Long matterId)
    {
        this.matterId = matterId;
    }

    public String getMatterName()
    {
        return matterName;
    }

    public void setMatterName(String matterName)
    {
        this.matterName = matterName;
    }

    public Long getApplicantId()
    {
        return applicantId;
    }

    public void setApplicantId(Long applicantId)
    {
        this.applicantId = applicantId;
    }

    public String getApplicantName()
    {
        return applicantName;
    }

    public void setApplicantName(String applicantName)
    {
        this.applicantName = applicantName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getApplyRemark()
    {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark)
    {
        this.applyRemark = applyRemark;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRejectReason()
    {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason)
    {
        this.rejectReason = rejectReason;
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

    public Long getHandlerId()
    {
        return handlerId;
    }

    public void setHandlerId(Long handlerId)
    {
        this.handlerId = handlerId;
    }

    public String getHandlerName()
    {
        return handlerName;
    }

    public void setHandlerName(String handlerName)
    {
        this.handlerName = handlerName;
    }

    public Date getAssignTime()
    {
        return assignTime;
    }

    public void setAssignTime(Date assignTime)
    {
        this.assignTime = assignTime;
    }

    public Date getHandleTime()
    {
        return handleTime;
    }

    public void setHandleTime(Date handleTime)
    {
        this.handleTime = handleTime;
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }

    public List<CommApplyAttachment> getAttachmentList()
    {
        return attachmentList;
    }

    public void setAttachmentList(List<CommApplyAttachment> attachmentList)
    {
        this.attachmentList = attachmentList;
    }

    @JsonIgnore
    public boolean allowResubmit()
    {
        return "1".equals(status);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("applyNo", applyNo).append("status", status).toString();
    }
}
