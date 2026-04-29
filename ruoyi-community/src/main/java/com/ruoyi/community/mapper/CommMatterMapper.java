package com.ruoyi.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.community.domain.CommMatter;

public interface CommMatterMapper
{
    CommMatter selectCommMatterById(Long matterId);

    List<CommMatter> selectCommMatterList(CommMatter query);

    int insertCommMatter(CommMatter matter);

    int updateCommMatter(CommMatter matter);

    int deleteCommMatterByIds(@Param("matterIds") Long[] matterIds);

    /** portal hot: order by apply count limit */
    List<CommMatter> selectHotMatters(@Param("limit") int limit);
}
