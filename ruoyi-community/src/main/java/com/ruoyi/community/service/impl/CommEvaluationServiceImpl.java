package com.ruoyi.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.community.domain.CommApply;
import com.ruoyi.community.domain.CommEvaluation;
import com.ruoyi.community.mapper.CommApplyMapper;
import com.ruoyi.community.mapper.CommEvaluationMapper;
import com.ruoyi.community.service.ICommEvaluationService;

/**
 * 办件办结后居民评价；每条办件仅能评价一次，且必须为本人办结件。
 */
@Service
public class CommEvaluationServiceImpl implements ICommEvaluationService {
    @Autowired
    private CommEvaluationMapper evaluationMapper;

    @Autowired
    private CommApplyMapper applyMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CommEvaluation> selectEvaluationList(CommEvaluation query) {
        return evaluationMapper.selectEvaluationList(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitEvaluation(CommEvaluation ev, Long userId) {
        CommEvaluation exist = evaluationMapper.selectByApplyId(ev.getApplyId());
        if (exist != null) {
            throw new ServiceException("该办件已评价，不可重复提交");
        }
        CommApply apply = applyMapper.selectCommApplyById(ev.getApplyId());
        if (apply == null) {
            throw new ServiceException("办件不存在");
        }
        if (!userId.equals(apply.getApplicantId())) {
            throw new ServiceException("仅本人可对本人办件进行评价");
        }
        if (!"3".equals(apply.getStatus())) {
            throw new ServiceException("仅办结后的办件可评价");
        }
        ev.setApplicantId(userId);
        return evaluationMapper.insertEvaluation(ev);
    }
}
