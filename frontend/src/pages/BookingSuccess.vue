<!-- src/pages/BookingSuccess.vue -->
<template>
  <div class="success-page">
    <div class="success-card">
      <!-- 顶部图标或文字 -->
      <div class="icon-wrapper">
        <span class="success-icon">✔</span>
      </div>

      <!-- 主要提示 -->
      <h2>预订成功</h2>
      <p class="desc">
        您的车票已预订并支付成功。祝您旅途愉快！
      </p>

      <!-- 底部操作按钮 -->
      <div class="button-group">
        <button class="btn-view-orders" @click="goToOrders">
          查看订单
        </button>
        <button class="btn-train-meal" @click="goToTrainMeal">
          火车订餐
        </button>
        <button class="btn-home" @click="goHome">
          回到首页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 点击“查看订单”
const goToOrders = () => {
  router.push({ name: 'OrderHistory' })
}

// 点击“火车订餐”
const goToTrainMeal = () => {
  router.push({ name: 'TrainMeal' })
}

// 点击“回到首页”
const goHome = () => {
  router.push({ name: 'HotelHome' })
}
</script>

<style scoped>
.success-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background: linear-gradient(to right, #e6f0ff, #f9fbff);
  font-family: 'Segoe UI', 'PingFang SC', 'Helvetica Neue', sans-serif;
}

.success-card {
  background: white;
  padding: 32px 40px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  width: 360px;
  text-align: center;
}

.icon-wrapper {
  font-size: 48px;
  color: #52c41a;
  margin-bottom: 16px;
}

.success-icon {
  display: inline-block;
  font-size: 48px;
}

.success-card h2 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.desc {
  font-size: 15px;
  color: #555;
  margin-bottom: 24px;
  line-height: 1.6;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.button-group .btn-view-orders,
.button-group .btn-train-meal,
.button-group .btn-home {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.btn-view-orders {
  background: linear-gradient(to right, #409eff, #66b1ff);
  color: #fff;
}
.btn-view-orders:hover {
  background: linear-gradient(to right, #3a8ee6, #5caceb);
}

.btn-train-meal {
  background: linear-gradient(to right, #ff9a00, #ffbf47);
  color: #fff;
}
.btn-train-meal:hover {
  background: linear-gradient(to right, #e08800, #d1a83c);
}

.btn-home {
  background: #f0f0f0;
  color: #333;
}
.btn-home:hover {
  background: #e0e0e0;
}
</style>
