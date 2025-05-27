<template>
  <div class="booking-page">

    <!-- 🔍 查询区域 -->
    <div class="search-box">
      <h3>查询火车票</h3>
      <div class="form">
        <input v-model="from" placeholder="出发地" />
        <input v-model="to" placeholder="目的地" />
        <input type="date" v-model="date" />
        <select v-model="sort">
          <option value="">默认排序</option>
          <option value="duration">按旅途时长</option>
          <option value="departureTime">按出发时间</option>
        </select>
        <button @click="searchTrains">查询</button>
      </div>
    </div>

    <!-- 🚆 车次列表展示 -->
    <div class="train-list" v-if="sortedTrains.length > 0">
      <div class="train-card" v-for="train in sortedTrains" :key="train.trainId">
        <div class="train-info">
          <div class="train-number">{{ train.trainId }}</div>
          <div class="time-info">
            <span>{{ train.departureTime }}</span>
            <span class="arrow">→</span>
            <span>{{ train.arrivalTime }}</span>
          </div>
          <div class="duration">历时：{{ train.duration }}</div>
        </div>

        <!-- 座位列表 -->
        <div class="seat-types">
          <div class="seat" v-for="seat in train.types" :key="seat.seatType">
            <p>{{ seat.seatType }}：￥{{ seat.price }} ｜ 余票：{{ seat.available }}</p>
            <button :disabled="seat.available === 0" @click="placeOrder(train, seat)">
              下单
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="no-result">暂无查询结果，请重新选择条件</div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const from = ref('')
const to = ref('')
const date = ref('')
const sort = ref('')
const trains = ref([])

const route = useRoute()

onMounted(() => {
  from.value = route.query.from || ''
  to.value = route.query.to || ''
  date.value = route.query.date || ''

  if (from.value && to.value && date.value) {
    searchTrains()
  }
})

const searchTrains = async () => {
  const res = await axios.get('http://localhost:8080/api/v1/train/search', {
    params: { from: from.value, to: to.value, date: date.value }
  })
  trains.value = res.data
}

const sortedTrains = computed(() => {
  let result = [...trains.value]
  if (sort.value === 'duration') {
    result.sort((a, b) => a.totalDuration - b.totalDuration)
  } else if (sort.value === 'departureTime') {
    result.sort((a, b) => new Date(a.departureTime) - new Date(b.departureTime))
  }
  return result
})

const placeOrder = async (train, seat) => {
  const res = await axios.post('http://localhost:8080/api/v1/train/order', {
    userId: 'u001',
    trainId: train.trainId,
    seatTypeId: seat.seatType,
    passengerName: '张三',
    idNumber: '123456199901010011',
    phone: '13800000000'
  })
  alert(res.data.message)
}
</script>

<style scoped>
.booking-page {
  max-width: 900px;
  margin: auto;
  padding: 20px;
}

.search-box {
  background: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin-bottom: 24px;
}

.search-box h3 {
  margin-bottom: 12px;
  color: #2b8cff;
}

.form {
  display: flex;
  gap: 12px;
}

.form input,
.form select {
  flex: 1;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.form button {
  background: #2b8cff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
}

.train-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.train-card {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.train-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px dashed #eee;
  padding-bottom: 10px;
  margin-bottom: 10px;
}

.train-number {
  font-weight: bold;
  font-size: 16px;
}

.time-info {
  display: flex;
  gap: 8px;
  font-size: 14px;
}

.arrow {
  font-weight: bold;
}

.duration {
  font-size: 14px;
  color: #666;
}

.seat-types {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.seat {
  background: #f5f7fa;
  padding: 8px 12px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.seat button {
  background: #409eff;
  color: white;
  border: none;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.no-result {
  text-align: center;
  color: #999;
  margin-top: 40px;
}
</style>
