<template>
  <div>
    <el-card shadow="never" class="mb16">
      <h2>社区一网通办群众端</h2>
      <p style="margin:8px 0 0;color:#606266">查看办事指南，在线提交材料，随时查询办理进度。</p>
    </el-card>
    <el-row :gutter="16">
      <el-col :span="16">
        <el-card shadow="hover" header="热门办事事项"><el-empty v-if="!hot.length && !loading" description="暂无数据" /><el-row :gutter="12" v-loading="loading">
          <el-col :span="12" v-for="m in hot" :key="m.matterId" class="mb12">
            <el-card shadow="never" body-style="padding:12px;"><div style="font-weight:600">{{ m.matterName }}</div>
              <dict-tag style="margin-top:8px;display:inline-block" :options="dict.type.comm_matter_category" :value="m.category"/>
              <el-link type="primary" style="margin-top:8px;display:block" @click="$router.push('/portal/matter-detail/'+m.matterId)">查看详情</el-link>
            </el-card>
          </el-col>
        </el-row></el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" header="快捷入口">
          <el-button type="primary" plain block style="width:100%;margin-bottom:8px" @click="$router.push('/portal/apply')">我要申报</el-button>
          <el-button plain block style="width:100%;margin-bottom:8px" @click="$router.push('/portal/my-apply')">我的办件</el-button>
          <el-button plain block style="width:100%" @click="$router.push('/portal/notice')">通知公告</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { portalHotMatters } from '@/api/community/matter'

export default {
  name: 'PortalHome',
  dicts: ['comm_matter_category'],
  data() {
    return { hot: [], loading: false }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.loading = true
      portalHotMatters(8).then(res => {
        this.hot = res.data || []
      }).finally(() => { this.loading = false })
    }
  }
}
</script>
<style scoped>
.mb16 { margin-bottom: 16px; }
.mb12 { margin-bottom: 12px; }
</style>
