import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import '@/assets/css/global.css'
import 'highlight.js/styles/monokai-sublime.css'

//main.js全局注册markdown
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'


const app = createApp(App)
app.config.globalProperties.$baseUrl=import.meta.env.VITE_BASE_URL

//use markdown
app.use(mavonEditor)

app.use(router)
app.use(ElementPlus, {
    locale: zhCn,
})
app.mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}