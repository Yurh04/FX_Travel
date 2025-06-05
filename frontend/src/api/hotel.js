// src/api/hotel.js
import axios from 'axios'

// 创建 Axios 实例，可根据环境变量配置 baseURL
const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// --------- 用户端接口 ---------

/**
 * 简单搜索酒店
 * GET /api/hotels?destination=&checkInDate=&checkOutDate=
 */
export function searchHotels({ destination, checkInDate, checkOutDate }) {
  return client.get('/hotels', {
    params: { destination, checkInDate, checkOutDate }
  })
}

/**
 * 高级搜索（带排序）
 * POST /api/hotels/search
 *
 * @param filters 形如：
 *   {
 *     destination: string,
 *     checkInDate: string,    // "YYYY-MM-DD"
 *     checkOutDate: string,   // "YYYY-MM-DD"
 *     sortBy?,                // "price" 或 "rating"
 *     sortOrder?              // "asc" 或 "desc"
 *   }
 */
export function advancedSearch(filters) {
  return client.post('/hotels/search', filters)
}

/**
 * 获取酒店详情
 * GET /api/hotels/{id}
 */
export function fetchHotelDetail(id) {
  return client.get(`/hotels/${id}`)
}

/**
 * 创建酒店预订
 * POST /api/hotel_bookings
 */
export function createBooking(bookingData) {
  // bookingData: { userId, hotelId, roomTypeId, checkInDate, checkOutDate }
  return client.post('/hotel_bookings', bookingData)
}

/**
 * 查询用户历史预订
 * GET /api/hotel_bookings?userId=
 */
export function fetchBookings(userId) {
  return client.get('/hotel_bookings', {
    params: { userId }
  })
}

/**
 * 取消预订
 * DELETE /api/hotel_bookings/{bookingId}
 */
export function cancelBooking(bookingId) {
  return client.delete(`/hotel_bookings/${bookingId}`)
}

// --------- 管理端接口 ---------

/**
 * 获取所有酒店（分页可选）
 * GET /api/hotel_admin?page=&size=
 */
export function fetchAllHotels({ page, size } = {}) {
  return client.get('/hotel_admin', {
    params: { page, size }
  })
}

/**
 * 条件查询酒店
 * POST /api/hotel_admin/query
 */
export function queryHotels(filters) {
  // filters: { destination?, priceMin?, priceMax?, ratingMin?, ratingMax?, enabled? }
  return client.post('/hotel_admin/query', filters)
}

/**
 * 添加新酒店
 * POST /api/hotel_admin
 */
export function addHotel(hotelData) {
  return client.post('/hotel_admin', hotelData)
}

/**
 * 更新酒店信息
 * PUT /api/hotel_admin
 */
export function updateHotel(hotelData) {
  return client.put('/hotel_admin', hotelData)
}

/**
 * 禁用酒店
 * DELETE /api/hotel_admin/disable?hotelId=
 */
export function disableHotel(hotelId) {
  return client.delete('/hotel_admin/disable', { params: { hotelId } })
}

/**
 * 启用酒店
 * PUT /api/hotel_admin/enable?hotelId=
 */
export function enableHotel(hotelId) {
  return client.put('/hotel_admin/enable', null, { params: { hotelId } })
}

/**
 * 物理删除酒店
 * DELETE /api/hotel_admin/delete?hotelId=
 */
export function deleteHotel(hotelId) {
  return client.delete('/hotel_admin/delete', { params: { hotelId } })
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
