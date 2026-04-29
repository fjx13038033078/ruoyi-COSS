import request from '@/utils/request'

export function dashboardStatistics(params) {
  return request({
    url: '/community/dashboard/statistics',
    method: 'get',
    params
  })
}
