/**
 * 居民门户 — 可选聚合导出（从各子模块 re-export，便于一次 import 门户相关接口）。
 * matter：热点 / 列表 / 详情；
 * apply：提交申办 / 补正 / 我的办件；
 * visit：上门预约；
 * evaluation：满意度评价；
 * dashboard：看板指标（首页若展示统计时可复用）。
 */

export { portalHotMatters, portalMatterList, portalMatterDetail } from './matter'
export {
  portalSubmitApply,
  portalResubmitApply,
  portalMyApplyList,
  portalMyApplyDetail
} from './apply'
export { portalSubmitVisit, portalMyVisitList } from './visit'
export { portalSubmitEvaluation } from './evaluation'
export { dashboardStatistics } from './dashboard'
