<template>
  <el-card shadow="never" header="已发布办事事项">
    <el-form inline size="small" @submit.native.prevent="handleQuery">
      <el-form-item label="名称"><el-input v-model="queryParams.matterName" clearable placeholder="事项名称" style="width:200px"/></el-form-item>
      <el-form-item><el-button type="primary" @click="handleQuery">查询</el-button></el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="matterList">
      <el-table-column label="事项名称" prop="matterName" min-width="220" />
      <el-table-column label="分类" prop="category" width="130">
        <template slot-scope="s"><dict-tag :options="dict.type.comm_matter_category" :value="s.row.category"/></template>
      </el-table-column>
      <el-table-column label="承诺时限（天）" prop="expectDays" width="130"/>
      <el-table-column label="操作" width="110" align="center">
        <template slot-scope="s"><el-button type="text" @click="$router.push('/portal/matter-detail/'+s.row.matterId)">详情</el-button></template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </el-card>
</template>

<script>
import { portalMatterList } from '@/api/community/matter'

export default {
  name: 'PortalMatterList',
  dicts: ['comm_matter_category'],
  data() {
    return {
      loading: false,
      matterList: [],
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, matterName: undefined }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      portalMatterList(this.queryParams).then(res => {
        this.matterList = res.rows || []
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    }
  }
}
</script>
