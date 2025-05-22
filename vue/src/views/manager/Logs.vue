<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询模块" v-model="data.operation"></el-input>
      <el-select style="margin: 0 5px" v-model="data.type">
        <el-option v-for="item in ['新增', '修改', '删除']" :key="item" :value="item" :label="item"></el-option>
      </el-select>
      <el-input style="width: 200px" placeholder="查询操作人" v-model="data.optUser"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <el-table :data="data.tableData" stripe :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="operation" label="操作模块"></el-table-column>
      <el-table-column prop="type" label="操作类型">
        <template #default="scope">
          <el-tag type="primary" v-if="scope.row.type === '新增'">{{ scope.row.type }}</el-tag>
          <el-tag type="info" v-if="scope.row.type === '修改'">{{ scope.row.type }}</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '删除'">{{ scope.row.type }}</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '批量删除'">{{ scope.row.type }}</el-tag>
          <el-tag type="success" v-if="scope.row.type === '登录'">{{ scope.row.type }}</el-tag>
          <el-tag type="success" v-if="scope.row.type === '注册'">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="ip" label="操作人IP"></el-table-column>
      <el-table-column prop="user" label="操作人"></el-table-column>
      <el-table-column prop="time" label="操作时间"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="card">
      <el-pagination v-model:pageSize="data.pageSize" v-model:current-page="data.pageNum"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

  </div>
</template>

<script setup>
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request";

const data = reactive({
  tableData: [],  // 所有的数据
  pageNum: 1,   // 当前的页码
  pageSize: 5,  // 每页显示的个数
  operation: '',
  total: 0,
  form: {},
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  ids: [],
  type: '',
  optUser: ''
})


const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('您确认批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/logs/delete/batch', {data: data.ids}).then(res => {
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

const handleSelectionChange = (rows) => {
  // 当前选中的所有的行数据
  data.ids = rows.map(v => v.id)
}

const del = (id) => {
  ElMessageBox.confirm('您确认删除吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/logs/delete/' + id).then(res => {
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

const reset = () => {
  data.operation = ''
  data.type = ''
  data.optUser = ''
  load()
}

const load = () => {  // 分页查询
  request.get('/logs/selectByPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      operation: data.operation,
      type: data.type,
      user: data.optUser,
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
  max-width: 300px !important;
}
</style>