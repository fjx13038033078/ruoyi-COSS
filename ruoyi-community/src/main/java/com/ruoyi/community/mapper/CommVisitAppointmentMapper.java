package com.ruoyi.community.mapper;

import java.util.List;

import com.ruoyi.community.domain.CommVisitAppointment;

public interface CommVisitAppointmentMapper
{
    CommVisitAppointment selectByVisitId(Long visitId);

    List<CommVisitAppointment> selectVisitList(CommVisitAppointment query);

    List<CommVisitAppointment> selectMyVisits(Long applicantId);

    int insertVisit(CommVisitAppointment row);

    int updateVisit(CommVisitAppointment row);
}
