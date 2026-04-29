import request from '@/utils/request'

export function listEvaluation(query) {
  return request({ url: '/community/evaluation/list', method: 'get', params: query })
}

export function portalSubmitEvaluation(data) {
  return request({ url: '/portal/evaluation', method: 'post', data })
}
