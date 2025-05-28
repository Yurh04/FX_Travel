//应用入口，创建 Vue 实例并挂载到页面。
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 1. 引入 Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

// 2. 安装 Element Plus 插件
app.use(ElementPlus)

app.use(router)
app.mount('#app')

