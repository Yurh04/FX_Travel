<!-- 文件：src/components/TrainFilter.vue -->
<template>
  <div class="filter-panel">
    <div class="filter-header">
      <h3>筛选</h3>
      <button class="reset-btn" @click="resetFilters">全部重置</button>
    </div>

    <!-- “只看有票” -->
    <div class="filter-section">
      <label>
        <input type="checkbox" v-model="onlyAvailableProxy" />
        只看有票
      </label>
    </div>

    <!-- 车型 -->
    <div class="filter-section">
      <div class="section-header">
        车型 <span class="reset" @click="selectedTypesProxy = []">重置</span>
      </div>
      <div class="checkbox-group">
        <!-- 这里的 `type` 就是原始的 'G/C'、'D'、'Z/T/K' 等 -->
        <label v-for="type in trainTypes" :key="type">
          <input
              type="checkbox"
              :value="type"
              v-model="selectedTypesProxy"
          />
          <!-- 用 labelMap 映射成中文，比如 '高铁(G/C)' -->
          {{ labelMap[type] || type }}
        </label>
      </div>
    </div>

    <!-- 出发时间 -->
    <div class="filter-section">
      <div class="section-header">
        出发时间 <span class="reset" @click="selectedTimesProxy = []">重置</span>
      </div>
      <div class="time-range-group">
        <label v-for="range in timeRanges" :key="range">
          <input
              type="checkbox"
              :value="range"
              v-model="selectedTimesProxy"
          />
          {{ range }}
        </label>
      </div>
    </div>

    <!-- 更多可选项 -->
    <Transition name="fade-expand">
      <div v-if="props.expanded" class="more-options">
        <!-- 座席 -->
        <div class="filter-section">
          <div class="section-header">
            座席 <span class="reset">重置</span>
          </div>
          <div class="checkbox-group">
            <label v-for="seat in seatTypes" :key="seat">
              <input type="checkbox" />
              {{ seat }}
            </label>
          </div>
        </div>

        <!-- 出发车站 -->
        <div class="filter-section">
          <div class="section-header">
            出发车站 <span class="reset">重置</span>
          </div>
          <div class="checkbox-group">
            <label v-for="station in departStations" :key="station">
              <input type="checkbox" />
              {{ station }}
            </label>
          </div>
        </div>

        <!-- 到达车站 -->
        <div class="filter-section">
          <div class="section-header">
            到达车站 <span class="reset">重置</span>
          </div>
          <div class="checkbox-group">
            <label v-for="station in arriveStations" :key="station">
              <input type="checkbox" />
              {{ station }}
            </label>
          </div>
        </div>
      </div>
    </Transition>

    <div class="expand-control" @click="emit('toggle-expand')">
      {{ props.expanded ? '收起列表 ▲' : '展开列表 ▼' }}
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'

const props = defineProps({
  onlyAvailable: Boolean,
  selectedTypes: Array,
  selectedTimes: Array,
  expanded: Boolean,
  trains: {
    type: Array,
    default: () => []
  }
})
const emit = defineEmits([
  'update:onlyAvailable',
  'update:selectedTypes',
  'update:selectedTimes',
  'toggle-expand'
])

// 1. Proxy for “只看有票”
const onlyAvailableProxy = ref(props.onlyAvailable)
watch(onlyAvailableProxy, val => emit('update:onlyAvailable', val))

// 2. Proxy for “车型筛选”，传递原始代码数组
const selectedTypesProxy = ref([...props.selectedTypes])
watch(selectedTypesProxy, val => emit('update:selectedTypes', val))

// 3. Proxy for “时间段筛选”
const selectedTimesProxy = ref([...props.selectedTimes])
watch(selectedTimesProxy, val => emit('update:selectedTimes', val))

// 时间范围选项固定
const timeRanges = [
  '00:00 - 06:00',
  '06:00 - 12:00',
  '12:00 - 18:00',
  '18:00 - 24:00'
]

// 4. trainTypes 从原始数据里取 “t.type” 唯一值
const trainTypes = computed(() => {
  return [
    ...new Set(props.trains.map(t => t.type).filter(Boolean))
  ]
})

// 5. 一个映射表，把原始代码映射为中文标签
const labelMap = {
  'G/C': '高铁(G/C)',
  'D': '动车(D)',
  'Z/T/K': '普通(Z/T/K)',
  'L/Y': '其他(L/Y)'
}

// 座席、出发/到达车站 这些根据你的逻辑保持不变
const seatTypes = computed(() => {
  return [
    ...new Set(
        props.trains
            .map(t => {
              const match = t.seat?.match(/[\u4e00-\u9fa5]+/)
              return match ? match[0] : ''
            })
            .filter(Boolean)
    )
  ]
})

const departStations = computed(() => {
  return [
    ...new Set(props.trains.map(t => t.departStation).filter(Boolean))
  ]
})

const arriveStations = computed(() => {
  return [
    ...new Set(props.trains.map(t => t.arriveStation).filter(Boolean))
  ]
})

// 6. “全部重置” 的方法
const resetFilters = () => {
  onlyAvailableProxy.value = false
  selectedTypesProxy.value = []
  selectedTimesProxy.value = []
}
</script>

<style scoped>
.filter-panel {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  font-family: Arial, sans-serif;
  width: 260px;
  font-size: 14px;
}
.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.reset-btn {
  background: none;
  border: none;
  color: #409eff;
  cursor: pointer;
  font-size: 13px;
}
.filter-section {
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
  margin-top: 16px;
}
.checkbox-group {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
  margin-top: 8px;
}
.time-range-group {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
  margin-top: 8px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
.reset {
  color: #409eff;
  font-size: 13px;
  cursor: pointer;
}
.expand-control {
  text-align: center;
  color: #1677ff;
  font-size: 14px;
  margin-top: 20px;
  cursor: pointer;
  user-select: none;
}
.more-options {
  margin-top: 12px;
  background: #f9fafc;
  padding: 12px;
  border-radius: 8px;
}
.fade-expand-enter-active,
.fade-expand-leave-active {
  transition: all 0.3s ease;
}
.fade-expand-enter-from,
.fade-expand-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
