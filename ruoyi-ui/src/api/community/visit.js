/**
 * 社区一网通办 — 上门代办预约（comm_visit_appointment）接口
 */
import request from '@/utils/request'

/**
 * 分页查询预约列表（管理端工作台）
 * @param {Object} query — 筛选与分页参数
 */
export function listVisit(query) {
  return request({ url: '/community/visit/list', method: 'get', params: query })
}

/**
 * 预约详情（管理端）
 * @param {number|string} visitId
 */
export function getVisit(visitId) {
  return request({ url: '/community/visit/' + visitId, method: 'get' })
}

/**
 * 工作人员接单（待接单 → 已接单/办理中等，以后端状态为准）
 * @param {Object} data — 含 visitId 等
 */
export function acceptVisit(data) {
  return request({ url: '/community/visit/accept', method: 'post', data })
}

/**
 * 办结上门预约（填写处理摘要等）
 * @param {Object} data — 含 visitId、summary 等
 */
export function completeVisit(data) {
  return request({ url: '/community/visit/complete', method: 'post', data })
}

/**
 * 居民门户 — 提交上门预约申请
 * @param {Object} data — 预约表单：地址、期望时间、事项描述等
 */
export function portalSubmitVisit(data) {
  return request({ url: '/portal/visit', method: 'post', data })
}

/**
 * 居民门户 — 当前用户的预约记录列表
 */
export function portalMyVisitList() {
  return request({ url: '/portal/visit/my/list', method: 'get' })
}
