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

    <el-dialog title="办理详情" :visible.sync="detailOpen" width="680px" append-to-body custom-class="portal-my-apply-detail-dialog" @closed="detail=null">
      <div v-if="detail" class="portal-detail-body">
        <div class="portal-detail-status">
          <dict-tag :options="dict.type.comm_apply_status" :value="detail.status"/>
        </div>
        <p><b>编号：</b> {{ detail.applyNo }}</p>
        <p><b>事项：</b> {{ detail.matterName }}</p>
        <p><b>姓名 / 电话：</b> {{ detail.applicantName }} / {{ detail.phone }}</p>
        <p><b>身份证号：</b> {{ detail.idCard || '-' }}</p>
        <p v-if="detail.applyRemark"><b>申请说明：</b> {{ detail.applyRemark }}</p>
        <p v-if="detail.rejectReason"><b>驳回原因：</b> {{ detail.rejectReason }}</p>
        <p v-if="detail.opinion"><b>办结意见：</b> {{ detail.opinion }}</p>
        <div v-if="detail.status === '3' && resultFinishUrls.length" class="attach-block">
          <p><b>办结结果文件</b></p>
          <ul class="attach-ul">
            <li v-for="(url, idx) in resultFinishUrls" :key="'r'+idx">
              <el-link :href="fileLink(url)" type="primary" target="_blank" :underline="false">
                <i class="el-icon-document"/> {{ fileBasename(url) }}
              </el-link>
            </li>
          </ul>
        </div>
        <div v-else-if="detail.status === '3'" class="attach-block muted-line">
          <p><b>办结结果文件</b></p>
          <p class="muted-tip">暂无上传</p>
        </div>
        <div v-if="detail.attachmentList && detail.attachmentList.length" class="attach-block">
          <p><b>申请材料</b></p>
          <ul class="attach-ul">
            <li v-for="a in detail.attachmentList" :key="a.id">
              <el-link :href="fileLink(a.fileUrl)" type="primary" target="_blank">{{ a.fileName || a.fileUrl }}</el-link>
            </li>
          </ul>
        </div>
      </div>
    </el-dialog>

    <el-dialog
      title="补正后重新提交"
      :visible.sync="resubmitOpen"
      width="620px"
      append-to-body
      @close="onResubmitClose"
    >
      <el-form v-if="resubmitRow" ref="resForm" v-loading="resubmitLoading" :model="resForm" :rules="resRules" label-width="100px">
        <el-form-item label="姓名" prop="applicantName"><el-input v-model="resForm.applicantName"/></el-form-item>
        <el-form-item label="手机" prop="phone"><el-input v-model="resForm.phone" maxlength="11"/></el-form-item>
        <el-form-item label="身份证号" prop="idCard"><el-input v-model="resForm.idCard" maxlength="18"/></el-form-item>
        <el-form-item label="备注" prop="applyRemark"><el-input v-model="resForm.applyRemark" type="textarea" rows="3"/></el-form-item>
        <el-form-item label="材料附件">
          <file-upload
            v-model="resubmitAttachmentUrls"
            :limit="10"
            :file-size="10"
            :file-type="attachFileTypes"
          />
        </el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="resubmitOpen=false">取消</el-button><el-button type="primary" :loading="resubmitSaving" @click="doResubmit">提交</el-button></span>
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
import { validMobile, validIdCard } from '@/utils/validate'

const ATTACH_FILE_TYPES = ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'pdf', 'jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'zip', 'rar', '7z']

export default {
  name: 'PortalMyApply',
  dicts: ['comm_apply_status', 'comm_evaluation_level'],
  data() {
    const checkPhone = (rule, value, callback) => {
      if (!value || !value.trim()) {
        callback(new Error('请输入手机号码'))
        return
      }
      if (!validMobile(value.trim())) {
        callback(new Error('请输入正确的11位中国大陆手机号'))
      } else {
        callback()
      }
    }
    const checkIdCard = (rule, value, callback) => {
      if (!value || !value.trim()) {
        callback(new Error('请输入身份证号码'))
        return
      }
      if (!validIdCard(value.trim())) {
        callback(new Error('身份证号码格式或校验码不正确'))
      } else {
        callback()
      }
    }
    return {
      attachFileTypes: ATTACH_FILE_TYPES,
      resubmitAttachmentUrls: '',
      resubmitSaving: false,
      resubmitLoading: false,
      baseApi: process.env.VUE_APP_BASE_API,
      loading: false,
      list: [],
      detailOpen: false,
      detail: null,
      resubmitOpen: false,
      resubmitRow: null,
      resForm: { applicantName: '', phone: '', applyRemark: '', idCard: '' },
      resRules: {
        applicantName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ validator: checkPhone, trigger: 'blur' }],
        idCard: [{ validator: checkIdCard, trigger: 'blur' }],
        applyRemark: [{ max: 1000, message: '备注不超过1000字', trigger: 'blur' }]
      },
      evalOpen: false,
      evalRow: null,
      evForm: { score: 5, evaluationLevel: '', content: '' }
    }
  },
  computed: {
    /** 已办结时，结果文件相对路径拆成列表用于展示 */
    resultFinishUrls() {
      if (!this.detail || !this.detail.resultFileUrl) return []
      return String(this.detail.resultFileUrl).split(',').map(s => s.trim()).filter(Boolean)
    }
  },
  created() { this.loadList() },
  methods: {
    fileLink(url) {
      if (!url) return ''
      if (/^https?:\/\//i.test(url)) return url
      return this.baseApi + url
    },
    fileBasename(url) {
      if (!url) return '文件'
      const p = String(url).trim()
      const i = p.lastIndexOf('/')
      return i >= 0 ? p.slice(i + 1) : p
    },
    buildAttachmentList(urlStr) {
      if (!urlStr || !String(urlStr).trim()) return []
      return String(urlStr).split(',').map(s => s.trim()).filter(Boolean).map(fileUrl => ({
        fileUrl,
        fileName: fileUrl.includes('/') ? fileUrl.slice(fileUrl.lastIndexOf('/') + 1) : fileUrl
      }))
    },
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
      this.resubmitOpen = true
      this.resubmitLoading = true
      this.resubmitAttachmentUrls = ''
      portalMyApplyDetail(row.applyId).then(res => {
        const d = res.data || {}
        this.resForm = {
          applicantName: d.applicantName || '',
          phone: (d.phone || '').trim(),
          applyRemark: d.applyRemark || '',
          idCard: (d.idCard || '').trim()
        }
        const urls = (d.attachmentList || []).map(a => a.fileUrl).filter(Boolean)
        this.resubmitAttachmentUrls = urls.join(',')
      }).finally(() => {
        this.resubmitLoading = false
        this.$nextTick(() => {
          if (this.$refs.resForm) this.$refs.resForm.clearValidate()
        })
      })
    },
    onResubmitClose() {
      this.resubmitRow = null
      this.resubmitAttachmentUrls = ''
    },
    doResubmit() {
      this.$refs.resForm.validate(valid => {
        if (!valid) return
        const row = this.resubmitRow
        this.resubmitSaving = true
        portalResubmitApply(row.applyId, {
          matterId: row.matterId,
          applicantName: this.resForm.applicantName.trim(),
          phone: this.resForm.phone.trim(),
          idCard: this.resForm.idCard.trim(),
          applyRemark: this.resForm.applyRemark,
          attachmentList: this.buildAttachmentList(this.resubmitAttachmentUrls)
        }).then(() => {
          this.$modal.msgSuccess('已重新提交')
          this.resubmitOpen = false
          this.loadList()
        }).finally(() => { this.resubmitSaving = false })
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

<style scoped>
.portal-detail-body { margin-top: -4px; }
.portal-detail-status { margin-bottom: 14px; }
.attach-block { margin-top: 10px }
.attach-ul { margin: 4px 0 0 18px; padding: 0; color: #606266 }
.attach-ul li { margin-bottom: 4px }
.muted-line .muted-tip { margin: 6px 0 0; color: #909399; font-size: 13px }
</style>
