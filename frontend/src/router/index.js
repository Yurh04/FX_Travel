import { createRouter, createWebHistory } from 'vue-router'
import TrainBooking from '../pages/TrainBooking.vue'
import OrderHistory from '../pages/OrderHistory.vue'
import TrainMeal from '../pages/TrainMeal.vue'
import TrainHome from '../pages/TrainHome.vue'
import HotelHome from '../pages/HotelHome.vue'
import HotelSearch from '../pages/HotelSearch.vue'
import HotelDetail from '../pages/HotelDetail.vue'
import BookingForm from '../pages/BookingForm.vue'
import TrainSearchResult from '../pages/TrainSearchResult.vue'
import AuthPage from '../pages/auth-page.vue'
import AboutUs from "../pages/AboutUs.vue"


const routes = [
    { path: '/', name: 'HotelHome', component: HotelHome },
    { path: '/search', name: 'HotelSearch', component: HotelSearch },
    { path: '/hotels/:id', name: 'HotelDetail', component: HotelDetail, props: true },
    { path: '/bookingHotel/:bookingId?', name: 'BookingForm', component: BookingForm, props: true },
    { path: '/train', name: 'train', component: TrainHome },
    { path: '/booking', name: 'TrainBooking', component: TrainBooking },
    { path: '/orders', name: 'OrderHistory', component: OrderHistory },
    { path: '/meal', name: 'TrainMeal', component: TrainMeal },
    { path: '/train-result', name: 'trainResult', component: TrainSearchResult, props: true },
    { path: '/auth',name:"Authentication", component: AuthPage },
    { path: '/aboutUs', name: "aboutUs", component: AboutUs, props: true},
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router;