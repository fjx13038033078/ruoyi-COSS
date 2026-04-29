package com.ruoyi.community.service;

import java.util.List;

import com.ruoyi.community.domain.CommEvaluation;

/**
 * 办件评价
 */
public interface ICommEvaluationService {
    List<CommEvaluation> selectEvaluationList(CommEvaluation query);

    /**
     * 居民提交评价（一案一评）
     */
    int submitEvaluation(CommEvaluation ev, Long userId);
}
