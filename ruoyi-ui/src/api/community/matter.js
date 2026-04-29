/**
 * 社区一网通办 — 政务事项（comm_matter）接口
 * 含管理端事项 CRUD 与居民门户列表/详情/热点等。
 */
import request from '@/utils/request'

/**
 * 分页查询事项列表（管理端）
 * @param {Object} query — 如 matterName、category、status、分页 pageNum/pageSize
 */
export function listMatter(query) {
  return request({ url: '/community/matter/list', method: 'get', params: query })
}

/**
 * 事项详情（管理端）
 * @param {number|string} matterId
 */
export function getMatter(matterId) {
  return request({ url: '/community/matter/' + matterId, method: 'get' })
}

/**
 * 新增事项（管理端）
 * @param {Object} data — 事项表单实体
 */
export function addMatter(data) {
  return request({ url: '/community/matter', method: 'post', data })
}

/**
 * 修改事项（管理端）
 */
export function updateMatter(data) {
  return request({ url: '/community/matter', method: 'put', data })
}

/**
 * 批量删除事项（管理端）；路径通常为逗号分隔的多个 ID
 * @param {string} matterIds — 例如 "1,2,3"
 */
export function delMatter(matterIds) {
  return request({ url: '/community/matter/' + matterIds, method: 'delete' })
}

/**
 * 居民门户 — 可办事项分页列表（仅上架状态等由后端筛选）
 * @param {Object} query — pageNum、pageSize 等
 */
export function portalMatterList(query) {
  return request({ url: '/portal/matter/list', method: 'get', params: query })
}

/**
 * 居民门户 — 事项详情（办事指南、材料说明等）
 * @param {number|string} matterId
 */
export function portalMatterDetail(matterId) {
  return request({ url: '/portal/matter/' + matterId, method: 'get' })
}

/**
 * 居民门户 — 热点事项 Top N（用于首页等）
 * @param {number} limit — 条数上限
 */
export function portalHotMatters(limit) {
  return request({ url: '/portal/matter/hot', method: 'get', params: { limit } })
}
