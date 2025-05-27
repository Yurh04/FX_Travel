import { createRouter, createWebHistory } from 'vue-router'
import TrainBooking from '../pages/TrainBooking.vue'
import OrderHistory from '../pages/OrderHistory.vue'
import TrainMeal from '../pages/TrainMeal.vue'
import HomePage from '../pages/HomePage.vue'
import LoginPage from '../pages/LoginPage.vue'
import HotelHome from '../pages/HotelHome.vue'

const routes = [
    { path: '/', name: 'HotelHome', component: HotelHome },
    { path: '/homepage', name: 'HomePage', component: HomePage },
    { path: '/booking', name: 'TrainBooking', component: TrainBooking },
    { path: '/orders', name: 'OrderHistory', component: OrderHistory },
    { path: '/meal', name: 'TrainMeal', component: TrainMeal },
    { path: '/login', name: 'LoginPage', component: LoginPage }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router