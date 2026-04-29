<template>
  <div>
    <el-card shadow="never" v-loading="loading" header="我的申办记录">
      <el-table :data="list">
        <el-table-column label="办件编号" prop="applyNo" width="150"/>
        <el-table-column label="事项名称" prop="matterName" min-width="180"/>
        <el-table-column label="当前状态" prop="status" width="120">
          <template slot-scope="s"><dict-tag :options="dict.type.comm_apply_status" :value="s.row.status"/></template>
        </el-table-column>
        <el-table-column label="提交时间" prop="createTime" width="165"/>
        <el-table-column label="操作" width="210" fixed="right">
          <template slot-scope="s">
            <el-button type="text" size="mini" @click="openDetail(s.row.applyId)">详情</el-button>
            <el-button v-if="s.row.status==='1'" type="text" size="mini" @click="openResubmit(s.row)">补正重提</el-button>
            <el-button v-if="s.row.status==='3'" type="text" size="mini" @click="openEval(s.row)">满意度评价</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="办理详情" :visible.sync="detailOpen" width="640px" append-to-body>
      <div v-if="detail">
        <p><b>编号</b> {{ detail.applyNo }} <dict-tag style="margin-left:8px" :options="dict.type.comm_apply_status" :value="detail.status"/></p>
        <p><b>事项：</b> {{ detail.matterName }}</p>
        <p><b>姓名 / 电话：</b> {{ detail.applicantName }} / {{ detail.phone }}</p>
        <p v-if="detail.rejectReason"><b>驳回原因：</b> {{ detail.rejectReason }}</p>
        <p v-if="detail.opinion"><b>办结意见：</b> {{ detail.opinion }}</p>
      </div>
    </el-dialog>

    <el-dialog title="补正后重新提交" :visible.sync="resubmitOpen" width="520px" append-to-body @close="resubmitRow=null">
      <el-form v-if="resubmitRow" label-width="100px">
        <el-form-item label="姓名"><el-input v-model="resForm.applicantName"/></el-form-item>
        <el-form-item label="电话"><el-input v-model="resForm.phone"/></el-form-item>
        <el-form-item label="备注"><el-input v-model="resForm.applyRemark" type="textarea" rows="3"/></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="resubmitOpen=false">取消</el-button><el-button type="primary" @click="doResubmit">提交</el-button></span>
    </el-dialog>

    <el-dialog title="政务服务满意度评价" :visible.sync="evalOpen" width="480px" append-to-body @close="evalRow=null">
      <el-form ref="evForm" :model="evForm" label-width="100px">
        <el-form-item label="打分" prop="score" :rules="[{ required: true, message: '请打分' }]">
          <el-rate v-model="evForm.score" :max="5"/>
        </el-form-item>
        <el-form-item label="档位" prop="evaluationLevel" :rules="[{ required: true, message: '请选择评价档位' }]">
          <el-select v-model="evForm.evaluationLevel" placeholder="请选择" style="width:100%">
            <el-option v-for="d in dict.type.comm_evaluation_level" :key="d.value" :label="d.label" :value="d.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="意见"><el-input v-model="evForm.content" type="textarea" rows="3"/></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="evalOpen=false">取消</el-button><el-button type="primary" @click="submitEval">提交</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { portalMyApplyList, portalMyApplyDetail, portalResubmitApply } from '@/api/community/apply'
import { portalSubmitEvaluation } from '@/api/community/evaluation'

export default {
  name: 'PortalMyApply',
  dicts: ['comm_apply_status', 'comm_evaluation_level'],
  data() {
    return {
      loading: false,
      list: [],
      detailOpen: false,
      detail: null,
      resubmitOpen: false,
      resubmitRow: null,
      resForm: { applicantName: '', phone: '', applyRemark: '', idCard: '' },
      evalOpen: false,
      evalRow: null,
      evForm: { score: 5, evaluationLevel: '', content: '' }
    }
  },
  created() { this.loadList() },
  methods: {
    loadList() {
      this.loading = true
      portalMyApplyList().then(res => { this.list = res.data || [] }).finally(() => { this.loading = false })
    },
    openDetail(applyId) {
      portalMyApplyDetail(applyId).then(res => {
        this.detail = res.data
        this.detailOpen = true
      })
    },
    openResubmit(row) {
      this.resubmitRow = row
      this.resForm = {
        applicantName: row.applicantName,
        phone: row.phone,
        applyRemark: row.applyRemark,
        idCard: row.idCard
      }
      this.resubmitOpen = true
    },
    doResubmit() {
      const row = this.resubmitRow
      portalResubmitApply(row.applyId, { ...this.resForm, matterId: row.matterId, attachmentList: [] }).then(() => {
        this.$modal.msgSuccess('已重新提交')
        this.resubmitOpen = false
        this.loadList()
      })
    },
    openEval(row) {
      this.evalRow = row
      this.evForm = { score: 5, evaluationLevel: '', content: '' }
      this.evalOpen = true
      this.$nextTick(() => { if (this.$refs.evForm) this.$refs.evForm.clearValidate() })
    },
    submitEval() {
      this.$refs.evForm.validate(valid => {
        if (!valid) return
        portalSubmitEvaluation({
          applyId: this.evalRow.applyId,
          score: this.evForm.score,
          evaluationLevel: this.evForm.evaluationLevel,
          content: this.evForm.content
        }).then(() => {
          this.$modal.msgSuccess('感谢您的评价')
          this.evalOpen = false
          this.loadList()
        })
      })
    }
  }
}
</script>
