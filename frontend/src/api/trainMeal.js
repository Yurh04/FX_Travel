// src/api/trainMeal.js
import axios from 'axios'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true
})

// ———————— 后端基础地址 ————————
// 请根据实际部署地址修改。如果后端同源，也可以直接写 '/api'。
const BASE_URL = 'http://localhost:8080'

export const searchByTrain = (trainId) => request.get(`train/meal/${trainId}`)

export const searchTrainMealOrder = (userId) => request.get(`train/meal/orders/${userId}`)

export const searchTrainMealOrderBySeatOrder = (seatOrderId) => request.get(`train/meal/orders/by-ticket/${userId}`)

export const startPayment = (params) => request.post('/train/meal/get', params)

export const doAsync = (orderId) => request.get('/train/meal', { params: orderId} )

export const refundMeal = (params) => request.post(`train/meal/refund`, params)

// 过时方法
export async function fetchTrainMealList(trainId) {
    try {
        const res = await axios.get(`${BASE_URL}/api/train_meal`, {
            params: { trainId: parseInt(trainId) }
        })
        return res.data
    } catch (error) {
        throw new Error(
            '获取餐食失败: ' + (error.response?.data?.message || error.message)
        )
    }
}
export async function createTrainMealOrder({ userId, ticketReservationId, trainMealId }) {
    try {
        const res = await axios.post(`${BASE_URL}/api/train_meal_order`, {
            userId,
            ticketReservationId,
            trainMealId,
            quantity: 1
        })
        return res.data
    } catch (error) {
        throw new Error(
            '提交订单失败: ' + (error.response?.data?.message || error.message)
        )
    }
}
