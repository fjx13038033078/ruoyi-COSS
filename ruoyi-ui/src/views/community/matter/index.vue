<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" inline size="small">
      <el-form-item label="事项名称"><el-input v-model="queryParams.matterName" clearable @keyup.enter.native="handleQuery"/></el-form-item>
      <el-form-item label="状态"><el-select v-model="queryParams.status" placeholder="全部" clearable><el-option v-for="d in dict.type.sys_normal_disable" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['community:matter:add']">新增</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="序号" align="center" width="55" type="index"/>
      <el-table-column label="事项名称" prop="matterName" min-width="200" show-overflow-tooltip/>
      <el-table-column label="分类" width="110"><template slot-scope="s"><dict-tag :options="dict.type.comm_matter_category" :value="s.row.category"/></template></el-table-column>
      <el-table-column label="状态" width="90"><template slot-scope="s"><dict-tag :options="dict.type.sys_normal_disable" :value="s.row.status"/></template></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160"/>
      <el-table-column label="操作" align="center" width="170" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['community:matter:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['community:matter:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="事项名称" prop="matterName"><el-input v-model="form.matterName"/></el-form-item>
        <el-row>
          <el-col :span="12"><el-form-item label="分类" prop="category"><el-select v-model="form.category" style="width:100%"><el-option v-for="d in dict.type.comm_matter_category" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="缓急"><el-select v-model="form.priority" style="width:100%"><el-option v-for="d in dict.type.comm_matter_priority" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="责任部门" prop="deptId">
          <treeselect v-model="form.deptId" :options="deptTree" :normalizer="deptNormalizer" :show-count="true" placeholder="请选择部门"/>
        </el-form-item>
        <el-form-item label="所需材料"><el-input v-model="form.requiredDocs" type="textarea" rows="3"/></el-form-item>
        <el-form-item label="承诺时限（天）"><el-input-number v-model="form.expectDays" :min="1" controls-position="right"/></el-form-item>
        <el-form-item label="是否启用"><el-radio-group v-model="form.status"><el-radio v-for="d in dict.type.sys_normal_disable" :key="d.value" :label="d.value">{{d.label}}</el-radio></el-radio-group></el-form-item>
        <el-form-item label="办事流程"><editor v-model="form.processDesc" :min-height="180"/></el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer"><el-button type="primary" @click="submitForm">确定</el-button><el-button @click="dlgCancel">取消</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { listDept } from '@/api/system/dept'
import { listMatter, getMatter, addMatter, updateMatter, delMatter } from '@/api/community/matter'

export default {
  name: 'CommMatter',
  dicts: ['comm_matter_category', 'comm_matter_priority', 'sys_normal_disable'],
  components: { Treeselect },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      dataList: [],
      queryParams: { pageNum: 1, pageSize: 10, matterName: undefined, status: undefined },
      open: false,
      title: '',
      deptTree: [],
      form: {},
      rules: {
        matterName: [{ required: true, message: '请输入事项名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择分类', trigger: 'change' }],
        deptId: [{ required: true, message: '请选择责任部门', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadDeptTree()
  },
  methods: {
    blankForm() {
      this.form = { matterId: undefined, matterName: '', category: '', priority: 'normal', deptId: undefined, requiredDocs: '', expectDays: 5, processDesc: '', status: '0' }
    },
    loadDeptTree() {
      listDept().then(res => {
        this.deptTree = this.handleTree(res.data, 'deptId')
      })
    },
    deptNormalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      }
    },
    getList() {
      this.loading = true
      listMatter(this.queryParams).then(res => {
        this.dataList = res.rows || []
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    },
    dlgCancel() {
      this.open = false
      this.blankForm()
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleAdd() {
      this.blankForm()
      this.title = '新增事项'
      this.open = true
    },
    handleUpdate(row) {
      getMatter(row.matterId).then(res => {
        this.form = res.data || {}
        this.title = '修改事项'
        this.open = true
      })
    },
    submitForm() {
      this.$refs.form.validate(ok => {
        if (!ok) return
        const api = this.form.matterId != null ? updateMatter : addMatter
        api(this.form).then(() => {
          this.$modal.msgSuccess('保存成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleDelete(row) {
      this.$modal.confirm('确定删除该事项吗？').then(() => delMatter(row.matterId)).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>
