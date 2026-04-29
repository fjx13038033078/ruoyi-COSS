import request from '@/utils/request'

export function listApply(query) {
  return request({ url: '/community/apply/list', method: 'get', params: query })
}

export function getApply(applyId) {
  return request({ url: '/community/apply/' + applyId, method: 'get' })
}

export function rejectApply(data) {
  return request({ url: '/community/apply/reject', method: 'post', data })
}

export function acceptApply(data) {
  return request({ url: '/community/apply/accept', method: 'post', data })
}

export function finishApply(data) {
  return request({ url: '/community/apply/finish', method: 'post', data })
}

export function portalSubmitApply(data) {
  return request({ url: '/portal/apply', method: 'post', data })
}

export function portalResubmitApply(applyId, data) {
  return request({ url: '/portal/apply/resubmit/' + applyId, method: 'put', data })
}

export function portalMyApplyList() {
  return request({ url: '/portal/apply/my/list', method: 'get' })
}

export function portalMyApplyDetail(applyId) {
  return request({ url: '/portal/apply/my/' + applyId, method: 'get' })
}
