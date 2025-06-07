// src/api/hotel.js
import axios from 'axios'

// ———————— 创建 Axios 实例 ————————
// baseURL 从环境变量里读取，如果不存在就默认 '/api'
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

export const searchHotels = (params) => request.post('/hotel/room/by-dest', params)

export const bookRoom = (params) => request.post('/hotel/room/get', params)

export const doAsync = (orderId) => request().get('/hotel', { params: orderId} )

export const getOrderRooms = (userId) => request().get('/hotel/orders', { params: userId} )
