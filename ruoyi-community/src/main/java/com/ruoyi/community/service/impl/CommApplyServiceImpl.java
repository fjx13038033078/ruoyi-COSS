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

/**
 * 办件全流程：门户申报、驳回补正重提、工作台审核签收与办结。
 */
@Service
public class CommApplyServiceImpl implements ICommApplyService
{
    @Autowired
    private CommApplyMapper applyMapper;

    @Autowired
    private CommApplyAttachmentMapper attachmentMapper;

    @Autowired
    private CommMatterMapper matterMapper;

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public List<CommApply> selectCommApplyList(CommApply query)
    {
        return applyMapper.selectCommApplyList(query);
    }

    /** {@inheritDoc} */
    @Override
    public List<CommApply> selectMyApplyList(Long applicantId)
    {
        return applyMapper.selectMyApplyList(applicantId);
    }

    /** 按日序号生成办件单号前缀 COyyyyMMdd。 */
    private String nextApplyNo()
    {
        String day = DateUtils.dateTimeNow("yyyyMMdd");
        String prefix = "CO" + day;
        int n = applyMapper.countApplyNoLike(prefix + "%");
        return prefix + String.format("%04d", n + 1);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPortalSubmit(CommApply apply, List<CommApplyAttachment> attachments)
    {
        CommMatter m = matterMapper.selectCommMatterById(apply.getMatterId());
        if (m == null || !"0".equals(m.getStatus()))
        {
            throw new ServiceException("所选事项不存在或已停用");
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

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateResubmit(Long applyId, Long userId, CommApply payload, List<CommApplyAttachment> attachments)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("办件不存在");
        }
        if (!userId.equals(old.getApplicantId()))
        {
            throw new ServiceException("无权操作该办件");
        }
        if (!"1".equals(old.getStatus()))
        {
            throw new ServiceException("仅驳回后的办件可重新提交");
        }
        old.setApplicantName(payload.getApplicantName());
        old.setIdCard(payload.getIdCard());
        old.setPhone(payload.getPhone());
        old.setApplyRemark(payload.getApplyRemark());
        old.setMatterId(payload.getMatterId() != null ? payload.getMatterId() : old.getMatterId());
        CommMatter m = matterMapper.selectCommMatterById(old.getMatterId());
        if (m == null || !"0".equals(m.getStatus()))
        {
            throw new ServiceException("所选事项不存在或已停用");
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

    /** {@inheritDoc} */
    @Override
    public int rejectApply(Long applyId, String reason, Long handlerId, String handlerName)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("办件不存在");
        }
        if (!"0".equals(old.getStatus()) && !"2".equals(old.getStatus()))
        {
            throw new ServiceException("当前状态不允许驳回");
        }
        Date now = DateUtils.getNowDate();
        old.setStatus("1");
        old.setRejectReason(reason);
        old.setHandlerId(handlerId);
        old.setHandlerName(handlerName);
        old.setHandleTime(now);
        return applyMapper.updateCommApply(old);
    }

    /** {@inheritDoc} */
    @Override
    public int acceptApply(Long applyId, Long handlerId, String handlerName)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("办件不存在");
        }
        if (!"0".equals(old.getStatus()))
        {
            throw new ServiceException("仅待签收状态的办件可受理");
        }
        Date now = DateUtils.getNowDate();
        old.setStatus("2");
        old.setHandlerId(handlerId);
        old.setHandlerName(handlerName);
        old.setAssignTime(now);
        old.setHandleTime(now);
        return applyMapper.updateCommApply(old);
    }

    /** {@inheritDoc} */
    @Override
    public int finishApply(Long applyId, String opinion, String resultFileUrl, Long handlerId, String handlerName)
    {
        CommApply old = applyMapper.selectCommApplyById(applyId);
        if (old == null)
        {
            throw new ServiceException("办件不存在");
        }
        if (!"2".equals(old.getStatus()))
        {
            throw new ServiceException("仅办理中的办件可办结");
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
