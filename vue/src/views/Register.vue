<template>
  <div>
    <div class="login-container">
      <div style="width:350px" class="login-box">
        <div style="font-weight: bold;font-size:24px;text-align: center;margin-bottom: 30px">注册</div>
        <el-form :model="data.form" ref="formRef" :rules="rules">
          <el-form-item prop="username">
            <el-input style="width: 100%" prefix-icon="User" v-model="data.form.username" placeholder="请输入账号"/>
          </el-form-item>

          <el-form-item prop="password">
            <el-input show-password style="width: 100%" prefix-icon="Lock" v-model="data.form.password"
                      placeholder="请输入密码"/>
          </el-form-item>

          <el-form-item prop="confirmPass">
            <el-input show-password style="width: 100%" prefix-icon="Lock" v-model="data.form.confirmPass"
                      placeholder="请确认密码"/>
          </el-form-item>

          <el-form-item prop="role">
            <el-select style="width: 100%" v-model="data.form.role">
              <el-option value="ADMIN" label="管理员"></el-option>
              <el-option value="STUDENT" label="学生"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" style="width:100%" @click="register">注册</el-button>
          </el-form-item>
        </el-form>
        <div style="margin-top: 30px;text-align: right">
          已有账号？请<a href="/login">登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index";
import request from "@/utils/request";

const data = reactive({
  form: {role: 'ADMIN'},
  confirmPass: ''
})

const validatePassword = (rule, confirmPass, callback) => {
  if (confirmPass === '') {
    callback(new Error('请确认密码'))
  } else if (confirmPass !== data.form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
  confirmPass: [
    {validator: validatePassword, required: true, message: '请确认密码', trigger: 'blur'}
  ],
  role: [
    {required: true, message: '请选择角色', trigger: 'blur'},
  ]
})


const formRef = ref()

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功')
          router.push('/login') //跳转到主页
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>

<style scoped>
.login-container {
  min-height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url("@/assets/imgs/bg.png");
  background-size: cover;
}

.login-box {
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
  border-radius: 5px;
}

</style>