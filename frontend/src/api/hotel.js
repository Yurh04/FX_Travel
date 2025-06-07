// src/api/hotel.js
import axios from 'axios'

// ———————— 创建 Axios 实例 ————————
// baseURL 从环境变量里读取，如果不存在就默认 '/api'
const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

/**
 * ======== 用户端接口 ========
 * 下面的示例调用（“示例”节）演示了前端组件里应该如何正确传参，避免多套一层大括号。
 */

/**
 * 简单搜索酒店（GET 查询参数放在 URL QueryString 上）
 * GET /api/hotels?destination=&checkInDate=&checkOutDate=
 *
 * @param {Object} options
 * @param {string} options.destination     目的地
 * @param {string} options.checkInDate      入住日期，格式 "YYYY-MM-DD"
 * @param {string} options.checkOutDate     退房日期，格式 "YYYY-MM-DD"
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // 注意：searchHotels 只接受一个对象，不要写成 searchHotels({ params: { … } })
 *   searchHotels({
 *     destination: 'Beijing',
 *     checkInDate: '2025-06-10',
 *     checkOutDate: '2025-06-12'
 *   })
 */
export function searchHotels({ destination, checkInDate, checkOutDate }) {
  return client.get('/hotels', {
    params: { destination, checkInDate, checkOutDate }
  })
}

/**
 * 高级搜索（带排序）（POST 将 filters 直接当做请求体）
 * POST /api/hotels/search
 *
 * @param {Object} filters
 *   {
 *     destination: string,
 *     checkInDate: string,    // "YYYY-MM-DD"
 *     checkOutDate: string,   // "YYYY-MM-DD"
 *     sortBy?: string,        // "price" 或 "rating"
 *     sortOrder?: string      // "asc" 或 "desc"
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   advancedSearch({
 *     destination: 'Beijing',
 *     checkInDate: '2025-06-10',
 *     checkOutDate: '2025-06-12',
 *     sortBy: 'price',
 *     sortOrder: 'asc'
 *   })
 */
export function advancedSearch(filters) {
  return client.post('/hotels/search', filters)
}

/**
 * 获取酒店详情
 * GET /api/hotels/{id}
 *
 * @param {string|number} id  酒店 ID
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   fetchHotelDetail(123)
 */
export function fetchHotelDetail(id) {
  return client.get(`/hotels/${id}`)
}

/**
 * 创建酒店预订
 * POST /api/hotel_bookings
 *
 * @param {Object} bookingData
 *   {
 *     userId: string | number,
 *     hotelId: string | number,
 *     roomTypeId: string | number,
 *     checkInDate: string,    // "YYYY-MM-DD"
 *     checkOutDate: string    // "YYYY-MM-DD"
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   createBooking({
 *     userId: 42,
 *     hotelId: 123,
 *     roomTypeId: 7,
 *     checkInDate: '2025-06-10',
 *     checkOutDate: '2025-06-12'
 *   })
 */
export function createBooking(bookingData) {
  return client.post('/hotel_bookings', bookingData)
}

/**
 * 查询用户历史预订（GET + QueryString）
 * GET /api/hotel_bookings?userId=
 *
 * @param {string|number} userId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   fetchBookings(42)
 *   // 等价于：client.get('/hotel_bookings', { params: { userId: 42 } })
 */
export function fetchBookings(userId) {
  return client.get('/hotel_bookings', {
    params: { userId }
  })
}

/**
 * 取消预订
 * DELETE /api/hotel_bookings/{bookingId}
 *
 * @param {string|number} bookingId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   cancelBooking(888)
 */
export function cancelBooking(bookingId) {
  return client.delete(`/hotel_bookings/${bookingId}`)
}


/**
 * ======== 管理端接口 ========
 */

/**
 * 获取所有酒店（可分页，可选传 page、size）
 * GET /api/hotel_admin?page=&size=
 *
 * @param {Object} [options]
 * @param {number} options.page    页码，从 1 开始（如果不传则默认不分页返回全部）
 * @param {number} options.size    每页大小（如果不传则后端使用默认值）
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // 分页
 *   fetchAllHotels({ page: 1, size: 20 })
 *
 *   // 如果不分页，直接 fetchAllHotels() 或者 fetchAllHotels({}) 都可以
 */
export function fetchAllHotels({ page, size } = {}) {
  return client.get('/hotel_admin', {
    params: { page, size }
  })
}

/**
 * 条件查询酒店（POST + 请求体 filters）
 * POST /api/hotel_admin/query
 *
 * @param {Object} filters
 *   {
 *     destination?: string,
 *     priceMin?: number,
 *     priceMax?: number,
 *     ratingMin?: number,
 *     ratingMax?: number,
 *     enabled?: boolean
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   queryHotels({
 *     destination: 'Beijing',
 *     priceMin: 100,
 *     priceMax: 500,
 *     ratingMin: 4.5,
 *     enabled: true
 *   })
 */
export function queryHotels(filters) {
  return client.post('/hotel_admin/query', filters)
}

/**
 * 添加新酒店
 * POST /api/hotel_admin
 *
 * @param {Object} hotelData
 *   {
 *     name: string,
 *     address: string,
 *     city: string,
 *     price: number,
 *     rating: number,
 *     enabled: boolean,
 *     … // … 其他字段
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   addHotel({
 *     name: 'Fancy Hotel',
 *     address: '123 Main St',
 *     city: 'Beijing',
 *     price: 299,
 *     rating: 4.8,
 *     enabled: true
 *   })
 */
export function addHotel(hotelData) {
  return client.post('/hotel_admin', hotelData)
}

/**
 * 更新酒店信息
 * PUT /api/hotel_admin
 *
 * @param {Object} hotelData
 *   {
 *     id: number,           // 后端识别要更新哪条记录
 *     name?: string,
 *     address?: string,
 *     city?: string,
 *     price?: number,
 *     rating?: number,
 *     enabled?: boolean,
 *     … // … 其他可更新字段
 *   }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   updateHotel({
 *     id: 123,
 *     price: 350,
 *     enabled: false
 *   })
 */
export function updateHotel(hotelData) {
  return client.put('/hotel_admin', hotelData)
}

/**
 * 禁用酒店
 * DELETE /api/hotel_admin/disable?hotelId=
 *
 * @param {string|number} hotelId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   disableHotel(123)
 */
export function disableHotel(hotelId) {
  return client.delete('/hotel_admin/disable', {
    params: { hotelId }
  })
}

/**
 * 启用酒店
 * PUT /api/hotel_admin/enable?hotelId=
 *
 * @param {string|number} hotelId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   enableHotel(123)
 */
export function enableHotel(hotelId) {
  // 由于 PUT 请求不需要 body，仅需在 URL 上带 hotelId
  return client.put('/hotel_admin/enable', null, {
    params: { hotelId }
  })
}

/**
 * 物理删除酒店
 * DELETE /api/hotel_admin/delete?hotelId=
 *
 * @param {string|number} hotelId
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   deleteHotel(123)
 */
export function deleteHotel(hotelId) {
  return client.delete('/hotel_admin/delete', {
    params: { hotelId }
  })
}


export default {
  searchHotels,
  advancedSearch,
  fetchHotelDetail,
  createBooking,
  fetchBookings,
  cancelBooking,
  fetchAllHotels,
  queryHotels,
  addHotel,
  updateHotel,
  disableHotel,
  enableHotel,
  deleteHotel
}
