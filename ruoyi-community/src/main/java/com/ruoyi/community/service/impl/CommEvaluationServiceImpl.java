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
 * Evaluation submit; only applicant on finished applications ({@code status=3}) may evaluate once.
 */
@Service
public class CommEvaluationServiceImpl implements ICommEvaluationService
{
    @Autowired
    private CommEvaluationMapper evaluationMapper;

    @Autowired
    private CommApplyMapper applyMapper;

    @Override
    public List<CommEvaluation> selectEvaluationList(CommEvaluation query)
    {
        return evaluationMapper.selectEvaluationList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitEvaluation(CommEvaluation ev, Long userId)
    {
        CommEvaluation exist = evaluationMapper.selectByApplyId(ev.getApplyId());
        if (exist != null)
        {
            throw new ServiceException("This application was already evaluated");
        }
        CommApply apply = applyMapper.selectCommApplyById(ev.getApplyId());
        if (apply == null)
        {
            throw new ServiceException("Application not found");
        }
        if (!userId.equals(apply.getApplicantId()))
        {
            throw new ServiceException("Not allowed to evaluate this application");
        }
        if (!"3".equals(apply.getStatus()))
        {
            throw new ServiceException("Only finished applications may be evaluated");
        }
        ev.setApplicantId(userId);
        return evaluationMapper.insertEvaluation(ev);
    }
}
