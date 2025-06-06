<template>
  <div class="filter-panel">
    <!-- 头部：标题 + 全部重置 -->
    <div class="filter-header">
      <h3>筛选</h3>
      <button class="reset-btn" @click="resetAll">全部重置</button>
    </div>

    <!-- 只看有票 -->
    <div class="filter-section">
      <label class="checkbox-label">
        <input type="checkbox" v-model="onlyAvailableProxy" />
        <span>只看有票</span>
      </label>
    </div>

    <!-- 车型 -->
    <div class="filter-section">
      <div class="section-header">
        车型
        <span class="reset-text" @click="selectedTypesProxy = []">重置</span>
      </div>
      <div class="checkbox-group">
        <label
            v-for="type in trainTypeOptions"
            :key="type"
            class="checkbox-label"
        >
          <input
              type="checkbox"
              :value="type"
              v-model="selectedTypesProxy"
          />
          <span>{{ labelMap[type] || type }}</span>
        </label>
      </div>
    </div>

    <!-- 出发时间段 -->
    <div class="filter-section">
      <div class="section-header">
        出发时间
        <span class="reset-text" @click="selectedTimesProxy = []">重置</span>
      </div>
      <div class="time-range-group">
        <label
            v-for="range in timeRanges"
            :key="range"
            class="checkbox-label"
        >
          <input
              type="checkbox"
              :value="range"
              v-model="selectedTimesProxy"
          />
          <span>{{ range }}</span>
        </label>
      </div>
    </div>

    <!-- 座席类型（展开后显示） -->
    <Transition name="fade-expand">
      <div v-if="props.expanded" class="filter-section">
        <div class="section-header">
          座席类型
          <span class="reset-text" @click="selectedSeatTypesProxy = []">重置</span>
        </div>
        <div class="checkbox-group">
          <label
              v-for="seat in seatTypeOptions"
              :key="seat"
              class="checkbox-label"
          >
            <input
                type="checkbox"
                :value="seat"
                v-model="selectedSeatTypesProxy"
            />
            <span>{{ seat }}</span>
          </label>
        </div>
      </div>
    </Transition>

    <!-- 出发车站（展开后显示） -->
    <Transition name="fade-expand">
      <div v-if="props.expanded" class="filter-section">
        <div class="section-header">
          出发车站
          <span class="reset-text" @click="selectedDepartStationsProxy = []">重置</span>
        </div>
        <div class="checkbox-group">
          <label
              v-for="station in departStationOptions"
              :key="station"
              class="checkbox-label"
          >
            <input
                type="checkbox"
                :value="station"
                v-model="selectedDepartStationsProxy"
            />
            <span>{{ station }}</span>
          </label>
        </div>
      </div>
    </Transition>

    <!-- 到达车站（展开后显示） -->
    <Transition name="fade-expand">
      <div v-if="props.expanded" class="filter-section">
        <div class="section-header">
          到达车站
          <span class="reset-text" @click="selectedArriveStationsProxy = []">重置</span>
        </div>
        <div class="checkbox-group">
          <label
              v-for="station in arriveStationOptions"
              :key="station"
              class="checkbox-label"
          >
            <input
                type="checkbox"
                :value="station"
                v-model="selectedArriveStationsProxy"
            />
            <span>{{ station }}</span>
          </label>
        </div>
      </div>
    </Transition>

    <!-- 展开/收起 控制 -->
    <div class="expand-control" @click="emit('toggle-expand')">
      <span v-if="props.expanded">收起 ▲</span>
      <span v-else>展开 ▼</span>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'

const props = defineProps({
  onlyAvailable: { type: Boolean, default: false },
  selectedTypes: { type: Array, default: () => [] },
  selectedTimes: { type: Array, default: () => [] },
  selectedSeatTypes: { type: Array, default: () => [] },
  selectedDepartStations: { type: Array, default: () => [] },
  selectedArriveStations: { type: Array, default: () => [] },
  expanded: { type: Boolean, default: false },
  trains: { type: Array, default: () => [] }
})

const emit = defineEmits([
  'update:onlyAvailable',
  'update:selectedTypes',
  'update:selectedTimes',
  'update:selectedSeatTypes',
  'update:selectedDepartStations',
  'update:selectedArriveStations',
  'toggle-expand'
])

const onlyAvailableProxy = ref(props.onlyAvailable)
watch(onlyAvailableProxy, val => emit('update:onlyAvailable', val))

const selectedTypesProxy = ref([...props.selectedTypes])
watch(selectedTypesProxy, val => emit('update:selectedTypes', val))

const selectedTimesProxy = ref([...props.selectedTimes])
watch(selectedTimesProxy, val => emit('update:selectedTimes', val))

const selectedSeatTypesProxy = ref([...props.selectedSeatTypes])
watch(selectedSeatTypesProxy, val => emit('update:selectedSeatTypes', val))

const selectedDepartStationsProxy = ref([...props.selectedDepartStations])
watch(selectedDepartStationsProxy, val => emit('update:selectedDepartStations', val))

const selectedArriveStationsProxy = ref([...props.selectedArriveStations])
watch(selectedArriveStationsProxy, val => emit('update:selectedArriveStations', val))

const trainTypeOptions = computed(() => {
  const types = props.trains
      .map(item => item.train?.trainType)
      .filter(Boolean)
  return Array.from(new Set(types)).sort()
})

const timeRanges = [
  '00:00 - 06:00',
  '06:00 - 12:00',
  '12:00 - 18:00',
  '18:00 - 24:00'
]

const seatTypeOptions = computed(() => {
  const allSeats = props.trains
      .flatMap(item => item.trainSeats || [])
      .map(s => s.seatType)
      .filter(Boolean)
  return Array.from(new Set(allSeats)).sort()
})

const departStationOptions = computed(() => {
  const allDepart = props.trains
      .map(item => item.train?.fromStation)
      .filter(Boolean)
  return Array.from(new Set(allDepart)).sort()
})

const arriveStationOptions = computed(() => {
  const allArrive = props.trains
      .map(item => item.train?.toStation)
      .filter(Boolean)
  return Array.from(new Set(allArrive)).sort()
})

const labelMap = {
  'GREEN_TRAIN': '绿皮/城际',
  'HIGH_SPEED': '高铁/动车',
  'BUSINESS_CLASS_SEAT': '商务座',
  'FIRST_CLASS_SEAT': '一等座',
  'SECOND_CLASS_SEAT': '二等座'
}

const resetAll = () => {
  onlyAvailableProxy.value = false
  selectedTypesProxy.value = []
  selectedTimesProxy.value = []
  selectedSeatTypesProxy.value = []
  selectedDepartStationsProxy.value = []
  selectedArriveStationsProxy.value = []
}
</script>

<style scoped>
.filter-panel {
  background: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  font-family: Arial, sans-serif;
  width: 260px;
  font-size: 14px;
  color: #333;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.filter-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
}
.reset-btn {
  background: none;
  border: none;
  color: #409eff;
  cursor: pointer;
  font-size: 13px;
  padding: 2px 4px;
}
.reset-btn:hover {
  text-decoration: underline;
}

.filter-section {
  margin-top: 16px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 14px;
  margin-bottom: 8px;
}
.reset-text {
  color: #409eff;
  font-size: 12px;
  cursor: pointer;
}
.reset-text:hover {
  text-decoration: underline;
}

.checkbox-group,
.time-range-group {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}
.checkbox-label input {
  cursor: pointer;
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

.expand-control {
  text-align: center;
  color: #1677ff;
  font-size: 14px;
  margin-top: 20px;
  cursor: pointer;
  user-select: none;
}
.expand-control:hover {
  text-decoration: underline;
}
</style>
