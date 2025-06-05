<!-- 文件：src/components/TrainCard.vue -->
<template>
  <div class="train-card">
    <!-- 左侧基本信息 -->
    <div class="info-left">
      <div class="train-id">{{ train.trainId }}</div>
      <div class="stations">
        <span class="depart">{{ train.departStation }}</span>
        <span class="arrow">→</span>
        <span class="arrive">{{ train.arriveStation }}</span>
      </div>
      <div class="time-range">
        <span>{{ train.departTime }} - {{ train.arriveTime }}</span>
        <span class="duration">历时 {{ train.duration }}</span>
      </div>
    </div>

    <!-- 右侧票务信息 -->
    <div class="info-right">
      <div class="seat">{{ train.seat }}</div>
      <div class="type">{{ typeLabel }}</div>
      <div class="status" :class="train.available ? 'available' : 'unavailable'">
        {{ train.available ? '有票' : '无票' }}
      </div>
      <button class="book-btn" @click="goToBooking" :disabled="!train.available">
        立即预订
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  train: Object
})

const router = useRouter()

const typeLabelMap = {
  'G/C': '高铁',
  'D': '动车',
  'Z/T/K': '普通列车',
  'L/Y': '其他'
}

const typeLabel = typeLabelMap[props.train.type] || props.train.type

const goToBooking = () => {
  router.push({
    path: '/booking',
    query: {
      trainId: props.train.trainId,
      from: props.train.departStation,
      to: props.train.arriveStation,
      departTime: props.train.departTime,
      arriveTime: props.train.arriveTime,
      seat: props.train.seat,
      price: props.train.price || 200
    }
  })
}
</script>

<style scoped>
.train-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  font-family: "Segoe UI", sans-serif;
  transition: transform 0.2s ease;
}
.train-card:hover {
  transform: translateY(-2px);
}

.info-left {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.train-id {
  font-size: 18px;
  font-weight: bold;
  color: #1677ff;
}
.stations {
  font-size: 15px;
  color: #333;
}
.arrow {
  margin: 0 8px;
  color: #888;
}
.time-range {
  font-size: 14px;
  color: #666;
}
.duration {
  margin-left: 10px;
  font-size: 13px;
  color: #999;
}

.info-right {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 140px;
}
.seat {
  font-size: 15px;
  color: #333;
}
.type {
  font-size: 14px;
  color: #888;
}
.status {
  font-size: 13px;
  font-weight: bold;
  padding: 4px 8px;
  border-radius: 6px;
  display: inline-block;
}
.available {
  color: #389e0d;
  background-color: #f6ffed;
  border: 1px solid #b7eb8f;
}
.unavailable {
  color: #cf1322;
  background-color: #fff1f0;
  border: 1px solid #ffa39e;
}

.book-btn {
  background-color: #1677ff;
  color: white;
  border: none;
  padding: 8px 14px;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
}
.book-btn:hover {
  background-color: #135ecf;
}
.book-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
