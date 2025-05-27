<template>
  <div class="order-page">
    <h2>我的历史订单</h2>

    <div v-if="orders.length === 0" class="empty">暂无订单记录</div>

    <div v-for="order in orders" :key="order.orderId" class="order-card">
      <div class="info">
        <p><strong>订单号：</strong>{{ order.orderId }}</p>
        <p><strong>车次：</strong>{{ order.trainId }}（{{ order.from }} → {{ order.to }}）</p>
        <p><strong>出发时间：</strong>{{ order.departureTime }}</p>
        <p><strong>座位类型：</strong>{{ order.seatType }}</p>
        <p><strong>状态：</strong>{{ order.status }}</p>
      </div>

      <button v-if="order.status !== '已取消'" @click="cancelOrder(order.orderId)">
        取消订单
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const orders = ref([])

const user = JSON.parse(localStorage.getItem('user') || '{}')

const getOrders = async () => {
  if (!user.username) {
    alert('请先登录')
    return
  }

  const res = await axios.get('http://localhost:8080/api/v1/train/order/history', {
    params: { userId: user.username } // 示例用用户名，实际用 userId
  })
  orders.value = res.data
}

const cancelOrder = async (orderId) => {
  const res = await axios.post('http://localhost:8080/api/v1/train/order/cancel', {
    orderId
  })
  alert(res.data.message || '取消成功')
  getOrders()
}

onMounted(() => {
  getOrders()
})
</script>

<style scoped>
.order-page {
  max-width: 800px;
  margin: auto;
  padding: 24px;
}
h2 {
  text-align: center;
  margin-bottom: 20px;
}
.order-card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  padding: 16px;
  margin-bottom: 20px;
}
.order-card .info p {
  margin: 6px 0;
}
.order-card button {
  margin-top: 10px;
  background: #f56c6c;
  color: white;
  border: none;
  padding: 8px 14px;
  border-radius: 6px;
  cursor: pointer;
}
.empty {
  color: #999;
  text-align: center;
  margin-top: 40px;
}
</style>

