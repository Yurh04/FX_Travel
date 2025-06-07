// src/main.js
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// UI 库
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 状态管理
import { createPinia } from 'pinia'

// 全局 Store 管理器
import { initGlobalStore } from './utils/globalStore'

const app = createApp(App)

// 安装 Element Plus
app.use(ElementPlus)

// 安装 Pinia
const pinia = createPinia()
app.use(pinia)

// 初始化全局 Store
const { userStore } = initGlobalStore(app)

// 应用启动时尝试恢复登录状态
userStore.fetchCurrentUser().catch(() => {
    console.log('未检测到有效登录状态')
})

// 路由守卫配置
router.beforeEach(async (to) => {
    // 公共页面直接放行
    if (!to.meta.requiresAuth) return true

    try {
        // 获取最新 Store 实例
        const currentStore = initGlobalStore().userStore

        // 如果未登录，尝试获取最新状态
        if (!currentStore.isLoggedIn) {
            await currentStore.fetchCurrentUser()
        }

        // 仍然未登录则跳转到登录页
        if (!currentStore.isLoggedIn) {
            return {
                name: 'Authentication',
                query: { redirect: to.fullPath } // 保留目标路由
            }
        }

        // 这里可以添加权限检查逻辑
        // if (to.meta.roles && !to.meta.roles.includes(currentStore.role)) {
        //   return { name: 'Forbidden' }
        // }
    } catch (error) {
        console.error('路由守卫出错:', error)
        return false // 阻止导航
    }
})

app.use(router)
app.mount('#app')