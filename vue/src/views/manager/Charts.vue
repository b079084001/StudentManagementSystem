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

    <el-row :gutter="10" style="margin: 10px 0">
      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="pie"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="bar3"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import * as echarts from 'echarts'
import request from "@/utils/request";
import {onMounted} from "vue";

const option = {
  title: {
    text: '订单销售的趋势图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '金额',
      data: [],
      type: 'line',
      smooth: true
    },
  ]
}

const option1 = {
  title: {
    text: '订单销售的柱状图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '金额',
      data: [],
      type: 'bar',
      smooth: true
    }
  ]
}

const option2 = {
  title: {
    text: '订单销售统计',
    subtext: '比例图',
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
      name: '金额',
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

const option3 = {
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      name: '特殊',
      data: [],
      type: 'bar'
    }
  ]
}


onMounted(() => {// 等待页面的元素全部加载完成之后再初始化

  // 折线图
  let linetDom = document.getElementById('line');
  let lineChart = echarts.init(linetDom);

  // 柱状图
  let barDom = document.getElementById('bar');
  let barChart = echarts.init(barDom);

  // 饼图
  let pieDom = document.getElementById('pie');
  let pieChart = echarts.init(pieDom);

  request.get('/charts').then(res => {
    // 折线
    option.xAxis.data = res.data?.line?.map(v => v.date) || []
    option.series[0].data = res.data?.line?.map(v => v.value) || []
    lineChart.setOption(option)

    // 柱状图
    option1.xAxis.data = res.data?.bar?.map(v => v.name) || []
    option1.series[0].data = res.data?.bar?.map(v => v.value) || []
    barChart.setOption(option1)

    // 饼图
    option2.series[0].data = res.data?.bar || []
    pieChart.setOption(option2)
  })


  // 柱状图(特殊)
  let barDom3 = document.getElementById('bar3');
  let barChart3 = echarts.init(barDom3);

  request.get('/example').then(res => {
    // 柱状图(特殊)
    option3.xAxis.data = res.data.x
    option3.series[0].data = res.data.y
    barChart3.setOption(option3)
  })
})

</script>

<style scoped>

</style>