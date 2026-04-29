<template>
  <el-card v-loading="loading" shadow="never">
    <div v-if="detail">
      <h2>{{ detail.matterName }}</h2>
      <!-- 勿用 p 包裹 DictTag：组件根节点为 div，放在 p 内会引发 DOM 错乱与样式堆叠 -->
      <div class="matter-meta-row">
        <div class="matter-tags">
          <dict-tag :options="dict.type.comm_matter_category" :value="detail.category"/>
          <dict-tag :options="dict.type.comm_matter_priority" :value="detail.priority"/>
        </div>
        <span class="sla-text">承诺 {{ detail.expectDays }} 个工作日办结</span>
      </div>
      <el-divider />
      <h4>所需材料</h4>
      <pre class="doc-block">{{ detail.requiredDocs }}</pre>
      <h4>办理流程</h4>
      <div class="html-block" v-html="detail.processDesc"/>
      <el-button type="primary" style="margin-top:16px" @click="goApply">立即申报</el-button>
    </div>
    <el-empty v-else-if="!loading" description="事项不存在或未发布"/>
  </el-card>
</template>

<script>
import { portalMatterDetail } from '@/api/community/matter'

export default {
  name: 'PortalMatterDetail',
  dicts: ['comm_matter_category', 'comm_matter_priority'],
  data() {
    return { detail: null, loading: false }
  },
  created() { this.load() },
  watch: {
    '$route'(to) {
      if (to.name === 'PortalMatterDetail') this.load()
    }
  },
  methods: {
    load() {
      const id = this.$route.params.matterId
      this.loading = true
      this.detail = null
      portalMatterDetail(id).then(res => {
        this.detail = res.data
      }).catch(() => {}).finally(() => { this.loading = false })
    },
    goApply() {
      this.$router.push({ path: '/portal/apply', query: { matterId: this.detail.matterId } })
    }
  }
}
</script>

<style scoped>
.matter-meta-row {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px 16px;
}
.matter-tags {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}
/* DictTag 根为 div（块级）；在 flex 子项中并排，避免纵向堆叠错乱 */
.matter-tags > div {
  display: inline-flex;
  align-items: center;
  flex-shrink: 0;
}
.sla-text { color: #909399; font-size: 14px; flex: 0 1 auto; min-width: 0; line-height: 1.6; }
.doc-block { white-space: pre-wrap; font-family: inherit; padding: 12px; background: #f9fafc; border-radius: 4px; margin: 8px 0 16px; }
.html-block { line-height: 1.65; margin-top: 8px; margin-bottom: 16px; }
</style>
