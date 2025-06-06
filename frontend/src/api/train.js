// src/api/train.js
import axios from 'axios'

// ———————— 创建 Axios 实例 ————————
const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true
})

/* ======================== 列车管理模块（管理员） ======================== */

/**
 * 添加列车
 * POST /api/train/admin/add
 *
 * @param {Object} data
 *   {
 *     trainNumber: string,
 *     departureStation: string,
 *     arrivalStation: string,
 *     departureTime: string, // e.g. "08:00"
 *     arrivalTime: string,   // e.g. "12:00"
 *     duration: number,      // 行驶时长，单位分钟
 *     price: number,         // 票价
 *     // … 其他列车属性
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：addTrain({ data: { trainNumber: 'G101', … } })
 *
 *   // ✅ 正确示例：直接把要传的字段扁平地传进去
 *   addTrain({
 *     trainNumber: 'G101',
 *     departureStation: 'Beijing',
 *     arrivalStation: 'Shanghai',
 *     departureTime: '08:00',
 *     arrivalTime: '12:00',
 *     duration: 240,
 *     price: 499
 *   })
 */
export const addTrain = (data) => {
    return request.post('/train/admin/add', data)
}

/**
 * 删除列车
 * DELETE /api/train/admin/delete/{trainId}
 *
 * @param {string|number} trainId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   // 直接传一个 trainId
 *   deleteTrain(123)
 */
export const deleteTrain = (trainId) => {
    return request.delete(`/train/admin/delete/${trainId}`)
}

/**
 * 更新列车信息
 * PUT /api/train/admin/update/{trainId}
 *
 * @param {string|number} trainId
 * @param {Object} data
 *   {
 *     departureTime?: string,
 *     arrivalTime?: string,
 *     duration?: number,
 *     price?: number,
 *     // … 其他可更新字段
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：updateTrain(123, { data: { price: 550 } })
 *
 *   // ✅ 正确示例
 *   updateTrain(123, {
 *     price: 550,
 *     duration: 260
 *   })
 */
export const updateTrain = (trainId, data) => {
    return request.put(`/train/admin/update/${trainId}`, data)
}

/**
 * 获取所有列车
 * GET /api/train/admin/list
 *
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   getAllTrains()
 */
export const getAllTrains = () => {
    return request.get('/train/admin/list')
}

/**
 * 获取列车详情
 * GET /api/train/{trainId}
 *
 * @param {string|number} trainId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   getTrainDetail(123)
 */
export const getTrainDetail = (trainId) => {
    return request.get(`/train/${trainId}`)
}


/* ======================== 座位管理模块 ======================== */

/**
 * 添加座位
 * POST /api/train/seat/admin/add
 *
 * @param {Object} data
 *   {
 *     trainId: string | number,
 *     seatNumber: string,   // 车厢号-座位号，例如 "1A"
 *     classType: string,    // "FirstClass" / "SecondClass" 等
 *     price: number,        // 座位票价
 *     // … 其他字段
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：addSeat({ data: { trainId: 123, seatNumber: '1A', … } })
 *
 *   // ✅ 正确示例
 *   addSeat({
 *     trainId: 123,
 *     seatNumber: '1A',
 *     classType: 'FirstClass',
 *     price: 150
 *   })
 */
export const addSeat = (data) => {
    return request.post('/train/seat/admin/add', data)
}

/**
 * 删除座位
 * DELETE /api/train/seat/admin/delete/{seatId}
 *
 * @param {string|number} seatId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   deleteSeat(456)
 */
export const deleteSeat = (seatId) => {
    return request.delete(`/train/seat/admin/delete/${seatId}`)
}

/**
 * 更新座位信息
 * PUT /api/train/seat/update/{seatId}
 *
 * @param {string|number} seatId
 * @param {Object} data
 *   {
 *     classType?: string,
 *     price?: number,
 *     // … 其他可更新字段
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   // ❌ 错误示例：updateSeat(456, { data: { price: 180 } })
 *
 *   // ✅ 正确示例
 *   updateSeat(456, {
 *     price: 180
 *   })
 */
export const updateSeat = (seatId, data) => {
    return request.put(`/train/seat/update/${seatId}`, data)
}

/**
 * 获取座位详情
 * GET /api/train/seat/get/{seatId}
 *
 * @param {string|number} seatId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   getSeatDetail(456)
 */
export const getSeatDetail = (seatId) => {
    return request.get(`/train/seat/get/${seatId}`)
}

/**
 * 获取某列车的所有座位
 * GET /api/train/seat/list/{trainId}
 *
 * @param {string|number} trainId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   getSeatsByTrain(123)
 */
export const getSeatsByTrain = (trainId) => {
    return request.get(`/train/seat/list/${trainId}`)
}


/* ======================== 车票购买模块 ======================== */

/**
 * 按出发时间查询车次
 * GET /api/v1/train/search/by-departure-time?departureStation=&arrivalStation=&departureDate=
 *
 * @param {Object} params
 *   {
 *     departureStation: string,
 *     arrivalStation: string,
 *     departureDate: string   // 格式 "YYYY-MM-DD"
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 注意：这里的参数必须写在 params 对象里，而调用时不能再外面包一层。
 *
 * 示例调用（组件里）：
 *   // ❌ 错误示例：searchByDepartureTime({ params: { departureStation: '北京', ... } })
 *
 *   // ✅ 正确示例：直接把 “扁平” 对象传进去
 *   searchByDepartureTime({
 *     departureStation: '北京',
 *     arrivalStation: '上海',
 *     departureDate: '2023-06-01'
 *   })
 *
 * 实际底层会发送：
 *   GET /api/v1/train/search/by-departure-time?departureStation=北京&arrivalStation=上海&departureDate=2023-06-01
 */
export const searchByDepartureTime = (params) => {
    return request.get('/v1/train/search/by-departure-time', { params })
}

/**
 * 按旅途时长查询车次
 * GET /api/v1/train/search/by-travel-duration?duration=&departureStation=&arrivalStation=
 *
 * @param {Object} params
 *   {
 *     duration: number,       // 旅途时长，单位分钟
 *     departureStation: string,
 *     arrivalStation: string
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   // ❌ 错误示例：searchByDuration({ params: { duration: 240, … } })
 *
 *   // ✅ 正确示例
 *   searchByDuration({
 *     duration: 240,
 *     departureStation: 'Beijing',
 *     arrivalStation: 'Shanghai'
 *   })
 */
export const searchByDuration = (params) => {
    return request.get('/v1/train/search/by-travel-duration', { params })
}

/**
 * 生成车票
 * PUT /api/v1/train/ticket/get/{userId}
 *
 * @param {string|number} userId
 * @param {string|number} trainSeatId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   // ❌ 错误示例：generateTicket(42, { data: 987 })
 *
 *   // ✅ 正确示例：把 trainSeatId 放在请求体里，userId 放在 URL 中
 *   generateTicket(42, 987)
 *
 *   // 底层实际发出：
 *   // PUT /api/v1/train/ticket/get/42
 *   // 请求体 { trainSeatId: 987 }
 */
export const generateTicket = (userId, trainSeatId) => {
    return request.put(`/v1/train/ticket/get/${userId}`, { trainSeatId })
}

/**
 * 取消车票
 * PUT /api/v1/train/ticket/cancel/{ticketId}
 *
 * @param {string|number} ticketId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用：
 *   cancelTicket(555)
 *
 *   // 底层实际发出：
 *   // PUT /api/v1/train/ticket/cancel/555
 */
export const cancelTicket = (ticketId) => {
    return request.put(`/v1/train/ticket/cancel/${ticketId}`)
}


// ———————— 导出所有方法 ————————
export default {
    addTrain,
    deleteTrain,
    updateTrain,
    getAllTrains,
    getTrainDetail,

    addSeat,
    deleteSeat,
    updateSeat,
    getSeatDetail,
    getSeatsByTrain,

    searchByDepartureTime,
    searchByDuration,
    generateTicket,
    cancelTicket
}
