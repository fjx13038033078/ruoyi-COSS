package com.ruoyi.community.mapper;

import java.util.List;

import com.ruoyi.community.domain.CommApplyAttachment;

public interface CommApplyAttachmentMapper
{
    List<CommApplyAttachment> selectByApplyId(Long applyId);

    int insertAttachment(CommApplyAttachment row);

    int deleteByApplyId(Long applyId);
}
