package com.ruoyi.community.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 居民办件申请，对应表 {@code comm_apply}。创建时间、更新时间等继承自 {@link BaseEntity} 用于后台查询区间。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 办件主键 ID */
    private Long applyId;

    /** 办件编号（系统生成） */
    private String applyNo;

    /** 关联的事项 ID（comm_matter） */
    @NotNull(message = "请选择办事事项")
    private Long matterId;

    /** 事项名称冗余字段 */
    private String matterName;

    /** 申请人用户 ID（sys_user） */
    private Long applicantId;

    /** 申请人姓名 */
    @NotBlank(message = "申请人姓名不能为空")
    private String applicantName;

    /** 身份证号码 */
    private String idCard;

    /** 联系电话 */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /** 申请补充说明 */
    private String applyRemark;

    /** 流程状态：0 待初审；1 已驳回；2 办理中；3 已办结 */
    private String status;

    /** 驳回原因 */

    private String rejectReason;

    /** 审核/办结意见 */
    private String opinion;

    /** 结果文书或附件路径（办结时填写） */
    private String resultFileUrl;

    /** 经办人 ID */
    private Long handlerId;

    /** 经办人姓名 */
    private String handlerName;

    /** 签收/分配时间 */
    private Date assignTime;

    /** 最近一次处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    /** 办结完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /** 申请材料附件列表（非表字段时使用） */
    private List<CommApplyAttachment> attachmentList;

    /**
     * 是否为可重新提交状态（仅驳回后可补正重提）。
     *
     * @return 状态为驳回（1）时返回 true
     */
    @JsonIgnore
    public boolean allowResubmit()
    {
        return "1".equals(status);
    }
}
