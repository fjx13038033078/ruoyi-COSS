package com.ruoyi.community.domain;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 办件满意度评价，对应表 {@code comm_evaluation}。一案仅能评价一次。
 */
@Data
public class CommEvaluation
{
    /** 评价主键 */
    private Long evaluationId;

    /** 被评价的办件 ID */
    @NotNull(message = "办件不能为空")
    private Long applyId;

    /** 评价人用户 ID */
    private Long applicantId;

    /** 星级评分（1～5） */
    @NotNull(message = "请打分")
    @Min(1)
    @Max(5)
    private Integer score;

    /** 综合评价等级，字典：comm_evaluation_level */
    @NotBlank(message = "请选择评价档次")
    private String evaluationLevel;

    /** 评价文字内容（可选） */
    private String content;

    /** 评价时间（列表冗余展示时可由 SQL 回填） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 列表展示用事名称（非必存字段，查询联表冗余） */
    private String matterName;

    /** 列表展示办件编号（冗余） */
    private String applyNo;
}
