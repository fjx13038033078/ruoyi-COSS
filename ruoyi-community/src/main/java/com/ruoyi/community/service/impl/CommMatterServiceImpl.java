package com.ruoyi.community.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.community.domain.CommMatter;
import com.ruoyi.community.mapper.CommMatterMapper;
import com.ruoyi.community.service.ICommMatterService;

@Service
public class CommMatterServiceImpl implements ICommMatterService
{
    @Autowired
    private CommMatterMapper matterMapper;

    @Override
    public CommMatter selectCommMatterById(Long matterId)
    {
        return matterMapper.selectCommMatterById(matterId);
    }

    @Override
    public List<CommMatter> selectCommMatterList(CommMatter matter)
    {
        return matterMapper.selectCommMatterList(matter);
    }

    @Override
    public int insertCommMatter(CommMatter matter)
    {
        if (matter.getPriority() == null)
        {
            matter.setPriority("normal");
        }
        if (matter.getStatus() == null)
        {
            matter.setStatus("0");
        }
        return matterMapper.insertCommMatter(matter);
    }

    @Override
    public int updateCommMatter(CommMatter matter)
    {
        return matterMapper.updateCommMatter(matter);
    }

    @Override
    public int deleteCommMatterByIds(Long[] matterIds)
    {
        return matterMapper.deleteCommMatterByIds(matterIds);
    }

    @Override
    public List<CommMatter> selectHotMatters(int limit)
    {
        return matterMapper.selectHotMatters(limit);
    }
}
