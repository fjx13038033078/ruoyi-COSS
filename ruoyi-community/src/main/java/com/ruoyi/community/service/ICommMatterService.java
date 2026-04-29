package com.ruoyi.community.service;

import java.util.List;

import com.ruoyi.community.domain.CommMatter;

public interface ICommMatterService
{
    CommMatter selectCommMatterById(Long matterId);

    List<CommMatter> selectCommMatterList(CommMatter matter);

    int insertCommMatter(CommMatter matter);

    int updateCommMatter(CommMatter matter);

    int deleteCommMatterByIds(Long[] matterIds);

    List<CommMatter> selectHotMatters(int limit);
}
