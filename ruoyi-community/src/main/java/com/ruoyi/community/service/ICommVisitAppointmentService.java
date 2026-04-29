package com.ruoyi.community.service;

import java.util.List;

import com.ruoyi.community.domain.CommVisitAppointment;

/**
 * 上门代办预约
 */
public interface ICommVisitAppointmentService
{
    CommVisitAppointment selectByVisitId(Long visitId);

    List<CommVisitAppointment> selectVisitList(CommVisitAppointment query);

    List<CommVisitAppointment> selectMyVisits(Long applicantId);

    int insertVisit(CommVisitAppointment row);

    /** 工作人员接单 */
    int acceptVisit(Long visitId, Long handlerId, String handlerName);

    /** 线下完成，填写摘要 */
    int completeVisit(Long visitId, String summary, Long handlerId, String handlerName);
}
