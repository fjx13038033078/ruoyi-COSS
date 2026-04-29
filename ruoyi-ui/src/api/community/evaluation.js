/**
 * 社区一网通办 — 办件满意度评价（comm_evaluation）接口
 */
import request from '@/utils/request'

/**
 * 分页查询评价列表（管理端统计/查看）
 * @param {Object} query — 筛选与分页参数
 */
export function listEvaluation(query) {
  return request({ url: '/community/evaluation/list', method: 'get', params: query })
}

/**
 * 居民门户 — 对已办结办件提交评价（每办件通常仅一条，重复以后端为准）
 * @param {Object} data — 如 applyId、score、evaluationLevel、content
 */
export function portalSubmitEvaluation(data) {
  return request({ url: '/portal/evaluation', method: 'post', data })
}
