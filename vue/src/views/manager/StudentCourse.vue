<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px" v-model="data.name" placeholder="请输入课程名称" :prefix-icon="Search"/>
      <el-input style="width: 260px" v-model="data.no" placeholder="请输入课程编号" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left:10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>


    <div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="name" label="课程名称" width="180"/>
        <el-table-column prop="no" label="课程编号" width="180"/>
        <el-table-column prop="studentName" label="学生姓名" width="180"/>
        <el-table-column fixed="right" label="操作" min-width="120">
          <template #default="scope">
            <el-button type="danger" size="small" @click="delCourse(scope.row.id)">删除</el-button>
            <el-button type="primary" size="small" @click="addGrade(scope.row)" v-if="data.user.role==='ADMIN'">打分
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination v-model:pageSize="data.pageSize" v-model:current-page="data.pageNum"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

    <el-dialog width="30%" v-model="data.formVisible" title="成绩信息">
      <el-form :model="data.form" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="课程名称">
          <el-input v-model="data.gradeForm.name" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item label="分数">
          <el-input v-model="data.gradeForm.score" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="评语">
          <el-input type="textarea" v-model="data.gradeForm.comment" autocomplete="off"/>
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
  no: '',
  tableData: [],
  total: 0,
  pageNum: 1, //当前页码
  pageSize: 5,//每页个数
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  gradeForm: {},
  formVisible: false
})

const load = () => {
  let params = {
    pageNum: data.pageNum,
    pageSize: data.pageSize,
    name: data.name,
    no: data.no
  }
  if (data.user.role === 'STUDENT') {
    params.studentId = data.user.id
  }
  request.get('/studentCourse/selectPage', {
    params: params
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
  data.no = ''
  load()
}

const delCourse = (id) => {
  ElMessageBox.confirm(
      '删除后数据无法恢复，您确定删除吗?', '删除确认', {type: 'warning'}).then(res => {
    request.delete('/studentCourse/delete/' + id).then(res => {
      if (res.code === "200") {
        load()
        ElMessage.success("操作成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {
  })
}

const addGrade = (row) => {
  data.formVisible = true
  data.gradeForm.name = row.name
  data.gradeForm.courseId = row.courseId
  data.gradeForm.studentId = row.studentId
}


const save = () => {
  request.post('/grade/add', data.gradeForm).then(res => {
    if (res.code === "200") {
      data.formVisible = false //关闭弹窗
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>