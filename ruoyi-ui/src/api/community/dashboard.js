/**
 * 社区一网通办 — 数据看板统计（KPI、趋势、事项热度等）
 */
import request from '@/utils/request'

/**
 * 获取看板聚合数据（办件量、当日创建、在办/办结、平均耗时、按事项分组、按日序列等）
 * @param {Object} [params] — 可选时间范围等，与后端 Controller 约定一致
 */
export function dashboardStatistics(params) {
  return request({
    url: '/community/dashboard/statistics',
    method: 'get',
    params
  })
}
