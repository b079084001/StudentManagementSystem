<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询用户名" v-model="data.username"></el-input>
      <el-input style="width: 200px; margin: 0 5px" placeholder="查询姓名" v-model="data.name"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
      <el-button type="info" plain @click="exportData">批量导出</el-button>
      <el-upload
          :action="proxy.$baseUrl + '/user/import'"
          :headers="{token: data.user.token}"
          :on-success="handleImport"
          style="display: inline-block; margin-left: 10px"
          :show-file-list="false">
        <el-button type="primary" plain>批量导入</el-button>
      </el-upload>
    </div>

    <el-table :data="data.tableData" stripe :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column label="头像">
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <el-image style="width: 50px; height: 50px; border-radius: 50%" v-if="scope.row.avatar"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="角色"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          v-model:pageSize="data.pageSize"
          v-model:current-page="data.pageNum"
          @current-change="handleCurrentChange"
          background layout="prev, pager, next"
          :total="data.total"/>
    </div>

    <el-dialog title="用户信息" v-model="data.formVisible" width="30%">
      <el-form :model="data.form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="data.form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input type="textarea" v-model="data.form.address" placeholder="地址"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="data.form.role">
            <el-radio label="ADMIN">管理员</el-radio>
            <el-radio label="STUDENT">学生</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              :action="proxy.$baseUrl + '/file/upload'"
              :headers="{ token: data.user.token }"
              :file-list="data.form.avatar? [data.form.avatar] : []"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request";
import {getCurrentInstance} from "vue";

const data = reactive({
  tableData: [],  // 所有的数据
  pageNum: 1,   // 当前的页码
  pageSize: 5,  // 每页显示的个数
  username: '',
  name: '',
  total: 0,
  formVisible: false,
  form: {},
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  ids: []
})

const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
})

const handleImport = (res, file, fileList) => {
  if (res.code === '200') {
    ElMessage.success("操作成功")
    load()
  } else {
    ElMessage.error(res.msg)
  }
}

const {proxy} = getCurrentInstance()
// console.log(proxy.$baseUrl)
const exportData = () => {
  // 批量导出数据
  if (!data.ids.length) {   // 没有选择行的时候，全部导出  或者根据我的搜索条件导出
    window.open(proxy.$baseUrl + '/user/export?token=' + data.user.token + "&username="
        + data.username + "&name=" + data.name)
  } else {      // [1,2,3] => '1,2,3'
    let idStr = data.ids.join(',')
    window.open(proxy.$baseUrl + '/user/export?token=' + data.user.token + '&ids=' + idStr)
  }
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('您确认批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/user/delete/batch', {data: data.ids}).then(res => {
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
    request.delete('/user/delete/' + id).then(res => {
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
  data.form = {role: 'STUDENT'}  // 新增数据的时候清空数据
  data.formVisible = true   // 打开弹窗
}

const formRef = ref()

const save = () => {   // 保存按钮触发的逻辑  它会触发新增或者更新
  formRef.value.validate((valid) => {
    if (valid) {
      request.request({
        url: data.form.id ? '/user/update' : '/user/add',
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
  })
}

const reset = () => {
  data.name = ''
  data.username = ''
  load()
}

const load = () => {  // 分页查询
  request.get('/user/selectByPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username: data.username,
      name: data.name
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

const handleAvatarSuccess = (response, file, fileList) => {
  // 把user的头像属性换成上传的图片的链接
  data.form.avatar = response.data
}

</script>

<style scoped>

</style>