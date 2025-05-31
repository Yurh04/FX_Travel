<template>
  <div class="search-result-page">
    <ProgressBar :currentStep="1" />
    <LoginNotice @login="handleLogin" />
    <TopSearchBar :from="from" :to="to" :date="date" @search="searchTrains" />

    <div class="main-section">
      <div class="train-list-area">
        <TrainList :trains="filteredTrains" />
      </div>
      <div class="filter-area">
        <TrainFilter
            v-model:onlyAvailable="onlyAvailable"
            v-model:selectedTypes="selectedTypes"
            v-model:selectedTimes="selectedTimes"
            :expanded="expanded"
            @toggle-expand="expanded = !expanded"
        />
      </div>
    </div>

    <QRCodeCard :price="120" />
    <OrderButton @order="handleOrder" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ProgressBar from '../components/ProgressBar.vue'
import LoginNotice from '../components/LoginNotice.vue'
import TopSearchBar from '../components/TopSearchBar.vue'
import TrainList from '../components/TrainList.vue'
import TrainFilter from '../components/TrainFilter.vue'
import QRCodeCard from '../components/QRCodeCard.vue'
import OrderButton from '../components/OrderButton.vue'

const from = ref('上海')
const to = ref('北京')
const date = ref(new Date().toISOString().split('T')[0])
const onlyAvailable = ref(false)
const selectedTypes = ref([])
const selectedTimes = ref([])
const expanded = ref(false)

const allTrains = ref([
  {
    trainId: 'G101',
    type: 'G/C',
    departTime: '08:00',
    arriveTime: '12:30',
    duration: '4小时30分',
    seat: '二等座 ¥553',
    available: true,
  },
  {
    trainId: 'D205',
    type: 'D',
    departTime: '10:20',
    arriveTime: '15:10',
    duration: '4小时50分',
    seat: '二等座 ¥410',
    available: false,
  },
  {
    trainId: 'K512',
    type: 'Z/T/K',
    departTime: '06:15',
    arriveTime: '20:30',
    duration: '14小时15分',
    seat: '硬座 ¥192',
    available: true,
  }
])

const searchTrains = () => {
  alert(`查询：${from.value} → ${to.value} 日期：${date.value}`)
}

const handleOrder = () => {
  alert('订单已提交！')
}

const handleLogin = () => {
  alert('跳转登录页')
}

const filteredTrains = computed(() => {
  return allTrains.value.filter(train => {
    const matchType = selectedTypes.value.length === 0 || selectedTypes.value.includes(train.type)
    const matchTime = selectedTimes.value.length === 0 || selectedTimes.value.some(range => {
      const hour = parseInt(train.departTime.split(':')[0])
      if (range === '00:00 - 06:00') return hour >= 0 && hour < 6
      if (range === '06:00 - 12:00') return hour >= 6 && hour < 12
      if (range === '12:00 - 18:00') return hour >= 12 && hour < 18
      if (range === '18:00 - 24:00') return hour >= 18 && hour < 24
      return false
    })
    const matchAvailable = !onlyAvailable.value || train.available
    return matchType && matchTime && matchAvailable
  })
})
</script>


<style scoped>
.search-result-page {
  background-color: #f8f9fb;
  padding: 24px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.main-section {
  display: flex;
  margin-top: 24px;
  gap: 24px;
  max-width: 1200px;
  width: 100%;
}

.train-list-area {
  flex: 1;
  min-width: 0;
}

.filter-area {
  width: 260px;
}
</style>
