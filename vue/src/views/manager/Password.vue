<template>
  <div>
    <el-card style="width: 50%">
      <el-form ref="formRef" :model="data.form" :rules="rules" label-width="80px" style="padding-right: 20px">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="data.form.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="data.form.newPassword" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="data.form.confirmPassword" placeholder="请输入确认密码"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>

</template>

<script setup>
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";
import {reactive, ref} from "vue";

// 验证码校验
const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认密码'))
  } else if (value !== data.form.newPassword) {
    callback(new Error('确认密码错误'))
  } else {
    callback()
  }
}

const data = reactive({
  form: JSON.parse(localStorage.getItem('student-user') || "{}")
})

const rules = reactive({
  password: [
    {required: true, message: '请输入原始密码', trigger: 'blur'},
  ],
  newPassword: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
  ],
  confirmPassword: [
    {validator: validatePassword, required: true, message: '请确认新密码', trigger: 'blur'},
  ],
})

const formRef = ref()

const update = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.password = data.form.newPassword  //不加不更新数据库
      request.put('/student/update', data.form).then(res => {
        if (res.code === "200") {
          ElMessage.success("操作成功")
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>

</style>