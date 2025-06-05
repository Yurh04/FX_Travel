// src/api/trainMeal.js
import axios from 'axios'

const BASE_URL = 'http://localhost:8080' // 根据部署地址调整

// 获取某列车的餐食列表（仅启用）
export async function fetchTrainMealList(trainId) {
    try {
        const res = await axios.get(`${BASE_URL}/api/train_meal`, {
            params: { trainId: parseInt(trainId) }
        })
        return res.data
    } catch (error) {
        throw new Error('获取餐食失败: ' + (error.response?.data?.message || error.message))
    }
}

// 创建餐食订单（一次提交一项）
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
        throw new Error('提交订单失败: ' + (error.response?.data?.message || error.message))
    }
}
