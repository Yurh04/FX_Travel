<template>
  <div class="city-select-popup">
    <div class="city-select-header">热门城市</div>
    <div class="city-grid">
      <span v-for="city in hotCities" :key="city" @click="$emit('select', city)">
        {{ city }}
      </span>
    </div>
  </div>
</template>

<script setup>
const hotCities = [
  '北京', '上海', '广州', '深圳', '杭州', '成都',
  '重庆', '南京', '武汉', '西安', '郑州', '长沙'
]
</script>

<style scoped>
.city-select-popup {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 6px;
  width: 320px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  padding: 16px;
  z-index: 999;
  animation: fadeIn 0.2s ease;
}

.city-select-header {
  font-weight: bold;
  font-size: 15px;
  margin-bottom: 12px;
  color: #333;
}

.city-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px 12px;
  font-size: 14px;
}

.city-grid span {
  background: #f5f7fa;
  padding: 6px 12px;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}
.city-grid span:hover {
  background: #e6f6ff;
  color: #1677ff;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(6px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
