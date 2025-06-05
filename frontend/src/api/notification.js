// src/api/notification.js
import axios from 'axios'

// 创建 Axios 实例：baseURL 从环境变量里读取，或默认为 "/api"
const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 5000,
})

// 1. 触发消息通知（前端一般由后端支付/下单流程自动触发，此处仅作接口示例）
export function triggerNotification(payload) {
  // payload 应当符合 NotificationRequestDTO 定义
  return client.post('/notifications', payload)
}

// 2. 查询用户通知历史
export function fetchMessageHistory(userId) {
  return client.get('/notifications/history', {
    params: { userId },
  })
}

// 如果后端提供“标记已读”接口，可以在这里添加。例如：
// export function markNotificationRead(notificationId) {
//   return client.put(`/notifications/${notificationId}/read`)
// }

export default {
  triggerNotification,
  fetchMessageHistory,
  // markNotificationRead,
}
