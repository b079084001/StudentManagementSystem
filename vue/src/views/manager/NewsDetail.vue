<template>
  <div>
    <div style="margin: 10px 0 20px 0; font-size: 30px; font-weight: bold">{{ data.news.title }}</div>
    <div style="margin: 10px 0; font-size: 13px; color: #666">{{ data.news.author }} <span
        style="margin-left: 20px">{{ data.news.time }}</span></div>
    <div style=" box-shadow: 0 0 10px rgba(0, 0, 0, .12); padding: 10px">
      <div v-html="data.news.content"></div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request";
import {useRoute} from "vue-router";

const data = reactive({
  news: {}
})

const $route = useRoute()
const load = () => {
  let id = $route.query.id
  if (!id) {
    return
  }
  request.get('/news/selectById/' + id).then(res => {
    data.news = res.data
  })
}

load()


</script>