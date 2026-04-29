package com.ruoyi.community.service;

import java.util.List;

import com.ruoyi.community.domain.CommApply;
import com.ruoyi.community.domain.CommApplyAttachment;

public interface ICommApplyService
{
    CommApply selectCommApplyById(Long applyId, boolean withAttachments);

    /** resident: null if not owner */
    CommApply selectCommApplyForResident(Long applyId, Long userId, boolean withAttachments);

    List<CommApply> selectCommApplyList(CommApply query);

    List<CommApply> selectMyApplyList(Long applicantId);

    int insertPortalSubmit(CommApply apply, List<CommApplyAttachment> attachments);

    int updateResubmit(Long applyId, Long userId, CommApply apply, List<CommApplyAttachment> attachments);

    int rejectApply(Long applyId, String reason, Long handlerId, String handlerName);

    int acceptApply(Long applyId, Long handlerId, String handlerName);

    int finishApply(Long applyId, String opinion, String resultFileUrl, Long handlerId, String handlerName);
}
