<template>
  <div style="color:#666;font-size:14px;">
    <div style="padding-bottom:20px">
      <b>您好！</b>
    </div>
    <el-row style="margin-top:40px">
      <el-col :span="12">
        <div style="width:500px;margin:0 auto;padding:20px;box-shadow:0 5px 15px -5px rgba(0,0,0,.5);border-radius:10px;">
          <div style="text-align:center;margin-bottom:20px;font-size:24px">
            <b>全国疫情实时数据</b>
          </div>
          <div style="display:flex;flex-wrap:wrap;justify-content:flex-start;">
            <div class="item" style="background-color:lightblue">
              <div class="item_title">境外输入</div>
              <div class="item_num" style="color:RoyalBlue">{{ data.total.input ? data.total.input : '未更新' }}</div>
              <div class="item_num item_num_plus">较昨日<span style="color:RoyalBlue">+{{ data.today.input }}</span></div>
            </div>
            <div class="item" style="background-color:aliceblue">
              <div class="item_title">无症状感染者</div>
              <div class="item_num" style="color:LightCoral">{{
                  data.extData.noSymptom ? data.extData.noSymptom: '未更新'
                }}
              </div>
              <div class="item_num item_num_plus">较昨日<span
                  style=" color:LightCoral ">+{ data.extData.incrNoSymptom }}</span></div>
            </div>
            <div class="item" style="background-color:lightgoldenrodyellow">
              <div class="item_title">现有确诊</div>
              <div class="item_num" style="color:OrangeRed">
                {{ (data.total.confirm - data.total.dead - data.total.heal) ? (data.total.confirm - data.total.dead - data.total.deal) : '未更新' }}
              </div>
              <div class="item_num item_num_plus">较昨日<span
                  style="color:OrangeRed">+{{
                  data.today.confirm - data.today.dead - data.today.heal ? data.total.confirm - data.total.dead - data.total.deal: ""
                }}</span>
              </div>
            </div>
            <div class="item" style="background-color:lightgray">
              <div class="item_title">累计确诊</div>
              <div class="item_num" style="color:Brown">{{ data.total.confirm ? data.total.confirm : '未更新' }}</div>
              <div class="item_num item_num_plus">较昨日<span style="color:Brown">+{{ data.today.confirm }}</span></div>
            </div>
            <div class="item" style="background-color:lightpink">
              <div class="item_title">累计死亡</div>
              <div class="item_num" style="color:#333">{{ data.total.dead ? data.total.dead : '未更新' }}</div>
              <div class="item_num item_num_plus">较昨日<span style="color:#333">+{{ data.today.dead }}</span></div>
            </div>
            <div class="item" style="background-color:lightgreen">
              <div class="item_title">累计治愈</div>
              <div class="item_num" style="color:green">{{ data.total.heal ? data.total.heal : '未更新' }}</div>
              <div class="item_num item_num_plus">较昨日<span style="color:green">+{{ data.today.heal }}</span></div>
            </div>
          </div>
        </div>
      </el-col>

      <el-row :span="12">
        <div id="main" style="width: 100%; height:500px"></div>
      </el-row>

    </el-row>
  </div>
</template>

<script setup>
import * as echarts from 'echarts';
import '@/utils/china.js';
import {reactive} from "vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  total: {},
  today: {},
  extData: {}
})

const options = {
  //标题样式
  title: {
    text: '中国疫情地图',
    x: " center ",
    y: '0%',
    textStyle: {
      fontSize: 20,
      color: "#333"
    },
  },
  //这里设置示框（鼠标费浮效果）
  tooltip: {
    //数据项签形和发
    trigger: ' item ',
    //表示推原层的背景颜色，（鼠标悬源后的提示准背景煮色）
    backgroundColor: " white ",
    //字符串模板（地图）:{a}（条外名称），{b}（区域名称），{c}（合并数值）,{d}（无）
    formatter: '地区:{b}<br/>现有确诊病例:{c}'
  },
  //视觉映射组件
  visualMap: {
    top: 'centor',
    left: 'left',
    //数据的范围
    min: 10,
    max: 100,
    text: ['高', '低'],
    realtime: true, //拖拽时，是否实时更新
    calculable: true, //是否显示拖拽用的手柄
    inRange: {
      //颜色分布
      color: ['white', 'Tomato', 'orangered']
    }
  },
  series: [
    {
      name: '确诊',
      type: 'map',
      mapType: 'china',
      roam: true,
      //是否开启鼠标缩放和平移漫游
      itemStyle: {
        //地图区域的多边形图形样式
        normal: {
          //是图形在默认状态下的样式
          label: {
            show: true,//是否显示标签
            textStyle: {
              color: "black"
            }
          }
        },
        zoom: 1,
        //地图缩放比例，默认为1
        emphasis: {
          //是图形在高亮状态下的样式，比如在鼠标悬浮或者图例联动高亮时
          label: {show: true}
        }
      },
      top: '0%',
      left: '15%',
      data: []
    }
  ]
}


let chartDom = document.getElementById('main');
let myChart = echarts.init(chartDom);

//获取数据
request.get("/echarts/epidemic").then(res => {
  data.total = res.data.chinaTotal.total
  data.today = res.data.chinaTotal.today
  data.extData = res.data.chinaTotal.extData

  let data = [{name: '南海诸岛', value: 0}]
  res.data.areaTree[2].children.forEach(item => {
    data.push({name: item.name, value: item.total.confirm - item.total.dead - item.total.heal})

    options.series[0].data = data
    myChart.setOption(options)
  })
})
</script>