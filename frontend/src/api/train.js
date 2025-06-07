// src/api/train.js
import axios from 'axios'

const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true
})

/* ------------------------ 列车管理模块（管理员） ------------------------ */

// 添加列车
export const addTrain = (data) => request.post('/train/admin/add', data)

// 删除列车
export const deleteTrain = (trainId) => request.delete(`/train/admin/delete/${trainId}`)

// 更新列车信息
export const updateTrain = (trainId, data) => request.put(`/train/admin/update/${trainId}`, data)

// 获取所有列车
export const getAllTrains = () => request.get('/train/admin/list')

// 获取列车详情
export const getTrainDetail = (trainId) => request.get(`/train/${trainId}`)


/* ------------------------ 座位管理模块 ------------------------ */

// 添加座位
export const addSeat = (data) => request.post('/train/seat/admin/add', data)

// 删除座位
export const deleteSeat = (seatId) => request.delete(`/train/seat/admin/delete/${seatId}`)

// 更新座位信息
export const updateSeat = (seatId, data) => request.put(`/train/seat/update/${seatId}`, data)

// 获取座位详情
export const getSeatDetail = (seatId) => request.get(`/train/seat/get/${seatId}`)

// 获取列车所有座位
export const getSeatsByTrain = (trainId) => request.get(`/train/seat/list/${trainId}`)


/* ------------------------ 车票购买模块 ------------------------ */

// 按出发时间查询车次
export const searchByDepartureTime = (params) => request.post('/v1/train/search/by-departure-time', params)

// 按旅途时长查询车次
export const searchByDuration = (params) => request.get('/v1/train/search/by-travel-duration', params)

// 生成车票
export const generateTicket = (userId, trainSeatId) => request.put(`/v1/train/ticket/get/${userId}`, trainSeatId)

// 取消车票
export const cancelTicket = (ticketId) => request.put(`/v1/train/ticket/cancel/${ticketId}`)

///<script setup>
// import { ref, onMounted } from 'vue'
// import { searchByDepartureTime } from '@/api/train'
//
// const trainList = ref([])
//
// onMounted(async () => {
//   const res = await searchByDepartureTime({
//     departureStation: '北京',
//     arrivalStation: '上海',
//     departureDate: '2023-06-01'
//   })
//   trainList.value = res.data || []
// })
// </script>