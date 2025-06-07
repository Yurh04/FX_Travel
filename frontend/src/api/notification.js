// src/api/notification.js
import axios from 'axios'

// ———————— 创建 Axios 实例 ————————
// baseURL 从环境变量里读取，如果不存在就默认 '/api'
const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 5000
})

/**
 * 1. 触发消息通知
 * POST /api/notifications
 *
 * @param {Object} payload
 *   payload 的结构需要跟后端定义的 NotificationRequestDTO 对象保持一致，
 *   比如：
 *     {
 *       userId: string | number,
 *       title: string,
 *       message: string,
 *       type?: string,         // 比如 'order'、'payment' 等
 *       metadata?: Object      // 其他扩展字段
 *     }
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   // 假设你在下单流程里，需要给用户发送一条通知
 *   triggerNotification({
 *     userId: 42,
 *     title: '订单已支付',
 *     message: '您的订单 #123 已经支付成功。',
 *     type: 'order',
 *     metadata: { orderId: 123, amount: 299.00 }
 *   })
 *
 * 注意：直接把整个 payload 对象当作第二个参数传给 client.post，
 * 不要像下面这样多套一层大括号：
 *
 *    // ❌ 错误示例，会导致后端收到 { payload: { ... } }，多了一层
 *    client.post('/notifications', { payload })
 */
export function triggerNotification(payload) {
  return client.post('/notifications', payload)
}

/**
 * 2. 查询用户通知历史
 * GET /api/notifications/history?userId=
 *
 * @param {string|number} userId  用户 ID
 * @returns {Promise} AxiosPromise
 *
 * 示例调用（组件里）：
 *   fetchMessageHistory(42)
 *
 * 这时 axios 会把 userId 自动拼到 URL 的 QueryString 上：
 *   GET /api/notifications/history?userId=42
 *
 * 注意：不要在调用方再写成 fetchMessageHistory({ params: { userId } })，
 * 那样就会导致两层大括号，后端拿不到正确的参数。
 */
export function fetchMessageHistory(userId) {
  return client.get('/notifications/history', {
    params: { userId }
  })
}

/**
 * 如果后端提供“标记已读”接口，也可以在这里添加。例如：
 *
 * // 3. 标记通知为已读
 * // PUT /api/notifications/{notificationId}/read
 * export function markNotificationRead(notificationId) {
 *   return client.put(`/notifications/${notificationId}/read`)
 * }
 *
 * 示例调用（组件里）：
 *   markNotificationRead('607d1f9b3c1a2a0015e8b6f7')
 */

export default {
  triggerNotification,
  fetchMessageHistory
  // 如果加上 markNotificationRead，就把它也放进来：
  // markNotificationRead
}
