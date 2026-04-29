/**
 * 社区一网通办 — 办件（comm_apply）接口
 * 含管理端审核工作台与居民门户在线申办、我的办件等。
 */
import request from '@/utils/request'

/**
 * 分页查询办件列表（管理端工作台，需 community:apply:list）
 * @param {Object} query — 查询参数，如 applyNo、matterName、applicantName、phone、status、params(begin/end)
 * @returns {Promise} Axios 响应体（若依通常为 { rows, total }）
 */
export function listApply(query) {
  return request({ url: '/community/apply/list', method: 'get', params: query })
}

/**
 * 查看单条办件详情（含事项名称等联表字段，附件由后端按需填充；需 community:apply:query）
 * @param {number|string} applyId — 办件主键
 */
export function getApply(applyId) {
  return request({ url: '/community/apply/' + applyId, method: 'get' })
}

/**
 * 驳回办件（管理端）
 * @param {Object} data — { applyId, rejectReason }
 */
export function rejectApply(data) {
  return request({ url: '/community/apply/reject', method: 'post', data })
}

/**
 * 受理/签收：待初审变为办理中（管理端）
 * @param {Object} data — { applyId }
 */
export function acceptApply(data) {
  return request({ url: '/community/apply/accept', method: 'post', data })
}

/**
 * 办结办件（管理端）
 * @param {Object} data — { applyId, opinion, resultFileUrl }
 */
export function finishApply(data) {
  return request({ url: '/community/apply/finish', method: 'post', data })
}

/**
 * 居民门户 — 提交新申办（role: resident）
 * @param {Object} data — 办件主体 + attachmentList（[{ fileUrl, fileName }]，可选）
 */
export function portalSubmitApply(data) {
  return request({ url: '/portal/apply', method: 'post', data })
}

/**
 * 居民门户 — 驳回后补正重提（会替换附件列表等业务规则以后端为准）
 * @param {number|string} applyId — 原办件 ID
 * @param {Object} data — 同上，含 applicantName、phone、idCard、matterId、applyRemark、attachmentList 等
 */
export function portalResubmitApply(applyId, data) {
  return request({ url: '/portal/apply/resubmit/' + applyId, method: 'put', data })
}

/**
 * 居民门户 — 当前登录用户的「我的办件」列表（无分页或由后端一次性返回数组，见控制台实现）
 */
export function portalMyApplyList() {
  return request({ url: '/portal/apply/my/list', method: 'get' })
}

/**
 * 居民门户 — 我的办件详情（校验本人；含附件列表）
 * @param {number|string} applyId
 */
export function portalMyApplyDetail(applyId) {
  return request({ url: '/portal/apply/my/' + applyId, method: 'get' })
}
