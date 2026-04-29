<template>
  <el-card shadow="never" header="Submit application">
    <el-alert title="Fill the form below. Track status under My Applications." type="info" :closable="false" show-icon style="margin-bottom:12px"/>
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="max-width:620px">
      <el-form-item label="Matter" prop="matterId">
        <el-select v-model="form.matterId" filterable placeholder="Select matter" style="width:100%">
          <el-option v-for="r in matters" :key="r.matterId" :label="r.matterName" :value="r.matterId"/>
        </el-select>
      </el-form-item>
      <el-form-item label="Name" prop="applicantName"><el-input v-model="form.applicantName"/></el-form-item>
      <el-form-item label="Mobile" prop="phone"><el-input v-model="form.phone" maxlength="11"/></el-form-item>
      <el-form-item label="ID card"><el-input v-model="form.idCard"/></el-form-item>
      <el-form-item label="Remark"><el-input v-model="form.applyRemark" type="textarea" rows="4"/></el-form-item>
      <el-form-item><el-button type="primary" :loading="saving" @click="submit">Submit</el-button></el-form-item>
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
        matterId: [{ required: true, message: 'Required', trigger: 'change' }],
        applicantName: [{ required: true, message: 'Required', trigger: 'blur' }],
        phone: [
          { required: true, message: 'Required', trigger: 'blur' },
          { pattern: /^1\d{10}$/, message: 'Invalid phone', trigger: 'blur' }
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
          this.$modal.msgSuccess('Submitted')
          this.$router.push('/portal/my-apply')
        }).finally(() => { this.saving = false })
      })
    }
  }
}
</script>
