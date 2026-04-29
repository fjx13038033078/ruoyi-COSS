package com.ruoyi.community.domain;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.core.domain.BaseEntity;

/** comm_matter */
public class CommMatter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long matterId;

    @NotBlank(message = "matter name required")
    private String matterName;

    /** dict: comm_matter_category */
    private String category;

    /** dict: comm_matter_priority */
    private String priority;

    private Long deptId;

    /** required proofs list text */
    private String requiredDocs;

    private Integer expectDays;

    /** rich text workflow */
    private String processDesc;

    /** 0 normal 1 disabled */
    private String status;

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

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getRequiredDocs()
    {
        return requiredDocs;
    }

    public void setRequiredDocs(String requiredDocs)
    {
        this.requiredDocs = requiredDocs;
    }

    public Integer getExpectDays()
    {
        return expectDays;
    }

    public void setExpectDays(Integer expectDays)
    {
        this.expectDays = expectDays;
    }

    public String getProcessDesc()
    {
        return processDesc;
    }

    public void setProcessDesc(String processDesc)
    {
        this.processDesc = processDesc;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("matterId", getMatterId())
                .append("matterName", getMatterName())
                .toString();
    }
}
