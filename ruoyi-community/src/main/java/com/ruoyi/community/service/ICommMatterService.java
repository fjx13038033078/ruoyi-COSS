package com.ruoyi.community.service;

import java.util.List;

import com.ruoyi.community.domain.CommMatter;

/**
 * 社区办事事项的增删改查与门户热门排序。
 */
public interface ICommMatterService
{
    /** @param matterId 主键 ID */
    CommMatter selectCommMatterById(Long matterId);

    /** @param matter 分页与条件查询 */
    List<CommMatter> selectCommMatterList(CommMatter matter);

    /** 新增事项（内部可补默认值：优先级、启用状态）。 */
    int insertCommMatter(CommMatter matter);

    /** 修改事项 */
    int updateCommMatter(CommMatter matter);

    /** 批量删除事项 */
    int deleteCommMatterByIds(Long[] matterIds);

    /** 门户首页：按办件热度取前几条事项 */
    List<CommMatter> selectHotMatters(int limit);
}
