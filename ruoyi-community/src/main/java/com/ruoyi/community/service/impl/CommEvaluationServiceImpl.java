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
 * 评价业务实现（仅已办结可申请）
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
            throw new ServiceException("该办件已评价");
        }
        CommApply apply = applyMapper.selectCommApplyById(ev.getApplyId());
        if (apply == null)
        {
            throw new ServiceException("办件不存在");
        }
        if (!userId.equals(apply.getApplicantId()))
        {
            throw new ServiceException("无权限评价");
        }
        if (!"3".equals(apply.getStatus()))
        {
            throw new ServiceException("仅已办结可申请评价");
        }
        ev.setApplicantId(userId);
        return evaluationMapper.insertEvaluation(ev);
    }
}
