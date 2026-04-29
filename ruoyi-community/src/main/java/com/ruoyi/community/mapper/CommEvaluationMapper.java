package com.ruoyi.community.mapper;

import java.util.List;

import com.ruoyi.community.domain.CommEvaluation;

/**
 * 表 {@code comm_evaluation}：办结后满意度评价（一案一条）。<br/>
 * 对应 Mapper XML：<code>mapper/community/CommEvaluationMapper.xml</code>。
 */
public interface CommEvaluationMapper
{
    /**
     * 按办件 ID 查询是否已有评价，用于防重复提交。
     */
    CommEvaluation selectByApplyId(Long applyId);

    /**
     * 管理端评价列表：联表带出办件编号、事项名称；可按办件编号模糊查询。
     */
    List<CommEvaluation> selectEvaluationList(CommEvaluation query);

    /** 插入评价，主键回填。 */
    int insertEvaluation(CommEvaluation row);

    /**
     * 全部评价记录的平均星级（1～5），保留两位小数；无数据时返回 null。<br/>
     * 供数据看板 KPI 与满意度折算使用。
     */
    Double selectAvgScore();
}
