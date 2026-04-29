package com.ruoyi.community.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.community.domain.CommApply;
import com.ruoyi.community.domain.CommApplyAttachment;
import com.ruoyi.community.domain.CommMatter;
import com.ruoyi.community.mapper.CommApplyAttachmentMapper;
import com.ruoyi.community.mapper.CommApplyMapper;
import com.ruoyi.community.mapper.CommMatterMapper;
import com.ruoyi.community.service.ICommApplyService;

@Service
public class CommApplyServiceImpl implements ICommApplyService
{
    @Autowired
    private CommApplyMapper applyMapper;

    @Autowired
    private CommApplyAttachmentMapper attachmentMapper;

    @Autowired
    private CommMatterMapper matterMapper;

    @Override
    public CommApply selectCommApplyById(Long applyId, boolean withAttachments)
    {
        CommApply a = applyMapper.selectCommApplyById(applyId);
        if (withAttachments && a != null)
        {
            a.setAttachmentList(attachmentMapper.selectByApplyId(applyId));
        }
        return a;
    }

    @Override
    public CommApply selectCommApplyForResident(Long applyId, Long userId, boolean withAttachments)
    {
        CommApply a = selectCommApplyById(applyId, withAttachments);
        if (a == null)
        {
            return null;
        }
        if (!userId.equals(a.getApplicantId()))
        {
            return null;
        }
        return a;
    }

    @Override
    public List<CommApply> selectCommApplyList(CommApply query)
    {
        return applyMapper.selectCommApplyList(query);
    }

    @Override
    public List<CommApply> selectMyApplyList(Long applicantId)
    {
        return applyMapper.selectMyApplyList(applicantId);
    }

    private String nextApplyNo()
    {
        String day = DateUtils.dateTimeNow("yyyyMMdd");
        String prefix = "CO" + day;
        int n = applyMapper.countApplyNoLike(prefix + "%");
        return prefix + String.format("%04d", n + 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPortalSubmit(CommApply apply, List<CommApplyAttachment> attachments)
    {
        CommMatter m = matterMapper.selectCommMatterById(apply.getMatterId());
        if (m == null || !"0".equals(m.getStatus()))
        {
            throw new ServiceException("Matter not exists or disabled.");
        }
        apply.setApplyNo(nextApplyNo());
        apply.setStatus("0");
        int row = applyMapper.insertCommApply(apply);
        if (attachments != null && !attachments.isEmpty())
        {
            for (CommApplyAttachment att : attachments)
            {
                if (att == null || StringUtils.isEmpty(att.getFileUrl()))
                {
                    continue;
                }
                att.setApplyId(apply.getApplyId());
                attachmentMapper.insertAttachment(att);
            }
        }
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateResubmit(Long applyId, Long userId, CommApply payload, List<CommApplyAttachment> attachments)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("Apply not exists.");
        }
        if (!userId.equals(old.getApplicantId()))
        {
            throw new ServiceException("No permission.");
        }
        if (!"1".equals(old.getStatus()))
        {
            throw new ServiceException("Only rejected apply can resubmit.");
        }
        old.setApplicantName(payload.getApplicantName());
        old.setIdCard(payload.getIdCard());
        old.setPhone(payload.getPhone());
        old.setApplyRemark(payload.getApplyRemark());
        old.setMatterId(payload.getMatterId() != null ? payload.getMatterId() : old.getMatterId());
        CommMatter m = matterMapper.selectCommMatterById(old.getMatterId());
        if (m == null || !"0".equals(m.getStatus()))
        {
            throw new ServiceException("Matter not exists or disabled.");
        }
        old.setStatus("0");
        old.setRejectReason("");
        int u = applyMapper.updateCommApply(old);
        attachmentMapper.deleteByApplyId(applyId);
        if (attachments != null)
        {
            for (CommApplyAttachment att : attachments)
            {
                if (att == null || StringUtils.isEmpty(att.getFileUrl()))
                {
                    continue;
                }
                att.setApplyId(applyId);
                attachmentMapper.insertAttachment(att);
            }
        }
        return u;
    }

    @Override
    public int rejectApply(Long applyId, String reason, Long handlerId, String handlerName)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("Apply not exists.");
        }
        if (!"0".equals(old.getStatus()) && !"2".equals(old.getStatus()))
        {
            throw new ServiceException("Status not allow reject.");
        }
        Date now = DateUtils.getNowDate();
        old.setStatus("1");
        old.setRejectReason(reason);
        old.setHandlerId(handlerId);
        old.setHandlerName(handlerName);
        old.setHandleTime(now);
        return applyMapper.updateCommApply(old);
    }

    @Override
    public int acceptApply(Long applyId, Long handlerId, String handlerName)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("Apply not exists.");
        }
        if (!"0".equals(old.getStatus()))
        {
            throw new ServiceException("Only pending apply can accept.");
        }
        Date now = DateUtils.getNowDate();
        old.setStatus("2");
        old.setHandlerId(handlerId);
        old.setHandlerName(handlerName);
        old.setAssignTime(now);
        old.setHandleTime(now);
        return applyMapper.updateCommApply(old);
    }

    @Override
    public int finishApply(Long applyId, String opinion, String resultFileUrl, Long handlerId, String handlerName)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("Apply not exists.");
        }
        if (!"2".equals(old.getStatus()))
        {
            throw new ServiceException("Only processing apply can finish.");
        }
        Date now = DateUtils.getNowDate();
        old.setStatus("3");
        old.setOpinion(opinion);
        old.setResultFileUrl(resultFileUrl);
        old.setHandlerId(handlerId);
        old.setHandlerName(handlerName);
        old.setHandleTime(now);
        old.setFinishTime(now);
        return applyMapper.updateCommApply(old);
    }
}
