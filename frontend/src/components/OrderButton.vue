<template>
  <div class="order-button-wrapper">
    <button class="order-button" @click="handleClick">
      立即预订
    </button>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue'

const emit = defineEmits(['order'])

const handleClick = () => {
  // 向父组件发送 "order" 事件
  emit('order')
}
</script>

<style scoped>
.order-button-wrapper {
  text-align: center;
  margin-top: 20px;
}

.order-button {
  background: linear-gradient(90deg, #ff7a45, #ff4d4f);
  color: white;
  border: none;
  padding: 12px 32px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(255, 77, 79, 0.3);
}

.order-button:hover {
  background: linear-gradient(90deg, #ff6a3d, #ff3333);
}
</style>
