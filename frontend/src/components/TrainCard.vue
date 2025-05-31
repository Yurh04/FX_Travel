<template>
  <div class="train-card">
    <div class="train-info">
      <div class="train-id">{{ train.trainId }}</div>
      <div class="train-type">类型：{{ typeLabel }}</div>
      <div class="train-time">时间段：{{ timeLabel }}</div>
    </div>
    <div class="ticket-status">
      <span :class="train.available ? 'available' : 'unavailable'">
        {{ train.available ? '有票' : '无票' }}
      </span>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  train: Object
})

const typeLabelMap = {
  'G/C': '高铁',
  'D': '动车',
  'Z/T/K': '普通',
  'L/Y': '其他'
}

const timeLabelMap = {
  '00-06': '00:00 - 06:00',
  '06-12': '06:00 - 12:00',
  '12-18': '12:00 - 18:00',
  '18-24': '18:00 - 24:00'
}

const typeLabel = typeLabelMap[props.train.type] || props.train.type
const timeLabel = timeLabelMap[props.train.timeRange] || props.train.timeRange
</script>

<style scoped>
.train-card {
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  padding: 16px;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.train-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.train-id {
  font-size: 16px;
  font-weight: bold;
}
.train-type, .train-time {
  font-size: 14px;
  color: #666;
}
.ticket-status span {
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 6px;
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
</style>
