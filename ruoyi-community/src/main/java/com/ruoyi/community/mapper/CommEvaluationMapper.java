package com.ruoyi.community.mapper;

import java.util.List;

import com.ruoyi.community.domain.CommEvaluation;

public interface CommEvaluationMapper
{
    CommEvaluation selectByApplyId(Long applyId);

    List<CommEvaluation> selectEvaluationList(CommEvaluation query);

    int insertEvaluation(CommEvaluation row);

    /** avg score 0-5 */
    Double selectAvgScore();
}
