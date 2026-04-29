<template>
  <el-card shadow="never" header="在线提交申办">
    <el-alert title="填报下方表单；进度请在「我的办件」中查看。" type="info" :closable="false" show-icon style="margin-bottom:12px"/>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="max-width:620px">
      <el-form-item label="办事事项" prop="matterId">
        <el-select v-model="form.matterId" filterable placeholder="请选择事项" style="width:100%">
          <el-option v-for="r in matters" :key="r.matterId" :label="r.matterName" :value="r.matterId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="姓名" prop="applicantName"><el-input v-model="form.applicantName"/></el-form-item>
      <el-form-item label="手机" prop="phone"><el-input v-model="form.phone" maxlength="11"/></el-form-item>
      <el-form-item label="身份证号" prop="idCard"><el-input v-model="form.idCard" maxlength="18"/></el-form-item>
      <el-form-item label="备注说明" prop="applyRemark"><el-input v-model="form.applyRemark" type="textarea" rows="4"/></el-form-item>
      <el-form-item label="材料附件">
        <file-upload
          v-model="attachmentUrls"
          :limit="10"
          :file-size="10"
          :file-type="attachFileTypes"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="submit">提交申办</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { portalMatterList } from '@/api/community/matter'
import { portalSubmitApply } from '@/api/community/apply'
import { validMobile, validIdCard } from '@/utils/validate'

/** 常见办事材料：与若依 FileUpload 白名单一致扩展图片、压缩包 */
const ATTACH_FILE_TYPES = ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'pdf', 'jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'zip', 'rar', '7z']

export default {
  name: 'PortalApply',
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
      attachmentUrls: '',
      matters: [],
      saving: false,
      form: {
        matterId: undefined,
        applicantName: '',
        phone: '',
        idCard: '',
        applyRemark: ''
      },
      rules: {
        matterId: [{ required: true, message: '请选择办事事项', trigger: 'change' }],
        applicantName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ validator: checkPhone, trigger: 'blur' }],
        idCard: [{ validator: checkIdCard, trigger: 'blur' }],
        applyRemark: [{ max: 1000, message: '备注不超过1000字', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadMatters()
  },
  watch: {
    '$route'(r) {
      const mid = r.query && r.query.matterId
      if (mid) this.form.matterId = Number(mid)
    }
  },
  methods: {
    buildAttachmentList(urlStr) {
      if (!urlStr || !String(urlStr).trim()) return []
      return String(urlStr).split(',').map(s => s.trim()).filter(Boolean).map(fileUrl => ({
        fileUrl,
        fileName: fileUrl.includes('/') ? fileUrl.slice(fileUrl.lastIndexOf('/') + 1) : fileUrl
      }))
    },
    loadMatters() {
      portalMatterList({ pageNum: 1, pageSize: 200 }).then(res => {
        this.matters = res.rows || []
        const q = this.$route.query.matterId
        if (q) this.form.matterId = Number(q)
      })
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.saving = true
        const payload = {
          ...this.form,
          phone: (this.form.phone || '').trim(),
          idCard: (this.form.idCard || '').trim(),
          attachmentList: this.buildAttachmentList(this.attachmentUrls)
        }
        portalSubmitApply(payload).then(() => {
          this.$modal.msgSuccess('提交成功')
          this.$router.push('/portal/my-apply')
        }).finally(() => { this.saving = false })
      })
    }
  }
}
</script>
