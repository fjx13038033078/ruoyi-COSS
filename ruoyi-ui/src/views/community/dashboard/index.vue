<template>
  <!-- 数据看板：KPI 来自 raw.kpi；柱图 matterHot；折线 dailyTrend。接口见 api/community/dashboard -->
  <div class="app-container community-dashboard">
    <el-row :gutter="16" class="panel-row">
      <el-col v-for="card in kpiCards" :key="card.key" :xs="24" :sm="12" :md="8" class="mb16">
        <el-card shadow="hover" :body-style="{ padding: '14px' }">
          <div class="kpi-title">{{ card.title }}</div>
          <div class="kpi-val">{{ formatKpi(card.key) }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" header="各事项申办量排名"><div ref="chartHot" class="chart-box"/></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" header="每日新增申办量（近期）"><div ref="chartTrend" class="chart-box"/></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
/**
 * 社区一网通办 - 数据决策看板
 *
 * - 数据源：dashboardStatistics(params)，返回 data.kpi、data.matterHot、data.dailyTrend
 * - matterHot: [{ label: 事项名, value: 件数 }] 已在服务端排序并截取条数
 * - dailyTrend: [{ dt: 'yyyy-MM-dd', cnt: 件数 }] 按日历日分组
 *
 * ECharts：mounted + nextTick 后 init；beforeDestroy dispose；监听 window resize 同步尺寸
 */
import * as echarts from 'echarts'
import { dashboardStatistics } from '@/api/community/dashboard'

export default {
  name: 'CommunityDashboard',
  data() {
    return {
      raw: {},
      charts: []
    }
  },
  computed: {
    /** KPI：card.key 与后端 kpi Map 字段名保持一致 */
    kpiCards() {
      return [
        { key: 'totalApply', title: '累计申办量' },
        { key: 'todayNew', title: '当日新增' },
        { key: 'pending', title: '待办/办理中' },
        { key: 'avgScore', title: '平均满意度评分（1-5）' },
        { key: 'satisfactionPercent', title: '满意度折算（%）' },
        { key: 'avgHandleMinutes', title: '平均办理耗时（分钟）' }
      ]
    },
    kpi() {
      return (this.raw && this.raw.kpi) || {}
    }
  },
  mounted() {
    this.load().then(() => {
      this.$nextTick(() => {
        this.initCharts()
        window.addEventListener('resize', this.resizeCharts)
      })
    })
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    this.disposeCharts()
  },
  methods: {
    /** 拉取看板统计数据 */
    load() {
      return dashboardStatistics({ matterHotLimit: 10, trendDays: 14 }).then(res => {
        this.raw = res.data || {}
      })
    },
    /** 单列 KPI 格式化；办结耗时顺带展示服务端折算小时 */
    formatKpi(key) {
      const v = this.kpi[key]
      if (v === null || v === undefined) return '-'
      if (key === 'avgHandleMinutes' && this.kpi.avgHandleHours != null) {
        return `${v}（约 ${this.kpi.avgHandleHours} 小时）`
      }
      return v
    },
    /** 柱状图 + 折线图 option；类目轴文案较长时柱状图倾斜显示 */
    initCharts() {
      this.disposeCharts()
      if (!this.$refs.chartHot || !this.$refs.chartTrend) return
      const hot = echarts.init(this.$refs.chartHot)
      const trend = echarts.init(this.$refs.chartTrend)
      this.charts = [hot, trend]
      const mh = this.raw.matterHot || []
      hot.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 44, right: 16, bottom: 64, top: 24 },
        xAxis: { type: 'category', data: mh.map(i => i.label), axisLabel: { rotate: 28 } },
        yAxis: { type: 'value', name: '件数' },
        series: [{ type: 'bar', data: mh.map(i => i.value), itemStyle: { color: '#409EFF' } }]
      })
      const tr = this.raw.dailyTrend || []
      trend.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 44, right: 16, bottom: 24, top: 24 },
        xAxis: { type: 'category', data: tr.map(i => i.dt), name: '日期' },
        yAxis: { type: 'value', minInterval: 1, name: '件数' },
        series: [{ type: 'line', smooth: true, data: tr.map(i => i.cnt), areaStyle: { opacity: 0.12 }, itemStyle: { color: '#67C23A' } }]
      })
    },
    resizeCharts() {
      this.charts.forEach(c => c && c.resize())
    },
    disposeCharts() {
      this.charts.forEach(c => c && c.dispose())
      this.charts = []
    }
  }
}
</script>

<style scoped>
.panel-row { margin-bottom: 8px; }
.mb16 { margin-bottom: 16px; }
.kpi-title { color: #909399; font-size: 13px; }
.kpi-val { font-size: 22px; font-weight: 600; margin-top: 6px; color: #303133; word-break: break-all; }
.chart-box { height: 360px; width: 100%; min-height: 280px; }
</style>
