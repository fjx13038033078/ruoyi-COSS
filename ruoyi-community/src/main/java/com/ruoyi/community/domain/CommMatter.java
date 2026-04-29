package com.ruoyi.community.domain;

import javax.validation.constraints.NotBlank;

import com.ruoyi.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 社区办事事项，对应数据库表 {@code comm_matter}。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommMatter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事项主键 ID */
    private Long matterId;

    /** 事项名称 */
    @NotBlank(message = "事项名称不能为空")
    private String matterName;

    /** 事项分类，字典类型：comm_matter_category */
    private String category;

    /** 缓急程度，字典类型：comm_matter_priority（如 normal 等） */
    private String priority;

    /** 责任单位部门 ID（sys_dept） */
    private Long deptId;

    /** 办理所需材料与说明文本 */
    private String requiredDocs;

    /** 承诺办结工作日数（SLA） */
    private Integer expectDays;

    /** 办理流程描述（可为富文本 HTML） */
    private String processDesc;

    /** 状态：0 正常；1 停用 */
    private String status;
}
