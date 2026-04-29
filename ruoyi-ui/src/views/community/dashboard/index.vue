<template>
  <div class="app-container">
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
        <el-card shadow="never" header="Applications by matter (top)"><div ref="chartHot" class="chart-box"/></el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" header="Daily new applications (last N days)"><div ref="chartTrend" class="chart-box"/></el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
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
    kpiCards() {
      return [
        { key: 'totalApply', title: 'Total applications' },
        { key: 'todayNew', title: 'New today' },
        { key: 'pending', title: 'Pending / in progress' },
        { key: 'avgScore', title: 'Avg satisfaction score (1-5)' },
        { key: 'satisfactionPercent', title: 'Satisfaction estimate (%)' },
        { key: 'avgHandleMinutes', title: 'Avg handle time (min)' }
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
    load() {
      return dashboardStatistics({ matterHotLimit: 10, trendDays: 14 }).then(res => {
        this.raw = res.data || {}
      })
    },
    formatKpi(key) {
      const v = this.kpi[key]
      if (v === null || v === undefined) return '-'
      if (key === 'avgHandleMinutes' && this.kpi.avgHandleHours != null) {
        return `${v} (${this.kpi.avgHandleHours} h)`
      }
      return v
    },
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
        yAxis: { type: 'value' },
        series: [{ type: 'bar', data: mh.map(i => i.value), itemStyle: { color: '#409EFF' } }]
      })
      const tr = this.raw.dailyTrend || []
      trend.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 44, right: 16, bottom: 24, top: 24 },
        xAxis: { type: 'category', data: tr.map(i => i.dt) },
        yAxis: { type: 'value', minInterval: 1 },
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
