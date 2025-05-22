<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>欢迎您，<span style="color:dodgerblue;">{{ user.name }}</span> 祝您今天过得开心！</div>
    </div>

    <div style="display: flex; margin: 15px 0">
      <el-card style="width: 50%; margin-right: 10px">
        <div style="margin-bottom: 15px; font-size: 20px; font-weight: bold">系统公告</div>

        <el-collapse v-model="data.activeNames" accordion>
          <el-collapse-item v-for="(item,index) in data.notices" :key="item.id" :title="item.title" :name="index + ''">
            <el-card>
              <div>{{ item.content }}</div>
            </el-card>
          </el-collapse-item>
        </el-collapse>
      </el-card>

      <el-card style="width: 50%; margin-right: 10px">
        <div style="margin-bottom: 15px; font-size: 20px; font-weight: bold">系统公告</div>
        <el-timeline style="padding: 0">
          <el-timeline-item v-for="item in data.notices" :key="item.id" :timestamp="item.time" placement="top">
            <el-card>
              <h4>{{ item.title }}</h4>
              <p>{{ item.content}}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive} from "vue";

const user = JSON.parse(localStorage.getItem('student-user') || '{}')

const data = reactive({
  notices: [],
  activeNames: '0'
})

const loadNotice = () => {
  request.get('/notice/selectUserData').then(res => {
    data.notices = res.data
  })
}

loadNotice()

</script>
