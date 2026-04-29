<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" inline size="small">
      <el-form-item label="Name"><el-input v-model="queryParams.applicantName" clearable placeholder="resident name"/></el-form-item>
      <el-form-item label="Status">
        <el-select v-model="queryParams.status" clearable placeholder="All">
          <el-option label="Pending" value="0"/>
          <el-option label="Accepted" value="1"/>
          <el-option label="Done" value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">Search</el-button><el-button icon="el-icon-refresh" @click="resetQuery">Reset</el-button></el-form-item>
    </el-form>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="ID" prop="visitId" width="80"/>
      <el-table-column label="Name" prop="applicantName" width="110"/>
      <el-table-column label="Phone" prop="phone" width="120"/>
      <el-table-column label="Address" prop="address" min-width="180" show-overflow-tooltip/>
      <el-table-column label="Expected" prop="expectedTime" width="165"/>
      <el-table-column label="Status" width="110">
        <template slot-scope="s">
          <el-tag v-if="s.row.status==='0'" type="warning">Pending</el-tag>
          <el-tag v-else-if="s.row.status==='1'" type="primary">Accepted</el-tag>
          <el-tag v-else-if="s.row.status==='2'" type="success">Done</el-tag>
          <span v-else>{{ s.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" width="200" fixed="right">
        <template slot-scope="s">
          <el-button type="text" size="mini" @click="openDetail(s.row)">Detail</el-button>
          <el-button v-hasPermi="['community:visit:handle']" v-if="s.row.status==='0'" type="text" size="mini" @click="doAccept(s.row)">Accept</el-button>
          <el-button v-hasPermi="['community:visit:handle']" v-if="s.row.status==='1'" type="text" size="mini" @click="doComplete(s.row)">Complete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog title="Visit detail" :visible.sync="dlg" width="600px" append-to-body>
      <pre v-if="current" style="white-space:pre-wrap;font-family:inherit">{{ summaryText }}</pre>
    </el-dialog>

    <el-dialog title="Complete visit" :visible.sync="compDlg" width="480px" append-to-body>
      <el-form><el-form-item label="Summary"><el-input v-model="summaryTxt" type="textarea" rows="4"/></el-form-item></el-form>
      <span slot="footer"><el-button @click="compDlg=false">Cancel</el-button><el-button type="primary" @click="submitComplete">OK</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { listVisit, getVisit, acceptVisit, completeVisit } from '@/api/community/visit'

export default {
  name: 'CommVisit',
  data() {
    return {
      loading: false,
      showSearch: true,
      dataList: [],
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, applicantName: undefined, status: undefined },
      dlg: false,
      current: null,
      compDlg: false,
      completeVisitId: null,
      summaryTxt: ''
    }
  },
  computed: {
    summaryText() {
      if (!this.current) return ''
      const v = this.current
      return JSON.stringify({
        visitId: v.visitId, applicantName: v.applicantName, phone: v.phone,
        address: v.address, matterDesc: v.matterDesc, expectedTime: v.expectedTime,
        status: v.status, handlerName: v.handlerName, summary: v.summary,
        finishTime: v.finishTime
      }, null, 2)
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listVisit(this.queryParams).then(res => {
        this.dataList = res.rows || []
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    openDetail(row) {
      getVisit(row.visitId).then(res => {
        this.current = res.data
        this.dlg = true
      })
    },
    doAccept(row) {
      acceptVisit({ visitId: row.visitId }).then(() => {
        this.$modal.msgSuccess('Accepted')
        this.getList()
      })
    },
    doComplete(row) {
      this.completeVisitId = row.visitId
      this.summaryTxt = ''
      this.compDlg = true
    },
    submitComplete() {
      if (!this.summaryTxt) { this.$modal.msgWarning('Enter summary'); return }
      completeVisit({ visitId: this.completeVisitId, summary: this.summaryTxt }).then(() => {
        this.$modal.msgSuccess('Saved')
        this.compDlg = false
        this.getList()
      })
    }
  }
}
</script>
