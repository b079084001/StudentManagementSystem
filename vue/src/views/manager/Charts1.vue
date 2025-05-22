<template>
  <div>
    <el-row :gutter="10">

      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="line"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="bar"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10">

      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="pie"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>

        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import {onMounted} from "vue";

const option = {
  title: {
    text: '订单',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    // orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '金额',
      data: [150, 230, 224, 218, 135, 147, 260],
      type: 'line',
      smooth: true
    },
    {
      name: '库存',
      data: [350, 530, 524, 518, 435, 447, 660],
      type: 'line',
      smooth: true
    }
  ]
}

const option1 = {
  title: {
    text: '库存',
    subtext: '销售',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '金额',
      data: [120, 200, 150, 80, 70, 110, 130],
      type: 'bar'
    },
    {
      name: '销量',
      data: [220, 300, 250, 180, 170, 210, 230],
      type: 'bar'
    }
  ]
}

const option3 = {
  title: {
    text: 'Referer of a Website',
    subtext: 'Fake Data',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: 'Access From',
      type: 'pie',
      // center: ['50%','60%'],
      radius: '50%',
      data: [
        {value: 1048, name: 'Search Engine'},
        {value: 735, name: 'Direct'},
        {value: 580, name: 'Email'},
        {value: 484, name: 'Union Ads'},
        {value: 300, name: 'Video Ads'}
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

onMounted(() => {// 等待页面的元素全部加载完成之后再初始化
  // 折线图
  let lineDom = document.getElementById('line');
  let lineChart = echarts.init(lineDom);
  lineChart.setOption(option)

  // 柱状图
  let barDom = document.getElementById('bar');
  let barChart = echarts.init(barDom);
  barChart.setOption(option1)

  // 饼图
  let pieDom = document.getElementById('pie');
  let pieChart = echarts.init(pieDom);
  pieChart.setOption(option3)
})

</script>

<style scoped>

</style>