<template>
  <el-card shadow="never" header="上门服务预约">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="max-width:520px">
      <el-form-item label="上门地址" prop="address"><el-input v-model="form.address" placeholder="楼栋门牌号、街道"/></el-form-item>
      <el-form-item label="需求说明"><el-input v-model="form.matterDesc" type="textarea" rows="3"/></el-form-item>
      <el-form-item label="期望时间" prop="expectedTime">
        <el-date-picker v-model="form.expectedTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期与时间" style="width:100%"/>
      </el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" @click="submit">提交预约</el-button></el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import { portalSubmitVisit } from '@/api/community/visit'

export default {
  name: 'PortalVisit',
  data() {
    return {
      loading: false,
      form: { address: '', matterDesc: '', expectedTime: '' },
      rules: {
        address: [{ required: true, message: '请填写上门地址', trigger: 'blur' }],
        expectedTime: [{ required: true, message: '请选择期望上门时间', trigger: 'change' }]
      }
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate(ok => {
        if (!ok) return
        this.loading = true
        portalSubmitVisit(this.form).then(() => {
          this.$modal.msgSuccess('预约已提交')
          this.form = { address: '', matterDesc: '', expectedTime: '' }
          this.resetForm('form')
        }).finally(() => { this.loading = false })
      })
    }
  }
}
</script>
