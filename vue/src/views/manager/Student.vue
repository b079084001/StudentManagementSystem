<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 260px" v-model="data.username" placeholder="请输入账号查询" :prefix-icon="Search"/>
      <el-input style="width: 260px" v-model="data.name" placeholder="请输入姓名查询" :prefix-icon="Search"/>
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
        <el-table-column prop="username" label="学生账号" width="180"/>
        <el-table-column prop="name" label="姓名" width="180"/>
        <el-table-column label="手机" prop="phone" width="180"/>
        <el-table-column label="邮箱" prop="email"/>
        <el-table-column label="性别" prop="sex"/>
        <el-table-column label="生日" prop="birth"/>
        <el-table-column label="头像" prop="avatar">
          <template #default="scope">
            <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="scope.row.avatar"
                      style="width:20px;height:20px;border-radius: 5px"/>
          </template>
        </el-table-column>
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

    <el-dialog width="30%" v-model="data.formVisible" title="学生信息" destory-on-close>
      <el-form :model="data.form" ref="formRef" :rules="rules" label-width="100px" label-position="right"
               style="padding-right: 40px">
        <el-form-item label="学生账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input show-password v-model="data.form.password" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="data.form.phone" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="data.form.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker style="width: 100%" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
                          v-model="data.form.birth"></el-date-picker>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload action="http://localhost:9090/files/upload" list-type="picture"
                     :on-success="handleImgUploadSuccess">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
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
import {ref, reactive} from "vue";
import {Search} from "@element-plus/icons-vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  username: '',
  name: '',
  tableData: [],
  total: 0,
  pageNum: 1, //当前页码
  pageSize: 5,//每页个数
  formVisible: false,
  form: {}
})

const load = () => {
  request.get('/student/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username,
      name: data.name,
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
  data.username = ''
  data.name = ''
  load()
}
const handleAdd = () => {
  //清空数据,然后打开弹框
  data.form = {}
  data.formVisible = true

}

const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
})

const formRef = ref()

const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.request({
        url: data.form.id ? '/student/update' : '/student/add',
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
    request.delete('/student/delete/' + id).then(res => {
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


const handleImgUploadSuccess = (res) => {
  data.form.avatar = res.data
}
</script>