package com.ruoyi.community.service;

import java.util.List;

import com.ruoyi.community.domain.CommApply;
import com.ruoyi.community.domain.CommApplyAttachment;

/**
 * 居民办件的提交、驳回、签收、办结及门户端自助查询。
 */
public interface ICommApplyService
{
    /**
     * 按办件 ID 查询；可选加载附件列表。
     *
     * @param applyId         办件 ID
     * @param withAttachments 是否顺带查询申请材料附件
     */
    CommApply selectCommApplyById(Long applyId, boolean withAttachments);

    /**
     * 门户端校验本人办件详情；若非本人或未找到则返回 null。
     */
    CommApply selectCommApplyForResident(Long applyId, Long userId, boolean withAttachments);

    /** 管理端工作台列表（条件分页） */
    List<CommApply> selectCommApplyList(CommApply query);

    /** 居民：“我的办理”列表 */
    List<CommApply> selectMyApplyList(Long applicantId);

    /**
     * 门户首次提交申报，生成编号、状态设为待初审，并入库附件。
     */
    int insertPortalSubmit(CommApply apply, List<CommApplyAttachment> attachments);

    /**
     * 驳回后补正重新提交。
     */
    int updateResubmit(Long applyId, Long userId, CommApply apply, List<CommApplyAttachment> attachments);

    /**
     * 工作人员驳回申报。
     */
    int rejectApply(Long applyId, String reason, Long handlerId, String handlerName);

    /**
     * 工作人员签收，进入办理中。
     */
    int acceptApply(Long applyId, Long handlerId, String handlerName);

    /**
     * 办结存档，并可填写意见书与结果件路径。
     */
    int finishApply(Long applyId, String opinion, String resultFileUrl, Long handlerId, String handlerName);
}
