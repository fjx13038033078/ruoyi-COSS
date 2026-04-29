package com.ruoyi.community.mapper;

import java.util.List;

import com.ruoyi.community.domain.CommApplyAttachment;

/**
 * 表 {@code comm_apply_attachment}：申办材料附件。<br/>
 * 对应 Mapper XML：<code>mapper/community/CommApplyAttachmentMapper.xml</code>。
 */
public interface CommApplyAttachmentMapper {
    /**
     * 按办件主键列出全部附件。
     */
    List<CommApplyAttachment> selectByApplyId(Long applyId);

    /**
     * 插入一条附件记录。
     */
    int insertAttachment(CommApplyAttachment row);

    /**
     * 补正重提前删除该办件下旧附件，再批量插入新附件。<br/>
     * 须与业务状态（仅驳回可重提）配合使用。
     */
    int deleteByApplyId(Long applyId);
}
