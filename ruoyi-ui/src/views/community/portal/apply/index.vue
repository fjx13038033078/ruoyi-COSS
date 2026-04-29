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
      <el-form-item label="身份证号"><el-input v-model="form.idCard"/></el-form-item>
      <el-form-item label="备注说明"><el-input v-model="form.applyRemark" type="textarea" rows="4"/></el-form-item>
      <el-form-item><el-button type="primary" :loading="saving" @click="submit">提交申办</el-button></el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { portalMatterList } from '@/api/community/matter'
import { portalSubmitApply } from '@/api/community/apply'

export default {
  name: 'PortalApply',
  data() {
    return {
      matters: [],
      saving: false,
      form: { matterId: undefined, applicantName: '', phone: '', idCard: '', applyRemark: '', attachmentList: [] },
      rules: {
        matterId: [{ required: true, message: '请选择办事事项', trigger: 'change' }],
        applicantName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { pattern: /^1\d{10}$/, message: '手机号码格式不正确', trigger: 'blur' }
        ]
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
        portalSubmitApply(this.form).then(() => {
          this.$modal.msgSuccess('提交成功')
          this.$router.push('/portal/my-apply')
        }).finally(() => { this.saving = false })
      })
    }
  }
}
</script>
