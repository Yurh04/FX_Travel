<template>
  <div class="booking-page">
    <div class="booking-card">
      <h2>确认预订</h2>
      <div class="info">
        <p><strong>车次：</strong>{{ trainId }}</p>
        <p><strong>出发城市：</strong>{{ from }}</p>
        <p><strong>到达城市：</strong>{{ to }}</p>
        <p><strong>出发时间：</strong>{{ departTime }}</p>
        <p><strong>到达时间：</strong>{{ arriveTime }}</p>
        <p><strong>座席：</strong>{{ seat }}</p>
      </div>
      <button class="confirm-btn" @click="confirmBooking">确认预订</button>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const trainId = route.query.trainId || '未知'
const from = route.query.from || '未知'
const to = route.query.to || '未知'
const departTime = route.query.departTime || '--:--'
const arriveTime = route.query.arriveTime || '--:--'
const seat = route.query.seat || '无座'

const confirmBooking = () => {
  ElMessage.success('预订成功！')
  setTimeout(() => router.push('/'), 1500)
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
  width: 360px;
  text-align: left;
}

.booking-card h2 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
}

.info p {
  font-size: 15px;
  color: #555;
  margin: 8px 0;
  line-height: 1.6;
}

.confirm-btn {
  margin-top: 24px;
  width: 100%;
  padding: 12px 20px;
  font-size: 16px;
  background: linear-gradient(to right, #409eff, #66b1ff);
  border: none;
  color: white;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.confirm-btn:hover {
  background: linear-gradient(to right, #3a8ee6, #5caceb);
}
</style>
