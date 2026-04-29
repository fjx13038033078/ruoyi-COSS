package com.ruoyi.community.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 办件附件，对应表 {@code comm_apply_attachment}。
 */
@Data
public class CommApplyAttachment implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 附件主键 */
    private Long id;

    /** 所属办件 ID */
    private Long applyId;

    /** 文件名 */
    private String fileName;

    /** 文件访问路径（与文件上传组件约定一致） */
    private String fileUrl;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
}
