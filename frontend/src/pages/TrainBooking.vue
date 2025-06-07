<!-- src/pages/TrainBooking.vue -->
<template>
  <div class="booking-page">
    <div class="booking-card">
      <h2>确认预订</h2>

      <!-- 车次信息展示 -->
      <div class="info">
        <p><strong>订单编号：</strong>{{ orderNumber }}</p>
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
        <el-button
            type="primary"
            :loading="loading"
            @click="processPayment"
        >立即支付</el-button
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { complete } from '../api/pay'   // ← 改用 complete

const route = useRoute()
const router = useRouter()

// 从路由 query 里拿参数
const trainId     = ref(route.query.trainId     || '未知')
const from        = ref(route.query.from        || '未知')
const to          = ref(route.query.to          || '未知')
const departTime  = ref(route.query.departTime  || '')
const arriveTime  = ref(route.query.arriveTime  || '')
const seat        = ref(route.query.seat        || '无座')
const price       = ref(route.query.price       || 0)
const orderNumber = ref(route.query.orderNumber || '')

const loading = ref(false)

async function processPayment() {
  loading.value = true
  try {
    // 发起 complete 请求
    const { data } = await complete({ orderNumber: orderNumber.value })
    // 假设后端返回 { message: '支付成功', ... }
    if (data.message) {
      ElMessage.success(data.message)
    } else {
      ElMessage.success('支付完成')
    }
    // 跳转到支付成功页
    router.push({
      name: 'BookingSuccess',
      query: { orderNumber: orderNumber.value }
    })
  } catch (err) {
    console.error('complete 异常：', err)
    const msg = err.response?.data?.message || '支付失败，请稍后重试'
    ElMessage.error(msg)
  } finally {
    loading.value = false
  }
}

function formatDateTime(isoString) {
  if (!isoString) return '--:--'
  const d = new Date(isoString)
  const Y = d.getFullYear()
  const M = String(d.getMonth() + 1).padStart(2, '0')
  const D = String(d.getDate()).padStart(2, '0')
  const h = String(d.getHours()).padStart(2, '0')
  const m = String(d.getMinutes()).padStart(2, '0')
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
}

.booking-card {
  background: white;
  padding: 32px 40px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  width: 380px;
}

.booking-card h2 {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 20px;
}

.info p {
  font-size: 15px;
  margin: 6px 0;
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
}

.total-amount span {
  font-weight: bold;
  color: #1677ff;
}
</style>
