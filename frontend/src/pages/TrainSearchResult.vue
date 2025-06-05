<!-- 文件：src/pages/TrainResult.vue -->
<template>
  <div class="search-result-page">
    <!-- 顶部进度条 -->
    <div class="progress-wrapper">
      <ProgressBar :currentStep="1" />
    </div>

    <!-- 登录提示：放在右上角 -->
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

    <!-- 主体区域：列表 + 筛选 -->
    <div class="main-section">
      <!-- 车次列表 -->
      <div class="train-list-area">
        <TrainList :trains="filteredTrains" />
      </div>

      <!-- 筛选栏 -->
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
    <!-- **已移除“二维码和下单”部分，不包含任何支付模块** -->
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

// 从 query 中获取参数
const from = ref(route.query.fromCity || '')
const to = ref(route.query.toCity || '')
const date = ref(route.query.departureDate || '')
const returnDate = ref(route.query.returnDate || '')
const tripType = ref(route.query.tripType || 'oneway')

// 筛选条件
const onlyAvailable = ref(false)
const selectedTypes = ref([])
const selectedTimes = ref([])
const expanded = ref(false)

// 后端拉取的原始车次列表
const allTrains = ref([])

// 过滤后需要展示的车次
const filteredTrains = computed(() => {
  return allTrains.value.filter(train => {
    const matchType =
        selectedTypes.value.length === 0 || selectedTypes.value.includes(train.type)

    const matchTime =
        selectedTimes.value.length === 0 ||
        selectedTimes.value.some(range => {
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

// 调用后端接口，按出发时间查询车次
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
    // 假设后端返回 { code: 0, data: [ ... ] }
    allTrains.value = res.data || []
  } catch (err) {
    console.error('获取车次失败：', err)
    allTrains.value = []
  }
}

// 首次挂载时拉取参数对应的车次
onMounted(() => {
  fetchTrains()
})

// 监听路由 query 中的关键参数变化，重新拉取
watch(
    () => [route.query.fromCity, route.query.toCity, route.query.departureDate],
    ([newFrom, newTo, newDate]) => {
      from.value = newFrom || ''
      to.value = newTo || ''
      date.value = newDate || ''
      fetchTrains()
    }
)

// 点击“搜索”时，用当前输入值更新路由 query
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

// 点击“登录”跳转到鉴权页
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

/* 美化登录按钮 */
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

/* 主体区域 */
.main-section {
  display: flex;
  gap: 24px;
  width: 100%;
  max-width: 1200px;
}

/* 移除 train-list-area 的白色背景、圆角与阴影 */
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

/* 移除 filter-area 的白色背景、圆角与阴影 */
.filter-area {
  width: 280px;
  background: transparent;
  border-radius: 0;
  padding: 16px;
  box-shadow: none;
  max-height: 600px;

  overflow-y: auto;      /* 保留纵向滚动 */
  overflow-x: hidden;    /* 隐藏横向滚动条 */
}

/* 响应式：窄屏时将筛选栏移到下方，并布局纵向排列 */
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
