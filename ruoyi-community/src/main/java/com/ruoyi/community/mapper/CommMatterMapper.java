package com.ruoyi.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.community.domain.CommMatter;

/**
 * 表 {@code comm_matter}：社区可办事项的持久化。<br/>
 * 对应 Mapper XML：<code>mapper/community/CommMatterMapper.xml</code>。
 */
public interface CommMatterMapper {
    /**
     * 按主键查询单条事项。
     *
     * @param matterId 事项主键
     */
    CommMatter selectCommMatterById(Long matterId);

    /**
     * 管理端条件列表：可按事项名称模糊、分类、状态，以及<br/>
     * {@code params.beginTime} / {@code params.endTime} 限定创建日期（若依分页参数）。
     */
    List<CommMatter> selectCommMatterList(CommMatter query);

    /**
     * 插入一条事项。
     */
    int insertCommMatter(CommMatter matter);

    /**
     * 按主键更新（XML 中动态 SET）。
     */
    int updateCommMatter(CommMatter matter);

    /**
     * 批量物理删除：<code>matter_id IN (...)</code>。
     *
     * @param matterIds 事项主键数组
     */
    int deleteCommMatterByIds(@Param("matterIds") Long[] matterIds);

    /**
     * 群众端热门事项：仅状态为启用（{@code status='0'}）的事项，按关联申办数降序取前几条。<br/>
     * 排序逻辑见 XML 中对 {@code comm_apply} 的子查询计数。
     *
     * @param limit 最大返回条数
     */
    List<CommMatter> selectHotMatters(@Param("limit") int limit);
}
