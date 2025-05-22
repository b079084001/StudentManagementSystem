<template>
  <div>
    <div class="card" style="margin-bottom: 10px">
      <el-input style="width: 200px" v-model="data.title" placeholder="查询标题" :prefix-icon="Search"/>
      <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div>
      <el-table :data="data.tableData" stripe :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="description" label="简介"></el-table-column>
        <!--        <el-table-column prop="content" label="内容" show-overflow-tooltip>-->
        <el-table-column prop="content" label="内容">
          <template #default="scope">
            <el-button @click="showContent(scope.row.content)" size="mini">显示内容</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="详情页">
          <template #default="scope">
            <el-button @click="$router.push('/newsDetail?id=' + scope.row.id)" size="mini">在详情页显示</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="发布人"></el-table-column>
        <el-table-column prop="time" label="发布时间"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template #default="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <el-pagination v-model:pageSize="data.pageSize" v-model:current-page="data.pageNum"
                     @current-change="handleCurrentChange"
                     background layout="prev, pager, next" :total="data.total"/>
    </div>

    <el-dialog title="新闻信息" v-model="data.formVisible" width="60%" @close="closeDialog"
               :close-on-click-modal="false">
      <el-form :model="data.form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="data.form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" v-model="data.form.description" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <!--          <el-input type="textarea" v-model="data.form.content" placeholder="请输入内容"></el-input>-->
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>


    <el-dialog title="内容" v-model="data.formVisible1" width="60%">
      <el-card class="w-e-text">
        <div v-html="data.content"></div>
      </el-card>
      <div class="dialog-footer">
        <el-button type="primary" @click="data.formVisible1 = false">确 定</el-button>
      </div>
    </el-dialog>

  </div>

</template>


<script setup>
import {reactive, ref, nextTick, getCurrentInstance} from "vue";
import {Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request";
import E from "wangeditor";
import hljs from 'highlight.js';


const data = reactive({
  tableData: [],  // 所有的数据
  pageNum: 1,   // 当前的页码
  pageSize: 5,  // 每页显示的个数
  total: 0,
  formVisible: false,
  form: {},
  user: JSON.parse(localStorage.getItem('student-user') || '{}'),
  ids: [],
  title: '',
  editor: null,
  content: '',
  formVisible1: false
})

const rules = reactive({
  title: [
    {required: true, message: '请输入标题', trigger: 'blur'},
  ],
})


const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('您确认批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/news/delete/batch', {data: data.ids}).then(res => {
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

const closeDialog = () => {
  // 销毁编辑器
  data.editor.destroy()
  data.editor = null
}

const showContent = (content) => {
  data.content = content
  data.formVisible1 = true
}

const handleSelectionChange = (rows) => {   // 当前选中的所有的行数据
  data.ids = rows.map(v => v.id)
}

const del = (id) => {
  ElMessageBox.confirm('您确认删除吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/news/delete/' + id).then(res => {
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

const {proxy} = getCurrentInstance()
// console.log(proxy.$baseUrl)

const setRichText = () => {
  nextTick(() => {
    data.editor = new E("#editor")
    data.editor.highlight = hljs

    data.editor.config.uploadImgServer = proxy.$baseUrl + '/files/editor/upload'
    data.editor.config.uploadFileName = 'file'
    data.editor.config.uploadImgHeaders = {
      token: data.user.token
    }
    data.editor.config.uploadImgParams = {
      type: "img",
    }

    data.editor.config.uploadVideoServer = proxy.$baseUrl + '/files/editor/upload'
    data.editor.config.uploadVideoName = 'file'
    data.editor.config.uploadVideoHeaders = {
      token: data.user.token
    }
    data.editor.config.uploadVideoParams = {
      type: "video",
    }

    data.editor.create()
  })
}

const handleEdit = (row) => {   // 编辑数据
  data.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
  data.formVisible = true   // 打开弹窗

  setRichText()

  //原来放在nextTick里面，单独提出来需要延迟加载
  setTimeout(()=>{
    data.editor.txt.html(row.content)
  },0)

}

const handleAdd = () => {   // 新增数据
  data.form = {}  // 新增数据的时候清空数据
  data.formVisible = true   // 打开弹窗

  setRichText()

}


const formRef = ref()
const save = () => {   // 保存按钮触发的逻辑  它会触发新增或者更新
  formRef.value.validate((valid) => {
    if (valid) {
      //获取编辑框的内容
      let content = data.editor.txt.html()
      data.form.content = content

      sendSaveRequest()
    }
  })
}

const sendSaveRequest = () => {
  request.request({
    url: data.form.id ? '/news/update' : '/news/add',
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

const reset = () => {
  data.title = ''
  load()
}


const load = () => {
  // 分页查询
  request.get('/news/selectByPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
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
</script>

<style>
.el-tooltip__popper {
  max-width: 200px !important;
}
</style>
