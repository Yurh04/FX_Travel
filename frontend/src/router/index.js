import { createRouter, createWebHistory } from 'vue-router'
import TrainBooking from '../pages/TrainBooking.vue'
import OrderHistory from '../pages/OrderHistory.vue'
import TrainMeal from '../pages/TrainMeal.vue'
import HomePage from '../pages/HomePage.vue'
import LoginPage from '../pages/LoginPage.vue'
import HotelHome from '../pages/HotelHome.vue'
import HotelSearch from '../pages/HotelSearch.vue'
import HotelDetail from '../pages/HotelDetail.vue'
import BookingForm from '../pages/BookingForm.vue'

const routes = [
    { path: '/', name: 'HotelHome', component: HotelHome },
    // 酒店搜索结果
    { path: '/search', name: 'HotelSearch', component: HotelSearch },
    // 酒店详情
    { path: '/hotels/:id', name: 'HotelDetail', component: HotelDetail, props: true },
    // 酒店预订表单（可选 bookingId 用于编辑或查看）
    { path: '/bookingHotel/:bookingId?', name: 'BookingForm', component: BookingForm, props: true },
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