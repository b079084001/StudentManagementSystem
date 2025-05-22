<template>
  <div style="height: 100vh; display: flex; align-items: center; justify-content: center; background-color: #0f9876">

    <div style="display: flex; background-color: white; width: 50%; border-radius: 5px; overflow: hidden">

      <div style="flex: 1">
        <img src="@/assets/login.png" alt="" style="width: 100%">
      </div>

      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="data.form" style="width: 80%" ref="formRef" :rules="rules">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px">
            欢迎登录后台管理系统
          </div>

          <el-form-item prop="username">
            <el-input style="width: 100%" prefix-icon="User" v-model="data.form.username" placeholder="请输入账号"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password style="width: 100%" prefix-icon="Lock" v-model="data.form.password"
                      placeholder="请输入密码"/>
          </el-form-item>

          <el-form-item prop="role">
            <el-select style="width: 100%" v-model="data.form.role">
              <el-option value="ADMIN" label="管理员"></el-option>
              <el-option value="STUDENT" label="学生"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item prop="code">
            <div style="display: flex">
              <el-input placeholder="请输入验证码" prefix-icon="CircleCheck" size="medium" style="flex: 1"
                        v-model="data.form.code"></el-input>
              <div style="flex: 1; height: 36px">
                <valid-code @update:value="getCode"/>
              </div>
            </div>
          </el-form-item>


          <el-form-item>
            <el-button type="primary" style="width:100%" @click="login">登 录</el-button>
          </el-form-item>

          <div style="display: flex">
            <div style="flex: 1">还没有账号？请 <span style="color: #0f9876; cursor: pointer"
                                                     @click="$router.push('/register')">注册</span></div>
            <div style="flex: 1; text-align: right"><span style="color: #0f9876; cursor: pointer"
                                                          @click="handleForgetPass">忘记密码</span></div>
          </div>

        </el-form>
      </div>
    </div>

    <el-dialog title="忘记密码" v-model="data.forgetPassDialogVis" width="30%">
      <el-form :model="data.forgetUserForm" label-width="80px" style="padding-right: 20px">
        <el-form-item label="用户名">
          <el-input v-model="data.forgetUserForm.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="data.forgetUserForm.phone" autocomplete="off" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.forgetPassDialogVis = false">取 消</el-button>
          <el-button type="primary" @click="resetPassword">确 定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>


<script setup>
import {ref, reactive} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index";
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode.vue"


const data = reactive({
  form: {role: 'ADMIN'},
  code: '', //验证码
  forgetUserForm: {},   // 忘记密码的表单数据
  forgetPassDialogVis: false,
})

// 验证码校验
const validateCode = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入验证码'))
  } else if (value.toLowerCase() !== data.code) {
    callback(new Error('验证码错误'))
  } else {
    callback()
  }
}

const getCode = (code) => {
  data.code = code.toLowerCase()
}

const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
  ],
  code: [
    {validator: validateCode, required: true, message: '请输入验证码', trigger: 'blur'}
  ],
})


const formRef = ref()

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          localStorage.setItem('student-user', JSON.stringify(res.data))
          router.push('/home') //跳转到主页
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

//初始化表单
const handleForgetPass = () => {
  data.forgetUserForm = {}
  data.forgetPassDialogVis = true
}

const resetPassword = () => {
  request.put('/password', data.forgetUserForm).then(res => {
    if (res.code === '200') {
      ElMessage.success('重置成功')
      data.forgetPassDialogVis = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}



</script>

<style scoped>


</style>