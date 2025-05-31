<template>
  <div class="order-history">
    <h2>🧾 我的历史订单</h2>

    <div v-if="loading" class="loading">订单加载中...</div>
    <div v-else-if="ticketOrders.length === 0 && mealOrders.length === 0" class="empty">
      暂无订单记录
    </div>

    <!-- 🎫 车票订单列表 -->
    <div v-if="ticketOrders.length" class="section">
      <h3>车票订单</h3>
      <ul class="order-list">
        <li v-for="order in ticketOrders" :key="order.id" class="order-card">
          <div class="info">
            <p><strong>车次：</strong>{{ order.train.trainNumber }}</p>
            <p><strong>出发：</strong>{{ order.train.fromStation }}</p>
            <p><strong>到达：</strong>{{ order.train.toStation }}</p>
            <p><strong>座位类型：</strong>{{ order.seatType }}</p>
            <p><strong>出发时间：</strong>{{ formatTime(order.train.departureTime) }}</p>
          </div>
          <button class="cancel-btn" @click="cancelTicket(order.id)" :disabled="cancelingId === order.id">
            {{ cancelingId === order.id ? '取消中...' : '取消订单' }}
          </button>
        </li>
      </ul>
    </div>

    <!-- 🍱 餐食订单（如有） -->
    <div v-if="mealOrders.length" class="section">
      <h3>订餐订单</h3>
      <ul class="order-list">
        <li v-for="meal in mealOrders" :key="meal.id" class="order-card">
          <div class="info">
            <p><strong>订单号：</strong>{{ meal.id }}</p>
            <p><strong>车次：</strong>{{ meal.trainNumber }}</p>
            <p><strong>餐品：</strong>{{ meal.items.join(', ') }}</p>
            <p><strong>金额：</strong>￥{{ meal.total }}</p>
          </div>
          <button class="cancel-btn" @click="cancelMeal(meal.id)" :disabled="cancelingId === meal.id">
            {{ cancelingId === meal.id ? '取消中...' : '取消订单' }}
          </button>
        </li>
      </ul>
    </div>

    <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const ticketOrders = ref([])
const mealOrders = ref([])
const loading = ref(true)
const cancelingId = ref(null)
const errorMsg = ref('')

// ✅ 时间格式化
const formatTime = (datetime) => {
  return new Date(datetime).toLocaleString()
}

// ✅ 获取所有订单
const fetchOrders = async () => {
  loading.value = true
  try {
    // 🎫 车票订单接口（后端需支持）
    const res1 = await axios.get('/api/user/orders', { withCredentials: true })
    ticketOrders.value = res1.data || []

    // 🍱 订餐订单接口（可选）
    const res2 = await axios.get('/api/user/meal-orders', { withCredentials: true })
    mealOrders.value = res2.data || []
  } catch (err) {
    errorMsg.value = '加载订单失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

// ✅ 取消车票
const cancelTicket = async (id) => {
  if (!confirm('确认取消该车票订单？')) return
  cancelingId.value = id
  try {
    await axios.put(`/api/v1/train/ticket/cancel/${id}`, {}, { withCredentials: true })
    ticketOrders.value = ticketOrders.value.filter(o => o.id !== id)
  } catch {
    errorMsg.value = '取消失败，请重试'
  } finally {
    cancelingId.value = null
  }
}

// ✅ 取消订餐
const cancelMeal = async (id) => {
  if (!confirm('确认取消该订餐订单？')) return
  cancelingId.value = id
  try {
    await axios.put(`/api/meal/order/cancel/${id}`, {}, { withCredentials: true })
    mealOrders.value = mealOrders.value.filter(o => o.id !== id)
  } catch {
    errorMsg.value = '取消失败，请重试'
  } finally {
    cancelingId.value = null
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order-history {
  max-width: 780px;
  margin: 40px auto;
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  font-family: Arial;
}
h2 {
  font-size: 24px;
  margin-bottom: 20px;
}
h3 {
  font-size: 18px;
  margin: 16px 0 10px;
  color: #444;
}
.loading, .empty {
  text-align: center;
  font-size: 15px;
  color: #888;
}
.section {
  margin-bottom: 28px;
}
.order-list {
  list-style: none;
  padding: 0;
}
.order-card {
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.info p {
  margin: 4px 0;
}
.cancel-btn {
  background: #f56c6c;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
}
.cancel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.error {
  color: red;
  text-align: center;
  margin-top: 12px;
}
</style>
