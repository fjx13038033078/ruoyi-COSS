<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" inline size="small">
      <el-form-item label="办件编号"><el-input v-model="queryParams.applyNo" clearable /></el-form-item>
      <el-form-item label="状态"><el-select v-model="queryParams.status" clearable placeholder="全部"><el-option v-for="d in dict.type.comm_apply_status" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="编号" prop="applyNo" width="150"/>
      <el-table-column label="事项" prop="matterName" min-width="160"/>
      <el-table-column label="申请人" prop="applicantName" width="100"/>
      <el-table-column label="联系电话" prop="phone" width="120"/>
      <el-table-column label="状态" width="100"><template slot-scope="s"><dict-tag :options="dict.type.comm_apply_status" :value="s.row.status"/></template></el-table-column>
      <el-table-column label="提交时间" prop="createTime" width="165"/>
      <el-table-column label="操作" width="260" fixed="right">
        <template slot-scope="s">
          <el-button type="text" size="mini" @click="openDetail(s.row)">详情</el-button>
          <el-button v-hasPermi="['community:apply:audit']" v-if="s.row.status==='0'" type="text" size="mini" @click="doReject(s.row)">驳回</el-button>
          <el-button v-hasPermi="['community:apply:audit']" v-if="s.row.status==='0'" type="text" size="mini" @click="doAccept(s.row)">受理</el-button>
          <el-button v-hasPermi="['community:apply:audit']" v-if="s.row.status==='2'" type="text" size="mini" @click="doFinish(s.row)">办结</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog title="申办详情" :visible.sync="detailVisible" width="720px" append-to-body @closed="detail=null">
      <div v-if="detail">
        <p><dict-tag :options="dict.type.comm_apply_status" :value="detail.status"/></p>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="办件编号">{{ detail.applyNo }}</el-descriptions-item>
          <el-descriptions-item label="办事事项">{{ detail.matterName }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ detail.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="电话">{{ detail.phone }}</el-descriptions-item>
          <el-descriptions-item label="备注说明" :span="2">{{ detail.applyRemark || '-' }}</el-descriptions-item>
          <el-descriptions-item label="驳回原因" :span="2" v-if="detail.rejectReason">{{ detail.rejectReason }}</el-descriptions-item>
          <el-descriptions-item label="意见" :span="2" v-if="detail.opinion">{{ detail.opinion }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="detail.attachmentList && detail.attachmentList.length" style="margin-top:12px">
          <div style="margin-bottom:6px">附件列表</div>
          <el-tag v-for="a in detail.attachmentList" :key="a.id||a.fileUrl" style="margin:4px">
            <a :href="base+a.fileUrl" target="_blank">{{ a.fileName || '文件' }}</a>
          </el-tag>
        </div>
      </div>
    </el-dialog>

    <el-dialog title="驳回" :visible.sync="rejVisible" width="480px" append-to-body>
      <el-form><el-form-item label="驳回原因"><el-input v-model="rejectReason" type="textarea" rows="3"/></el-form-item></el-form>
      <span slot="footer"><el-button @click="rejVisible=false">取消</el-button><el-button type="primary" @click="confirmReject">确定</el-button></span>
    </el-dialog>

    <el-dialog title="办结归档" :visible.sync="finVisible" width="520px" append-to-body @closed="finishRow=null">
      <el-form label-width="120px"><el-form-item label="办结意见"><el-input v-model="finishOpinion" type="textarea" rows="3"/></el-form-item>
      <el-form-item label="结果文件路径"><el-input v-model="resultFileUrl" placeholder="相对路径，与上传组件一致"/></el-form-item></el-form>
      <span slot="footer"><el-button @click="finVisible=false">取消</el-button><el-button type="primary" @click="confirmFinish">确定</el-button></span>
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
      if (!this.rejectReason) { this.$modal.msgWarning('请填写驳回原因'); return }
      rejectApply({ applyId: this.rejectApplyId, rejectReason: this.rejectReason }).then(() => {
        this.$modal.msgSuccess('已驳回')
        this.rejVisible = false
        this.getList()
      })
    },
    doAccept(row) {
      acceptApply({ applyId: row.applyId }).then(() => {
        this.$modal.msgSuccess('已受理')
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
        this.$modal.msgSuccess('办结成功')
        this.finVisible = false
        this.getList()
      })
    }
  }
}
</script>
