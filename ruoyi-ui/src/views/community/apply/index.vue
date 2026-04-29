<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" inline size="small">
      <el-form-item label="Apply no"><el-input v-model="queryParams.applyNo" clearable /></el-form-item>
      <el-form-item label="Status"><el-select v-model="queryParams.status" clearable placeholder="All"><el-option v-for="d in dict.type.comm_apply_status" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">Search</el-button><el-button icon="el-icon-refresh" @click="resetQuery">Reset</el-button></el-form-item>
    </el-form>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="No." prop="applyNo" width="150"/>
      <el-table-column label="Matter" prop="matterName" min-width="160"/>
      <el-table-column label="Applicant" prop="applicantName" width="100"/>
      <el-table-column label="Phone" prop="phone" width="120"/>
      <el-table-column label="Status" width="100"><template slot-scope="s"><dict-tag :options="dict.type.comm_apply_status" :value="s.row.status"/></template></el-table-column>
      <el-table-column label="Created" prop="createTime" width="165"/>
      <el-table-column label="Actions" width="260" fixed="right">
        <template slot-scope="s">
          <el-button type="text" size="mini" @click="openDetail(s.row)">Detail</el-button>
          <el-button v-hasPermi="['community:apply:audit']" v-if="s.row.status==='0'" type="text" size="mini" @click="doReject(s.row)">Reject</el-button>
          <el-button v-hasPermi="['community:apply:audit']" v-if="s.row.status==='0'" type="text" size="mini" @click="doAccept(s.row)">Accept</el-button>
          <el-button v-hasPermi="['community:apply:audit']" v-if="s.row.status==='2'" type="text" size="mini" @click="doFinish(s.row)">Finish</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog title="Apply detail" :visible.sync="detailVisible" width="720px" append-to-body @closed="detail=null">
      <div v-if="detail">
        <p><dict-tag :options="dict.type.comm_apply_status" :value="detail.status"/></p>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="Apply no">{{ detail.applyNo }}</el-descriptions-item>
          <el-descriptions-item label="Matter">{{ detail.matterName }}</el-descriptions-item>
          <el-descriptions-item label="Name">{{ detail.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="Phone">{{ detail.phone }}</el-descriptions-item>
          <el-descriptions-item label="Remark" :span="2">{{ detail.applyRemark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Reject reason" :span="2" v-if="detail.rejectReason">{{ detail.rejectReason }}</el-descriptions-item>
          <el-descriptions-item label="Opinion" :span="2" v-if="detail.opinion">{{ detail.opinion }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="detail.attachmentList && detail.attachmentList.length" style="margin-top:12px">
          <div style="margin-bottom:6px">Attachments</div>
          <el-tag v-for="a in detail.attachmentList" :key="a.id||a.fileUrl" style="margin:4px">
            <a :href="base+a.fileUrl" target="_blank">{{ a.fileName || 'file' }}</a>
          </el-tag>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="Reject" :visible.sync="rejVisible" width="480px" append-to-body>
      <el-form><el-form-item label="Reason"><el-input v-model="rejectReason" type="textarea" rows="3"/></el-form-item></el-form>
      <span slot="footer"><el-button @click="rejVisible=false">Cancel</el-button><el-button type="primary" @click="confirmReject">OK</el-button></span>
    </el-dialog>

    <el-dialog title="Finish" :visible.sync="finVisible" width="520px" append-to-body @closed="finishRow=null">
      <el-form label-width="120px"><el-form-item label="Opinion"><el-input v-model="finishOpinion" type="textarea" rows="3"/></el-form-item>
      <el-form-item label="Result file URL"><el-input v-model="resultFileUrl" placeholder="/profile/upload relative URL"/></el-form-item></el-form>
      <span slot="footer"><el-button @click="finVisible=false">Cancel</el-button><el-button type="primary" @click="confirmFinish">OK</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { listApply, getApply, rejectApply, acceptApply, finishApply } from '@/api/community/apply'

export default {
  name: 'CommApply',
  dicts: ['comm_apply_status'],
  data() {
    return {
      loading: false,
      showSearch: true,
      dataList: [],
      total: 0,
      queryParams: { pageNum: 1, pageSize: 10, applyNo: undefined, status: undefined },
      detailVisible: false,
      detail: null,
      rejVisible: false,
      rejectApplyId: null,
      rejectReason: '',
      finVisible: false,
      finishRow: null,
      finishOpinion: '',
      resultFileUrl: ''
    }
  },
  computed: {
    base() { return process.env.VUE_APP_BASE_API }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listApply(this.queryParams).then(res => {
        this.dataList = res.rows || []
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    openDetail(row) {
      getApply(row.applyId).then(res => {
        this.detail = res.data
        this.detailVisible = true
      })
    },
    doReject(row) {
      this.rejectApplyId = row.applyId
      this.rejectReason = ''
      this.rejVisible = true
    },
    confirmReject() {
      if (!this.rejectReason) { this.$modal.msgWarning('Enter reject reason'); return }
      rejectApply({ applyId: this.rejectApplyId, rejectReason: this.rejectReason }).then(() => {
        this.$modal.msgSuccess('Rejected')
        this.rejVisible = false
        this.getList()
      })
    },
    doAccept(row) {
      acceptApply({ applyId: row.applyId }).then(() => {
        this.$modal.msgSuccess('Accepted')
        this.getList()
      })
    },
    doFinish(row) {
      this.finishRow = row
      this.finishOpinion = ''
      this.resultFileUrl = ''
      this.finVisible = true
    },
    confirmFinish() {
      finishApply({
        applyId: this.finishRow.applyId,
        opinion: this.finishOpinion,
        resultFileUrl: this.resultFileUrl
      }).then(() => {
        this.$modal.msgSuccess('Finished')
        this.finVisible = false
        this.getList()
      })
    }
  }
}
</script>
