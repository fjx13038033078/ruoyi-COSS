<template>
  <el-card shadow="never" header="Notices (from system notice module)">
    <el-table v-loading="loading" :data="rows">
      <el-table-column label="Title" prop="noticeTitle" min-width="220"/>
      <el-table-column label="Type" prop="noticeType" width="100">
        <template slot-scope="s"><dict-tag :options="dict.type.sys_notice_type" :value="s.row.noticeType"/></template>
      </el-table-column>
      <el-table-column label="Time" prop="createTime" width="170"/>
      <el-table-column label="Content" prop="noticeContent">
        <template slot-scope="s">
          <div class="html-snippet" v-html="s.row.noticeContent"/>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="query.pageNum" :limit.sync="query.pageSize" @pagination="getList"/>
  </el-card>
</template>

<script>
import { listNotice } from '@/api/system/notice'

export default {
  name: 'PortalNotice',
  dicts: ['sys_notice_type'],
  data() {
    return {
      loading: false,
      rows: [],
      total: 0,
      query: { pageNum: 1, pageSize: 10, status: '0' }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listNotice(this.query).then(res => {
        this.rows = res.rows || []
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    }
  }
}
</script>
<style scoped>
.html-snippet { max-height: 120px; overflow: auto; line-height: 1.45; font-size: 13px; }
</style>
