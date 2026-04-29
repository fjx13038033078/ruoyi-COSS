import request from '@/utils/request'

export function listMatter(query) {
  return request({ url: '/community/matter/list', method: 'get', params: query })
}

export function getMatter(matterId) {
  return request({ url: '/community/matter/' + matterId, method: 'get' })
}

export function addMatter(data) {
  return request({ url: '/community/matter', method: 'post', data })
}

export function updateMatter(data) {
  return request({ url: '/community/matter', method: 'put', data })
}

export function delMatter(matterIds) {
  return request({ url: '/community/matter/' + matterIds, method: 'delete' })
}

/** resident portal */
export function portalMatterList(query) {
  return request({ url: '/portal/matter/list', method: 'get', params: query })
}

export function portalMatterDetail(matterId) {
  return request({ url: '/portal/matter/' + matterId, method: 'get' })
}

export function portalHotMatters(limit) {
  return request({ url: '/portal/matter/hot', method: 'get', params: { limit } })
}
