<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 200px" v-model="data.title" placeholder="查询标题" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div>
      <el-table :data="data.tableData" stripe :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="user" label="发布人"></el-table-column>
        <el-table-column prop="time" label="发布时间"></el-table-column>
        <el-table-column label="是否公开">
          <template #default="scope">
            <el-switch v-model="scope.row.open" @change="changeOpen(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination v-model:pageSize="data.pageSize" v-model:current-page="data.pageNum"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

    <el-dialog title="公告信息" v-model="data.formVisible" width="40%">
      <el-form :model="data.form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="data.form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="data.form.content" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request";

const data = reactive({
  tableData: [],  // 所有的数据
  pageNum: 1,   // 当前的页码
  pageSize: 5,  // 每页显示的个数
  total: 0,
  formVisible: false,
  form: {},
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  ids: [],
  title: '',
})

const rules = reactive({
  title: [
    {required: true, message: '请输入标题', trigger: 'blur'},
  ],
  content: [
    {required: true, message: '请输入内容', trigger: 'blur'},
  ],
})


const changeOpen = (form) => {
  // 调用更新的接口  更新数据到数据库
  data.form = JSON.parse(JSON.stringify(form)) //深拷贝
  sendSaveRequest()   // 直接发请求就可以了
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('您确认批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/notice/delete/batch', {data: data.ids}).then(res => {
      if (res.code === '200') {   // 表示操作成功
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)  // 弹出错误的信息
      }
    })
  }).catch(() => {
  })
}

const handleSelectionChange = (rows) => {   // 当前选中的所有的行数据
  data.ids = rows.map(v => v.id)
}

const del = (id) => {
  ElMessageBox.confirm('您确认删除吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/notice/delete/' + id).then(res => {
      if (res.code === '200') {   // 表示操作成功
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)  // 弹出错误的信息
      }
    })
  }).catch(() => {
  })
}

const handleEdit = (row) => {   // 编辑数据
  data.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
  data.formVisible = true   // 打开弹窗
}

const handleAdd = () => {   // 新增数据
  data.form = {}  // 新增数据的时候清空数据
  data.formVisible = true   // 打开弹窗
}

const formRef = ref()
const save = () => {   // 保存按钮触发的逻辑  它会触发新增或者更新
  formRef.value.validate((valid) => {
    if (valid) {
      sendSaveRequest()
    }
  })
}

const sendSaveRequest = () => {
  request.request({
    url: data.form.id ? '/notice/update' : '/notice/add',
    method: data.form.id ? 'PUT' : 'POST',
    data: data.form
  }).then(res => {
    if (res.code === '200') {  // 表示成功保存
      ElMessage.success('保存成功')
      load()
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)  // 弹出错误的信息
    }
  })
}

const reset = () => {
  data.title = ''
  load()
}


const load = () => {
  // 分页查询
  request.get('/notice/selectByPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
    }
  }).then(res => {
    data.tableData = res.data?.records || []
    data.total = res.data?.total || 0
  })
}

load()

const handleCurrentChange = (pageNum) => {
  load()
}
</script>

<style>
.el-tooltip__popper {
  max-width: 200px !important;
}
</style>