<template>
  <div>
    <el-card shadow="never" v-loading="loading" header="My applications">
      <el-table :data="list">
        <el-table-column label="No." prop="applyNo" width="150"/>
        <el-table-column label="Matter" prop="matterName" min-width="180"/>
        <el-table-column label="Status" prop="status" width="120">
          <template slot-scope="s"><dict-tag :options="dict.type.comm_apply_status" :value="s.row.status"/></template>
        </el-table-column>
        <el-table-column label="Created" prop="createTime" width="165"/>
        <el-table-column label="Actions" width="210" fixed="right">
          <template slot-scope="s">
            <el-button type="text" size="mini" @click="openDetail(s.row.applyId)">Detail</el-button>
            <el-button v-if="s.row.status==='1'" type="text" size="mini" @click="openResubmit(s.row)">Resubmit</el-button>
            <el-button v-if="s.row.status==='3'" type="text" size="mini" @click="openEval(s.row)">Review</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="Application detail" :visible.sync="detailOpen" width="640px" append-to-body>
      <div v-if="detail">
        <p><b>No.</b> {{ detail.applyNo }} <dict-tag style="margin-left:8px" :options="dict.type.comm_apply_status" :value="detail.status"/></p>
        <p><b>Matter:</b> {{ detail.matterName }}</p>
        <p><b>Name / Phone:</b> {{ detail.applicantName }} / {{ detail.phone }}</p>
        <p v-if="detail.rejectReason"><b>Reject reason:</b> {{ detail.rejectReason }}</p>
        <p v-if="detail.opinion"><b>Opinion:</b> {{ detail.opinion }}</p>
      </div>
    </el-dialog>

    <el-dialog title="Resubmit" :visible.sync="resubmitOpen" width="520px" append-to-body @close="resubmitRow=null">
      <el-form v-if="resubmitRow" label-width="100px">
        <el-form-item label="Name"><el-input v-model="resForm.applicantName"/></el-form-item>
        <el-form-item label="Phone"><el-input v-model="resForm.phone"/></el-form-item>
        <el-form-item label="Remark"><el-input v-model="resForm.applyRemark" type="textarea" rows="3"/></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="resubmitOpen=false">Cancel</el-button><el-button type="primary" @click="doResubmit">Submit</el-button></span>
    </el-dialog>

    <el-dialog title="Service review" :visible.sync="evalOpen" width="480px" append-to-body @close="evalRow=null">
      <el-form ref="evForm" :model="evForm" label-width="100px">
        <el-form-item label="Score" prop="score" :rules="[{ required: true, message: 'Required' }]">
          <el-rate v-model="evForm.score" :max="5"/>
        </el-form-item>
        <el-form-item label="Level" prop="evaluationLevel" :rules="[{ required: true, message: 'Required' }]">
          <el-select v-model="evForm.evaluationLevel" placeholder="Select" style="width:100%">
            <el-option v-for="d in dict.type.comm_evaluation_level" :key="d.value" :label="d.label" :value="d.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Comment"><el-input v-model="evForm.content" type="textarea" rows="3"/></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="evalOpen=false">Cancel</el-button><el-button type="primary" @click="submitEval">Submit</el-button></span>
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
        this.$modal.msgSuccess('Resubmitted')
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
          this.$modal.msgSuccess('Thanks!')
          this.evalOpen = false
          this.loadList()
        })
      })
    }
  }
}
</script>
