package com.ruoyi.community.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.community.domain.CommApply;

public interface CommApplyMapper
{
    CommApply selectCommApplyById(@Param("applyId") Long applyId);

    List<CommApply> selectCommApplyList(CommApply query);

    /** resident my applications */
    List<CommApply> selectMyApplyList(@Param("applicantId") Long applicantId);

    int countApplyNoLike(@Param("prefix") String prefix);

    int insertCommApply(CommApply apply);

    int updateCommApply(CommApply apply);

    /** total apply count grouped by matter (for dashboard) */
    List<Map<String, Object>> countApplyGroupByMatter(@Param("limit") int limit);
}
