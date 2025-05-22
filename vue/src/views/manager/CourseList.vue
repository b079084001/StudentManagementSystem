<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px" v-model="data.name" placeholder="请输入课程名称" :prefix-icon="Search"/>
      <el-input style="width: 260px" v-model="data.teacher" placeholder="请输入老师姓名" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left:10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
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
            <el-button type="primary" size="small" @click="selectCourse(scope.row)">选课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination v-model:pageSize="data.pageSize" v-model:current-page="data.pageNum"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>


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
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
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

const selectCourse = (row) => {
  request.post('/studentCourse/add', {studentId: data.user.id, name: row.name, no: row.no, courseId: row.id}).then(res => {
    if (res.code === '200') {
      ElMessage.success("操作成功")
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>