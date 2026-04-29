<template>
  <el-card shadow="never" header="Doorstep visit booking">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px" style="max-width:520px">
      <el-form-item label="Address" prop="address"><el-input v-model="form.address" placeholder="Building / street"/></el-form-item>
      <el-form-item label="Description"><el-input v-model="form.matterDesc" type="textarea" rows="3"/></el-form-item>
      <el-form-item label="Preferred time" prop="expectedTime">
        <el-date-picker v-model="form.expectedTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="Pick date/time" style="width:100%"/>
      </el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" @click="submit">Submit</el-button></el-form-item>
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
        address: [{ required: true, message: 'Required', trigger: 'blur' }],
        expectedTime: [{ required: true, message: 'Required', trigger: 'change' }]
      }
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate(ok => {
        if (!ok) return
        this.loading = true
        portalSubmitVisit(this.form).then(() => {
          this.$modal.msgSuccess('Booking submitted')
          this.form = { address: '', matterDesc: '', expectedTime: '' }
          this.resetForm('form')
        }).finally(() => { this.loading = false })
      })
    }
  }
}
</script>
