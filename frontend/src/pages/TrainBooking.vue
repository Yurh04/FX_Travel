// src/pages/TrainHomePage.vue
<template>
  <div class="train-home-page">
    <div class="hero">
      <h1>欢迎使用火车票预订系统</h1>
      <p>快速、便捷、安全地预订您的火车票</p>
      <router-link to="/train-search">
        <button class="primary-button">立即订票</button>
      </router-link>
    </div>

    <div class="features">
      <div class="feature-item">
        <h3>🚄 多种车型</h3>
        <p>支持高铁、动车、普快等多种列车类型</p>
      </div>
      <div class="feature-item">
        <h3>📅 灵活查询</h3>
        <p>可按出发地、目的地和时间进行筛选</p>
      </div>
      <div class="feature-item">
        <h3>💳 在线下单</h3>
        <p>快速下单、实时锁票，便捷高效</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// 无逻辑，仅展示首页引导
</script>

<style scoped>
.train-home-page {
  text-align: center;
  padding: 60px 20px;
  background-color: #f7f8fa;
}

.hero h1 {
  font-size: 36px;
  color: #2a5fff;
  margin-bottom: 12px;
}
.hero p {
  font-size: 18px;
  color: #666;
  margin-bottom: 24px;
}
.primary-button {
  padding: 12px 28px;
  font-size: 16px;
  background-color: #2a5fff;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}
.primary-button:hover {
  background-color: #1f4ed8;
}

.features {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 24px;
  margin-top: 48px;
}
.feature-item {
  width: 250px;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.feature-item h3 {
  margin-bottom: 8px;
  color: #333;
}
.feature-item p {
  color: #666;
  font-size: 14px;
}
</style>
