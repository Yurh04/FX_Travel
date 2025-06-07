<!-- src/pages/TrainSearchResult.vue -->
<template>
  <div class="search-result-page">
    <!-- 顶部进度条 -->
    <div class="progress-wrapper">
      <ProgressBar :currentStep="1" />
    </div>

    <!-- 登录提示：绝对定位到右上角 -->
    <div class="login-notice-wrapper">
      <LoginNotice @login="handleLogin" />
    </div>

    <!-- 搜索栏：保持与页面路由参数同步 -->
    <div class="search-bar-wrapper">
      <TopSearchBar
          :from="from"
          :to="to"
          :departureDate="date"
          :returnDate="returnDate"
          :tripType="tripType"
          @search="searchTrains"
      />
    </div>

    <!-- 主体区域：车次列表 + 筛选栏 -->
    <div class="main-section">
      <!-- 车次列表 区域 -->
      <div class="train-list-area">
        <TrainList :trains="filteredTrains" />
      </div>

      <!-- 筛选栏 区域 -->
      <div class="filter-area">
        <TrainFilter
            v-model:onlyAvailable="onlyAvailable"
            v-model:selectedTypes="selectedTypes"
            v-model:selectedTimes="selectedTimes"
            :expanded="expanded"
            @toggle-expand="expanded = !expanded"
            :trains="allTrains"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchByDepartureTime } from '../api/train'

// 引入子组件
import ProgressBar from '../components/ProgressBar.vue'
import LoginNotice from '../components/LoginNotice.vue'
import TopSearchBar from '../components/TopSearchBar.vue'
import TrainList from '../components/TrainList.vue'
import TrainFilter from '../components/TrainFilter.vue'

const route = useRoute()
const router = useRouter()

// 从 URL query 中拿到初始参数
const from = ref(route.query.fromCity || '')
const to = ref(route.query.toCity || '')
const date = ref(route.query.departureDate || '')
const returnDate = ref(route.query.returnDate || '')
const tripType = ref(route.query.tripType || 'oneway')

// 过滤器相关状态
const onlyAvailable = ref(false)      // “只看有票”
const selectedTypes = ref([])         // 车型筛选
const selectedTimes = ref([])         // 时间段筛选
const expanded = ref(false)           // 筛选栏展开/收起

// allTrains 存放后端返回的原始 data 数组，各项格式：{ train: {...}, trainSeats: [...] }
const allTrains = ref([])

// filteredTrains：根据 allTrains + 筛选条件 计算得到的数组，传给 TrainList.vue
const filteredTrains = computed(() => {
  return allTrains.value.filter(item => {
    const t = item.train
    const seats = item.trainSeats || []

    // 1. “只看有票”：至少有一个 seat.available > 0
    if (onlyAvailable.value) {
      const hasAvailable = seats.some(s => s.available > 0)
      if (!hasAvailable) return false
    }

    // 2. 车型筛选：selectedTypes 里如果不为空，则 item.train.trainType 必须在选中列表里
    if (selectedTypes.value.length > 0) {
      // 注意：后端返回的 trainType 可能是 "GREEN_TRAIN"、"HIGH_SPEED" 或你实际接口定义的类型
      // 这里假设 selectedTypes 存的也是 trainType 的原始值（如 "GREEN_TRAIN"）
      if (!selectedTypes.value.includes(t.trainType)) {
        return false
      }
    }

    // 3. 时间段筛选：根据 departureTime（ISO 字符串）里的小时数判断
    if (selectedTimes.value.length > 0) {
      // 把 ISO 格式取小时
      const hour = new Date(t.departureTime).getHours()
      // 必须有至少一个选中的时间段包含该小时
      const inAnyRange = selectedTimes.value.some(range => {
        if (range === '00:00 - 06:00') return hour >= 0 && hour < 6
        if (range === '06:00 - 12:00') return hour >= 6 && hour < 12
        if (range === '12:00 - 18:00') return hour >= 12 && hour < 18
        if (range === '18:00 - 24:00') return hour >= 18 && hour < 24
        return false
      })
      if (!inAnyRange) return false
    }

    // 如果都通过，则保留这条记录
    return true
  })
})

/**
 * 从后端拉取车次列表，并把 res.data.data 赋给 allTrains
 */
const fetchTrains = async () => {
  if (!from.value || !to.value || !date.value) {
    allTrains.value = []
    return
  }
  try {
    const res = await searchByDepartureTime({
      departureStation: from.value,
      arrivalStation: to.value,
      departureDate: date.value
    })
    // 后端响应格式：{ sortBy, message, data: [ { train:{…}, trainSeats:[…] }, … ] }
    allTrains.value = res.data.data || []
  } catch (err) {
    console.error('获取车次失败：', err)
    allTrains.value = []
  }
}

// 首次挂载时尝试拉取接口
onMounted(() => {
  fetchTrains()
})

// 监听 URL query 变化，参数改变时重新拉取
watch(
    () => [route.query.fromCity, route.query.toCity, route.query.departureDate],
    ([newFrom, newTo, newDate]) => {
      from.value = newFrom || ''
      to.value = newTo || ''
      date.value = newDate || ''
      fetchTrains()
    }
)

// 点击“搜索”，更新 URL query（触发上面的 watch）
const searchTrains = () => {
  router.push({
    path: '/train-result',
    query: {
      fromCity: from.value,
      toCity: to.value,
      departureDate: date.value,
      returnDate: returnDate.value,
      tripType: tripType.value
    }
  })
}

// 点击“登录”，跳转到鉴权页（router/index.js 中定义的 Authentication 路由）
const handleLogin = () => {
  router.push({ name: 'Authentication' })
}
</script>

<style scoped>
.search-result-page {
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 24px 16px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

/* 顶部进度条 */
.progress-wrapper {
  width: 100%;
  max-width: 1200px;
  margin-bottom: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 登录提示：绝对定位到右上角 */
.login-notice-wrapper {
  position: absolute;
  top: 24px;
  right: 24px;
  z-index: 10;
}

/* 美化登录按钮（此处针对 LoginNotice 内部按钮，也可以删掉） */
.login-notice-wrapper button {
  background: linear-gradient(to right, #409eff, #66b1ff);
  color: #fff;
  border: none;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.login-notice-wrapper button:hover {
  background: linear-gradient(to right, #3a8ee6, #5caceb);
}

/* 搜索栏 */
.search-bar-wrapper {
  width: 100%;
  max-width: 1200px;
  margin-bottom: 32px;
}

/* 主体区域：车次列表 + 筛选栏 */
.main-section {
  display: flex;
  gap: 24px;
  width: 100%;
  max-width: 1200px;
}

/* 车次列表 区域样式 */
.train-list-area {
  flex: 1;
  background: transparent;
  border-radius: 0;
  padding: 16px;
  box-shadow: none;
  min-height: 400px;
  display: flex;
  flex-direction: column;
}

/* 筛选栏 区域样式 */
.filter-area {
  width: 280px;
  background: transparent;
  border-radius: 0;
  padding: 16px;
  box-shadow: none;
  max-height: 600px;
  overflow-y: auto;
  overflow-x: hidden;
}

/* 响应式：窄屏时将筛选栏移到下方 */
@media (max-width: 992px) {
  .main-section {
    flex-direction: column;
  }
  .filter-area {
    width: 100%;
    margin-top: 24px;
    max-height: none;
  }
  .train-list-area {
    min-height: 300px;
  }
}
</style>
