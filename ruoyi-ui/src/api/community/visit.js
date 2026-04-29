import request from '@/utils/request'

export function listVisit(query) {
  return request({ url: '/community/visit/list', method: 'get', params: query })
}

export function getVisit(visitId) {
  return request({ url: '/community/visit/' + visitId, method: 'get' })
}

export function acceptVisit(data) {
  return request({ url: '/community/visit/accept', method: 'post', data })
}

export function completeVisit(data) {
  return request({ url: '/community/visit/complete', method: 'post', data })
}

export function portalSubmitVisit(data) {
  return request({ url: '/portal/visit', method: 'post', data })
}

export function portalMyVisitList() {
  return request({ url: '/portal/visit/my/list', method: 'get' })
}
