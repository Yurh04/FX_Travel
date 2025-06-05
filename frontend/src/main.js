// 文件：src/main.js

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 1. 引入 Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 2. 引入 Pinia
import { createPinia } from 'pinia'
// 将下面这一行的 "/store/user.js" 改为相对路径 "./store/user"
import { useUserStore } from './store/user'

const app = createApp(App)

// 安装 Element Plus
app.use(ElementPlus)

// 安装 Pinia
const pinia = createPinia()
app.use(pinia)

// 先通过 Pinia 拿到 userStore
const userStore = useUserStore()

// 将 userStore 挂载到全局，以便在任意组件里使用 this.$user
app.config.globalProperties.$user = userStore

// 应用启动时，尝试从后端拉取当前用户信息，恢复登录状态
userStore.fetchCurrentUser()

// 全局前置守卫：凡是带了 meta.requiresAuth 且未登录就跳转到 /auth
router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !userStore.isLoggedIn) {
        return next({ name: 'Authentication' })
    }
    next()
})

app.use(router)
app.mount('#app')
