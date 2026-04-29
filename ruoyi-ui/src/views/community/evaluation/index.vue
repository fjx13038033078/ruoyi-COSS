<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" inline size="small">
      <el-form-item label="Apply no"><el-input v-model="queryParams.applyNo" clearable /></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">Search</el-button><el-button icon="el-icon-refresh" @click="resetQuery">Reset</el-button></el-form-item>
    </el-form>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="ID" prop="evaluationId" width="80"/>
      <el-table-column label="Apply no" prop="applyNo" width="150"/>
      <el-table-column label="Matter" prop="matterName" min-width="160"/>
      <el-table-column label="Score" prop="score" width="70"/>
      <el-table-column label="Level" width="120"><template slot-scope="s"><dict-tag :options="dict.type.comm_evaluation_level" :value="s.row.evaluationLevel"/></template></el-table-column>
      <el-table-column label="Content" prop="content" show-overflow-tooltip min-width="140"/>
      <el-table-column label="Time" prop="createTime" width="165"/>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { listEvaluation } from '@/api/community/evaluation'

export default {
  name: 'CommEvaluation',
  dicts: ['comm_evaluation_level'],
  data() {
    return {
      loading: false,
      showSearch: true,
      dataList: [],
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, applyNo: undefined }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listEvaluation(this.queryParams).then(res => {
        this.dataList = res.rows || []
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() }
  }
}
</script>
