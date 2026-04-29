package com.ruoyi.community.mapper;

import java.util.List;

import com.ruoyi.community.domain.CommVisitAppointment;

/**
 * 表 {@code comm_visit_appointment}：上门服务预约。<br/>
 * 状态：0 待接单、1 办理中、2 已完成。<br/>
 * 对应 Mapper XML：<code>mapper/community/CommVisitAppointmentMapper.xml</code>。
 */
public interface CommVisitAppointmentMapper
{
    /** 按预约主键查询。 */
    CommVisitAppointment selectByVisitId(Long visitId);

    /**
     * 工作人员端列表：可按状态、申请人姓名模糊筛选。
     */
    List<CommVisitAppointment> selectVisitList(CommVisitAppointment query);

    /**
     * 居民端「我的预约」：按 {@code applicant_id} 查询。
     */
    List<CommVisitAppointment> selectMyVisits(Long applicantId);

    /** 插入预约，默认 {@code status='0'}，主键回填。 */
    int insertVisit(CommVisitAppointment row);

    /** 接单、办结时更新状态、经办人、摘要、完成时间等。 */
    int updateVisit(CommVisitAppointment row);
}
