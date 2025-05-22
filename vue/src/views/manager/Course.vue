<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px" v-model="data.name" placeholder="请输入课程名称" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left:10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px"></div>
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </div>

    <div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="name" label="课程名称" width="180"/>
        <el-table-column prop="no" label="课程编号" width="180"/>
        <el-table-column prop="descr" label="课程描述" width="180"/>
        <el-table-column prop="times" label="课时"/>
        <el-table-column prop="teacher" label="任课老师"/>
        <el-table-column fixed="right" label="操作" min-width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination v-model:pageSize="data.pageSize" v-model:current-page="data.pageNum"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

    <el-dialog width="30%" v-model="data.formVisible" title="课程信息">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程编号" prop="no">
          <el-input v-model="data.form.no" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课程描述" >
          <el-input v-model="data.form.descr" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="课时" >
          <el-input v-model="data.form.times" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="任课老师">
          <el-input v-model="data.form.teacher" autocomplete="off"/>
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
import {reactive} from "vue";
import {Search} from "@element-plus/icons-vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  name: '',
  teacher: '',
  tableData: [],
  total: 0,
  pageNum: 1, //当前页码
  pageSize: 5,//每页个数
  formVisible: false,
  form: {}
})

const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      teacher: data.teacher,
    }
  }).then(res => {
    data.tableData = res.data?.list || []
    data.total = res.data?.total || 0
  })
}

load()

const handleCurrentChange = (pageNum) => {
  //翻页的时候重新加载数据
  load()
}
const reset = () => {
  data.name = ''
  data.teacher = ''
  load()
}
const handleAdd = () => {
  //清空数据,然后打开弹框
  data.form = {}
  data.formVisible = true

}

const save = () => {
  //   request.post("/course/add", data.form).then(res => {
  //   if (res.code === "200") {
  //     load()
  //     data.formVisible = false //关闭弹窗
  //     ElMessage.success("操作成功")
  //   } else {
  //     ElMessage.error(res.msg)
  //   }
  // })
  request.request({
    url: data.form.id ? '/course/update' : '/course/add',
    method: data.form.id ? 'PUT' : 'POST',
    data: data.form
  }).then(res => {
    if (res.code === "200") {
      load()
      data.formVisible = false //关闭弹窗
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleEdit = (row) => {
  //深拷贝
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const handleDelete = (id) => {
  ElMessageBox.confirm(
      '删除后数据无法恢复，您确定删除吗?', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/course/delete/' + id).then(res => {
      if (res.code === "200") {
        load()
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
    ElMessage({
      type: 'info',
      message: '取消删除'
    })
  })
}
</script>