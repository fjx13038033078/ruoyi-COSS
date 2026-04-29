package com.ruoyi.community.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 上门代办预约记录，对应表 {@code comm_visit_appointment}。
 */
@Data
public class CommVisitAppointment {
    /**
     * 预约主键
     */
    private Long visitId;

    /**
     * 预约人用户 ID
     */
    private Long applicantId;

    /**
     * 联系人姓名（后端可从登录账号填充）
     */
    private String applicantName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 上门地址
     */
    @NotBlank(message = "上门地址不能为空")
    private String address;

    /**
     * 事由或需求简述
     */
    private String matterDesc;

    /**
     * 期望上门时间
     */
    @NotNull(message = "期望上门时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedTime;

    /**
     * 状态：0 待接单；1 已接单办理中；2 已完成
     */
    private String status;

    /**
     * 处理人 ID
     */
    private Long handlerId;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 办结摘要说明
     */
    private String summary;

    /**
     * 预约创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 办结时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
}
