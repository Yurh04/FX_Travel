// src/api/trainMeal.js
import axios from 'axios'

// ———————— 后端基础地址 ————————
// 请根据实际部署地址修改。如果后端同源，也可以直接写 '/api'。
const BASE_URL = 'http://localhost:8080'

/**
 * 获取某列车的餐食列表（仅启用）
 * GET /api/train_meal?trainId={trainId}
 *
 * @param {string|number} trainId   列车 ID
 * @returns {Promise<Array>} 返回餐食列表数据数组
 *
 * 示例调用（组件里）：
 *   try {
 *     const meals = await fetchTrainMealList(123)
 *     console.log('餐食列表：', meals)
 *   } catch (err) {
 *     console.error(err.message)
 *   }
 *
 * 说明：
 *  - 这里我们把 trainId 直接解析为整数，放到 params: { trainId } 中。
 *  - 不要再像 fetchTrainMealList({ params: { trainId } }) 那样多包一层，否则后端拿不到正确字段。
 */
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

/**
 * 创建餐食订单（一次提交一项）
 * POST /api/train_meal_order
 *
 * @param {Object} options
 *   {
 *     userId: string | number,            // 用户 ID
 *     ticketReservationId: string | number,// 车票预约 ID
 *     trainMealId: string | number        // 餐食 ID
 *   }
 * @returns {Promise<Object>} 返回后端下单成功后的订单信息
 *
 * 示例调用（组件里）：
 *   try {
 *     const order = await createTrainMealOrder({
 *       userId: 42,
 *       ticketReservationId: 777,
 *       trainMealId: 55
 *     })
 *     console.log('下单成功：', order)
 *   } catch (err) {
 *     console.error(err.message)
 *   }
 *
 * 说明：
 *  - POST 请求体直接传包含 userId、ticketReservationId、trainMealId 和 quantity（默认为 1） 的对象，不要再包 `{ payload: { … } }`。
 *  - 最终发出的请求 body 是：
 *      {
 *        userId: 42,
 *        ticketReservationId: 777,
 *        trainMealId: 55,
 *        quantity: 1
 *      }
 */
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
