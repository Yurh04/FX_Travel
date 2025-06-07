import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { createPinia } from 'pinia'
import {useUserStore} from "./store/user.js";

const app = createApp(App)
const pinia = createPinia()

app.use(ElementPlus)
app.use(pinia)

// 应用启动时尝试恢复登录状态
const userStore = useUserStore(pinia)
userStore.initializeFromStorage()

router.beforeEach(async (to) => {
    if (!to.meta.requiresAuth) return true

    try {
        // 从当前 pinia 实例获取 store
        const currentUserStore = useUserStore(pinia)

        if (!currentUserStore.loggedIn) {
            await currentUserStore.initializeFromStorage()
            console.log('current:' ,currentUserStore)
            if (!currentUserStore.loggedIn) {
                return {
                    name: 'Authentication',
                    query: { redirect: to.fullPath }
                }
            }
        }
        return true
    } catch (error) {
        console.error('路由守卫出错:', error)
        return false
    }
})

app.use(router)
app.mount('#app')