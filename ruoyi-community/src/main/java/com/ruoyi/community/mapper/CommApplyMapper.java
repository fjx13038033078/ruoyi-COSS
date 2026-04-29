package com.ruoyi.community.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.community.domain.CommApply;

/**
 * 表 {@code comm_apply}：居民申办记录；列表联查 {@code comm_matter} 带出事项名称。<br/>
 * 对应 Mapper XML：<code>mapper/community/CommApplyMapper.xml</code>。
 */
public interface CommApplyMapper
{
    /**
     * 单条申办详情（含事项名称）。附件需在 Service 层另调附件 Mapper。
     */
    CommApply selectCommApplyById(@Param("applyId") Long applyId);

    /**
     * 管理端工作台：支持办件编号模糊、事项名、申请人、电话、状态、创建区间等。
     */
    List<CommApply> selectCommApplyList(CommApply query);

    /**
     * 居民门户「我的办件」：按申办人账号 ID 筛选，最新在前。
     *
     * @param applicantId 申办人用户 ID（sys_user.user_id）
     */
    List<CommApply> selectMyApplyList(@Param("applicantId") Long applicantId);

    /**
     * 统计某日流水号前缀下已有条数，供 Service 生成新的 {@code apply_no}。
     *
     * @param prefix 如 CO + yyyyMMdd
     */
    int countApplyNoLike(@Param("prefix") String prefix);

    /** 插入申办记录，{@code applyId} 回写实体。 */
    int insertCommApply(CommApply apply);

    /** 更新申办全流程字段（驳回、受理、办结等）。 */
    int updateCommApply(CommApply apply);

    /**
     * 数据看板：按事项分组统计申办件数，用于「事项热度」柱状图。<br/>
     * 返回 {@code label}＝事项名、{@code value}＝件数，已排序并 {@code LIMIT}。
     *
     * @param limit 取前多少项
     */
    List<Map<String, Object>> countApplyGroupByMatter(@Param("limit") int limit);
}
