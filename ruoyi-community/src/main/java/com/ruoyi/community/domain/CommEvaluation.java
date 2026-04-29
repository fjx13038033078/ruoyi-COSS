package com.ruoyi.community.domain;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

/** comm_evaluation */
public class CommEvaluation
{
    private Long evaluationId;

    @NotNull(message = "applyId required")
    private Long applyId;

    private Long applicantId;

    @NotNull(message = "score required")
    @Min(1)
    @Max(5)
    private Integer score;

    @NotBlank(message = "evaluationLevel required")
    private String evaluationLevel;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String matterName;

    private String applyNo;

    public Long getEvaluationId()
    {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId)
    {
        this.evaluationId = evaluationId;
    }

    public Long getApplyId()
    {
        return applyId;
    }

    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public Long getApplicantId()
    {
        return applicantId;
    }

    public void setApplicantId(Long applicantId)
    {
        this.applicantId = applicantId;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public String getEvaluationLevel()
    {
        return evaluationLevel;
    }

    public void setEvaluationLevel(String evaluationLevel)
    {
        this.evaluationLevel = evaluationLevel;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getMatterName()
    {
        return matterName;
    }

    public void setMatterName(String matterName)
    {
        this.matterName = matterName;
    }

    public String getApplyNo()
    {
        return applyNo;
    }

    public void setApplyNo(String applyNo)
    {
        this.applyNo = applyNo;
    }
}
