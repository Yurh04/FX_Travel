<!-- 文件：src/components/TrainCard.vue -->
<template>
  <div class="train-card">
    <div class="train-header">
      <span class="train-id">车次：{{ train.trainNumber }}</span>
      <span class="train-type">类型：{{ train.trainType }}</span>
    </div>

    <div class="train-stations">
      <span>{{ train.fromStation }} → {{ train.toStation }}</span>
    </div>

    <div class="train-times">
      <span>出发：{{ formatDateTime(train.departureTime) }}</span>
      <span>到达：{{ formatDateTime(train.arrivalTime) }}</span>
      <span>历时：{{ train.durationMinutes }} 分钟</span>
    </div>

    <div v-if="seats && seats.length" class="seat-info">
      <h4>座位 & 票价：</h4>
      <ul>
        <li v-for="seatItem in seats" :key="seatItem.id" class="seat-item">
          <span>
            {{ seatItem.seatType }} – ¥{{ seatItem.price }} – 剩余：{{ seatItem.available }}
          </span>
          <button
              class="book-btn"
              @click="bookSeat(seatItem)"
              :disabled="seatItem.available === 0"
          >
            {{ seatItem.available > 0 ? '预订' : '售罄' }}
          </button>
        </li>
      </ul>
    </div>
    <div v-else class="no-seat">
      暂无座位信息
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  train: {
    type: Object,
    required: true
  },
  seats: {
    type: Array,
    default: () => []
  }
})

const router = useRouter()

function bookSeat(seatItem) {
  router.push({
    name: 'TrainBooking',
    query: {
      trainId: props.train.trainNumber,
      from: props.train.fromStation,
      to: props.train.toStation,
      departTime: props.train.departureTime,
      arriveTime: props.train.arrivalTime,
      seat: seatItem.seatType,
      price: seatItem.price
    }
  })
}

function formatDateTime(isoString) {
  if (!isoString) return ''
  const date = new Date(isoString)
  const Y = date.getFullYear()
  const M = String(date.getMonth() + 1).padStart(2, '0')
  const D = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const m = String(date.getMinutes()).padStart(2, '0')
  return `${Y}-${M}-${D} ${h}:${m}`
}
</script>

<style scoped>
.train-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  background: #fff;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.train-header {
  display: flex;
  justify-content: space-between;
  font-weight: 600;
  font-size: 16px;
}

.train-stations {
  color: #555;
  font-size: 14px;
}

.train-times {
  display: flex;
  gap: 16px;
  color: #777;
  font-size: 14px;
}

.seat-info h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
}

.seat-info ul {
  list-style: none;
  padding: 0;
  margin: 4px 0 0 0;
}

.seat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.book-btn {
  background: #409eff;
  color: #fff;
  border: none;
  padding: 4px 8px;
  font-size: 13px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.book-btn:disabled {
  background: #d9d9d9;
  cursor: not-allowed;
}
.book-btn:not(:disabled):hover {
  background: #3a8ee6;
}

.no-seat {
  font-size: 13px;
  color: #999;
}
</style>
