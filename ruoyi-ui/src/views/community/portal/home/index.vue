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
          <!-- flex 纵向 + stretch 对齐整列宽度；gap 取代各按钮手写 margin，避免垂直不齐 -->
          <div class="shortcut-btns">
            <el-button type="primary" plain class="shortcut-btn" @click="$router.push('/portal/apply')">我要申报</el-button>
            <el-button plain class="shortcut-btn" @click="$router.push('/portal/my-apply')">我的办件</el-button>
            <el-button plain class="shortcut-btn" @click="$router.push('/portal/notice')">通知公告</el-button>
          </div>
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
.shortcut-btns {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 10px;
}
/* Element 按钮默认可能有 margin-left/+last 等，统一拉满宽度并清零侧向 margin，保证三块左缘右缘对齐 */
.shortcut-btns .shortcut-btn {
  width: 100%;
  margin: 0;
}
.shortcut-btns .shortcut-btn + .shortcut-btn {
  margin-left: 0;
}
</style>
