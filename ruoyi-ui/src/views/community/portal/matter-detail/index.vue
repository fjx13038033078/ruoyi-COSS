<template>
  <el-card v-loading="loading" shadow="never">
    <div v-if="detail">
      <h2>{{ detail.matterName }}</h2>
      <p style="margin-top:8px">
        <dict-tag :options="dict.type.comm_matter_category" :value="detail.category"/>
        <dict-tag style="margin-left:8px" :options="dict.type.comm_matter_priority" :value="detail.priority"/>
        <span style="margin-left:12px;color:#909399">{{ detail.expectDays }} working days (SLA)</span>
      </p>
      <el-divider />
      <h4>Required documents</h4>
      <pre class="doc-block">{{ detail.requiredDocs }}</pre>
      <h4>Process</h4>
      <div class="html-block" v-html="detail.processDesc"/>
      <el-button type="primary" style="margin-top:16px" @click="goApply">Apply now</el-button>
    </div>
    <el-empty v-else-if="!loading" description="Unavailable or offline"/>
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
.doc-block { white-space: pre-wrap; font-family: inherit; padding: 12px; background: #f9fafc; border-radius: 4px; margin: 8px 0 16px; }
.html-block { line-height: 1.65; margin-top: 8px; margin-bottom: 16px; }
</style>
