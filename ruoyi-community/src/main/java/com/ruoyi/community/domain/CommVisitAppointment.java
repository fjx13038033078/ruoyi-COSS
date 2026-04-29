package com.ruoyi.community.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

/** comm_visit_appointment */
public class CommVisitAppointment
{
    private Long visitId;

    private Long applicantId;

    private String applicantName;

    private String phone;

    @NotBlank(message = "address required")
    private String address;

    private String matterDesc;

    @NotNull(message = "expectedTime required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedTime;

    /** 0 pending 1 accepted 2 done */
    private String status;

    private Long handlerId;

    private String handlerName;

    private String summary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    public Long getVisitId()
    {
        return visitId;
    }

    public void setVisitId(Long visitId)
    {
        this.visitId = visitId;
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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getMatterDesc()
    {
        return matterDesc;
    }

    public void setMatterDesc(String matterDesc)
    {
        this.matterDesc = matterDesc;
    }

    public Date getExpectedTime()
    {
        return expectedTime;
    }

    public void setExpectedTime(Date expectedTime)
    {
        this.expectedTime = expectedTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }
}
