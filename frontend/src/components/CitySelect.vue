<!-- CitySelect.vue -->
<template>
  <div class="city-select-popup" @click.stop>
    <div class="city-select-header">
      <span>热门城市</span>
      <button class="close-btn" @click="handleClose">×</button>
    </div>
    <div class="city-grid">
      <span
          v-for="city in hotCities"
          :key="city"
          @click="handleSelect(city)"
      >
        {{ city }}
      </span>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  field: {
    type: String,
    required: true
  }
})

const hotCities = [
  '北京', '上海', '广州', '深圳', '杭州', '成都',
  '重庆', '南京', '武汉', '西安', '郑州', '长沙'
]

// 发出选中事件和关闭事件
const emit = defineEmits(['select', 'close'])

function handleSelect(city) {
  emit('select', { field: props.field, city })
}

function handleClose() {
  emit('close')
}
</script>

<style scoped>
.city-select-popup {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 6px;
  width: 340px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  padding: 20px;
  z-index: 999;
  animation: fadeIn 0.2s ease-out;
}

.city-select-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #444;
  margin-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 6px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #999;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.2s, color 0.2s;
}

.close-btn:hover {
  background: #f0f0f0;
  color: #666;
}

.city-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px 14px;
  font-size: 14px;
}

.city-grid span {
  display: block;
  background: #f9f9f9;
  padding: 8px 0;
  border-radius: 8px;
  text-align: center;
  color: #555;
  cursor: pointer;
  transition: background 0.2s, color 0.2s, transform 0.1s;
}

.city-grid span:hover {
  background: #e6f6ff;
  color: #1677ff;
  transform: translateY(-2px);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>