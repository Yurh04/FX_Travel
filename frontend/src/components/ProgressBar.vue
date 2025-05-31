<template>
  <div class="progress-container">
    <div
        v-for="(step, index) in steps"
        :key="index"
        class="progress-step"
        :class="{ active: index + 1 === currentStep }"
    >
      <div class="circle">{{ index + 1 }}</div>
      <div class="label">{{ step }}</div>
      <div v-if="index < steps.length - 1" class="line"></div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  currentStep: {
    type: Number,
    default: 1
  }
})

const steps = ['填写', '支付', '完成']
</script>

<style scoped>
.progress-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 24px 0;
  gap: 16px;
}

.progress-step {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #999;
}

.progress-step.active {
  color: #1677ff;
  font-weight: bold;
}

.circle {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background-color: #ddd;
  color: #fff;
  text-align: center;
  line-height: 24px;
  font-size: 14px;
}

.progress-step.active .circle {
  background-color: #1677ff;
}

.label {
  font-size: 14px;
}

.line {
  width: 32px;
  height: 2px;
  background-color: #ccc;
}
</style>
