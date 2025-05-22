<template>
  <div>
    <div
        style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">学生成绩管理系统</div>
        </div>
      </div>

      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <!--        <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt=""-->
        <!--             style="width: 40px; height: 40px">-->
        <!--        <span style="margin-left: 5px">管理员</span>-->
        <el-dropdown>
          <div style="display: flex; align-items: center; cursor: default">
            <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt=""
                 style="width: 40px; height: 40px">
            <span style="margin-left: 5px">{{ data.user.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="$route.path"
            :default-openeds="['/home', '2','3']"
        >
          <el-menu-item index="/home">
            <el-icon>
              <HomeFilled/>
            </el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <Memo/>
              </el-icon>
              <span>课程管理</span>
            </template>
            <el-menu-item index="/course" v-if="data.user.role==='ADMIN'">
              <el-icon>
                <Document/>
              </el-icon>
              <span>课程信息</span>
            </el-menu-item>
            <el-menu-item index="/courseList" v-if="data.user.role==='STUDENT'">
              <el-icon>
                <Document/>
              </el-icon>
              <span>学生选课</span>
            </el-menu-item>
            <el-menu-item index="/Studentcourse">
              <el-icon>
                <Document/>
              </el-icon>
              <span>选课记录</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              <el-icon>
                <Memo/>
              </el-icon>
              <span>成绩信息</span>
            </template>
            <el-menu-item index="/grade">
              <el-icon>
                <Document/>
              </el-icon>
              <span>学生成绩</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="4" v-if="data.user.role==='ADMIN'">
            <template #title>
              <el-icon>
                <User/>
              </el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/student">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>学生信息</span>
            </el-menu-item>
            <el-menu-item index="/user">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>用户信息</span>
            </el-menu-item>
            <el-menu-item index="/news">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>新闻信息</span>
            </el-menu-item>
            <el-menu-item index="/notice">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>系统公告</span>
            </el-menu-item>
            <el-menu-item index="/orders">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>订单信息</span>
            </el-menu-item>
            <el-menu-item index="/charts">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>数据统计</span>
            </el-menu-item>
            <el-menu-item index="/charts1">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>数据统计1(原始)</span>
            </el-menu-item>
            <el-menu-item index="/echarts">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>疫情</span>
            </el-menu-item>
            <el-menu-item index="/article">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>文章</span>
            </el-menu-item>
            <el-menu-item index="/logs">
              <el-icon>
                <UserFilled/>
              </el-icon>
              <span>系统日志</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/person" v-if="data.user.role==='STUDENT'">
            <el-icon>
              <User/>
            </el-icon>
            <span>个人资料</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon>
              <SwitchButton/>
            </el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @update:user="updateUser"/>
      </div>
    </div>

  </div>
</template>

<script setup>
import {useRoute} from 'vue-router'
import router from "@/router";
import {reactive} from "vue";

const $route = useRoute()
const data = reactive({
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),

})
// const user = JSON.parse(localStorage.getItem('student-user') || '{}')
console.log($route.path)

const logout = () => {
  localStorage.removeItem('student-user')
  router.push('/login')
}

const updateUser = (user) => {
  // data.user = user
  //使用深拷贝，不然未保存就更新右上角的头像
  data.user = JSON.parse(JSON.stringify(user))
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #dcede9 !important;
}

.el-menu-item:hover {
  color: #11A983;
}

:deep(th) {
  color: #333;
}

</style>