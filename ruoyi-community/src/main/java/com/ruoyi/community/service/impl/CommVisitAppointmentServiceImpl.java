package com.ruoyi.community.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.community.domain.CommVisitAppointment;
import com.ruoyi.community.mapper.CommVisitAppointmentMapper;
import com.ruoyi.community.service.ICommVisitAppointmentService;

/**
 * 上门代办：居民发起预约、社工接单及线下办结记录摘要。
 */
@Service
public class CommVisitAppointmentServiceImpl implements ICommVisitAppointmentService
{
    @Autowired
    private CommVisitAppointmentMapper visitMapper;

    /** {@inheritDoc} */
    @Override
    public CommVisitAppointment selectByVisitId(Long visitId)
    {
        return visitMapper.selectByVisitId(visitId);
    }

    /** {@inheritDoc} */
    @Override
    public List<CommVisitAppointment> selectVisitList(CommVisitAppointment query)
    {
        return visitMapper.selectVisitList(query);
    }

    /** {@inheritDoc} */
    @Override
    public List<CommVisitAppointment> selectMyVisits(Long applicantId)
    {
        return visitMapper.selectMyVisits(applicantId);
    }

    /** {@inheritDoc} */
    @Override
    public int insertVisit(CommVisitAppointment row)
    {
        return visitMapper.insertVisit(row);
    }

    /** {@inheritDoc} */
    @Override
    public int acceptVisit(Long visitId, Long handlerId, String handlerName)
    {
        CommVisitAppointment v = visitMapper.selectByVisitId(visitId);
        if (v == null)
        {
            throw new ServiceException("预约不存在");
        }
        if (!"0".equals(v.getStatus()))
        {
            throw new ServiceException("当前状态不可接单");
        }
        v.setStatus("1");
        v.setHandlerId(handlerId);
        v.setHandlerName(handlerName);
        return visitMapper.updateVisit(v);
    }

    /** {@inheritDoc} */
    @Override
    public int completeVisit(Long visitId, String summary, Long handlerId, String handlerName)
    {
        CommVisitAppointment v = visitMapper.selectByVisitId(visitId);
        if (v == null)
        {
            throw new ServiceException("预约不存在");
        }
        if (!"1".equals(v.getStatus()))
        {
            throw new ServiceException("请先接单后再办结");
        }
        Date now = DateUtils.getNowDate();
        v.setStatus("2");
        v.setSummary(summary);
        v.setHandlerId(handlerId);
        v.setHandlerName(handlerName);
        v.setFinishTime(now);
        return visitMapper.updateVisit(v);
    }
}
