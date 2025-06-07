<!-- 文件：src/pages/TrainBooking.vue -->
<template>
  <div class="booking-page">
    <div class="booking-card">
      <h2>确认预订</h2>

      <!-- 车次信息展示 -->
      <div class="info">
        <p><strong>车次：</strong>{{ trainId }}</p>
        <p><strong>出发城市：</strong>{{ from }}</p>
        <p><strong>到达城市：</strong>{{ to }}</p>
        <p><strong>出发时间：</strong>{{ formatDateTime(departTime) }}</p>
        <p><strong>到达时间：</strong>{{ formatDateTime(arriveTime) }}</p>
        <p><strong>座席：</strong>{{ seat }}</p>
        <p><strong>票价：</strong>{{ price }} 元</p>
      </div>

      <hr />

      <!-- 支付模块 -->
      <div class="payment-section">
        <p class="total-amount">应付金额：<span>{{ price }} 元</span></p>
        <button class="pay-btn" @click="processPayment">立即支付</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 从路由 query 里拿参数
const trainId    = ref(route.query.trainId || '未知')
const from       = ref(route.query.from       || '未知')
const to         = ref(route.query.to         || '未知')
const departTime = ref(route.query.departTime || '')
const arriveTime = ref(route.query.arriveTime || '')
const seat       = ref(route.query.seat       || '无座')
const price      = ref(route.query.price      || 0)

// 支付成功后跳转
const processPayment = () => {
  ElMessage.success('支付成功！')
  setTimeout(() => {
    router.push({ name: 'BookingSuccess' })
  }, 1200)
}

// 格式化显示 ISO 时间
function formatDateTime(isoString) {
  if (!isoString) return '--:--'
  const date = new Date(isoString)
  const Y = date.getFullYear()
  const M = String(date.getMonth() + 1).padStart(2, '0')
  const D = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const m = String(date.getMinutes()).padStart(2, '0')
  return `${Y}-${M}-${D} ${h}:${m}`
}
</script>

<style scoped>
.booking-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background: linear-gradient(to right, #e6f0ff, #f9fbff);
  font-family: 'Segoe UI', 'PingFang SC', 'Helvetica Neue', sans-serif;
}

.booking-card {
  background: white;
  padding: 32px 40px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  width: 380px;
  text-align: left;
}

.booking-card h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

.info p {
  font-size: 15px;
  color: #555;
  margin: 8px 0;
  line-height: 1.6;
}

hr {
  margin: 24px 0;
  border: none;
  border-top: 1px solid #e0e0e0;
}

.payment-section {
  text-align: center;
}

.total-amount {
  font-size: 16px;
  margin-bottom: 16px;
  color: #333;
}

.total-amount span {
  font-weight: bold;
  color: #1677ff;
}

.pay-btn {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  background: linear-gradient(to right, #409eff, #66b1ff);
  border: none;
  color: white;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.pay-btn:hover {
  background: linear-gradient(to right, #3a8ee6, #5caceb);
}
</style>
