import { createRouter, createWebHistory } from 'vue-router'
import TrainBooking from '../pages/TrainBooking.vue'
import OrderHistory from '../pages/OrderHistory.vue'
import TrainMeal from '../pages/TrainMeal.vue'
import HomePage from '../pages/HomePage.vue'
import LoginPage from '../pages/LoginPage.vue'

const routes = [
    { path: '/', component: HomePage },
    { path: '/booking', component: TrainBooking },
    { path: '/orders', component: OrderHistory },
    { path: '/meal', component: TrainMeal },
    { path: '/login', component: LoginPage } // ✅ 登录页
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router


