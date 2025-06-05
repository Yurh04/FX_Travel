<template>
  <div class="flat-search-bar">
    <!-- 行程选择 -->
    <div class="trip-options">
      <label>
        <input type="radio" v-model="tripType" value="oneway" />
        <span class="radio-label" :class="{ active: tripType === 'oneway' }">
          单程
        </span>
      </label>
      <label>
        <input type="radio" v-model="tripType" value="round" />
        <span class="radio-label" :class="{ active: tripType === 'round' }">
          往返
        </span>
      </label>
    </div>

    <!-- 城市与日期选择 -->
    <div class="form-row">
      <!-- 出发城市 -->
      <div class="form-box" @click.stop="activeField = 'from'">
        <div class="label">出发城市</div>
        <div class="value">{{ fromCity || '请选择' }}</div>
        <transition name="fade">
          <CitySelect
              v-if="activeField === 'from'"
              @select="selectCity('from', $event)"
          />
        </transition>
      </div>

      <!-- 交换按钮 -->
      <button class="swap-btn" @click="swapCities">⇄</button>

      <!-- 到达城市 -->
      <div class="form-box" @click.stop="activeField = 'to'">
        <div class="label">到达城市</div>
        <div class="value">{{ toCity || '请选择' }}</div>
        <transition name="fade">
          <CitySelect
              v-if="activeField === 'to'"
              @select="selectCity('to', $event)"
          />
        </transition>
      </div>

      <!-- 出发日期 -->
      <div class="form-box">
        <div class="label">出发日期</div>
        <input type="date" v-model="departureDate" />
      </div>

      <!-- 返程日期，仅在往返时显示 -->
      <div class="form-box" v-if="tripType === 'round'">
        <div class="label">返程日期</div>
        <input type="date" v-model="returnDate" />
      </div>

      <!-- 搜索按钮 -->
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watchEffect, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import CitySelect from './CitySelect.vue'

// 接收父组件传入的初始值
const props = defineProps({
  from: String,
  to: String,
  departureDate: String,
  returnDate: String,
  tripType: String
})

const router = useRouter()

// 绑定字段
const fromCity = ref('')
const toCity = ref('')
const departureDate = ref('')
const returnDate = ref('')
const tripType = ref('oneway')
const activeField = ref(null)

// 自动回填 props 到本地值
watchEffect(() => {
  if (props.from) fromCity.value = props.from
  if (props.to) toCity.value = props.to
  if (props.departureDate) departureDate.value = props.departureDate
  if (props.returnDate) returnDate.value = props.returnDate
  if (props.tripType) tripType.value = props.tripType
})

// 交换城市
const swapCities = () => {
  const tmp = fromCity.value
  fromCity.value = toCity.value
  toCity.value = tmp
}

// 选中城市后：更新对应值，并关闭弹窗
const selectCity = (field, city) => {
  if (field === 'from') fromCity.value = city
  else toCity.value = city

  // 关闭 CitySelect 弹窗
  activeField.value = null
}

// 点击搜索按钮，携带参数跳转到结果页
const handleSearch = () => {
  if (!fromCity.value || !toCity.value || !departureDate.value) {
    alert('请填写完整信息')
    return
  }
  router.push({
    path: '/train-result',
    query: {
      fromCity: fromCity.value,
      toCity: toCity.value,
      departureDate: departureDate.value,
      returnDate: returnDate.value,
      tripType: tripType.value
    }
  })
}

// 点击空白区域关闭弹窗
const handleClickOutside = (e) => {
  if (!e.target.closest('.form-box')) {
    activeField.value = null
  }
}

onMounted(() => {
  window.addEventListener('click', handleClickOutside)
})
onBeforeUnmount(() => {
  window.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.flat-search-bar {
  background: white;
  border-radius: 16px;
  padding: 24px;
  font-size: 14px;
  font-family: 'Segoe UI', 'PingFang SC', 'Helvetica Neue', sans-serif;
  max-width: 1080px;
  margin: 0 auto;
}

.trip-options {
  display: flex;
  gap: 24px;
  margin-bottom: 20px;
  font-weight: bold;
  font-size: 15px;
  color: #333;
}

.trip-options input[type='radio'] {
  margin-right: 6px;
  accent-color: #1677ff;
}

.form-row {
  display: flex;
  gap: 16px;
  align-items: flex-end;
}

.form-box {
  position: relative;
  width: 160px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background: white;
  cursor: pointer;
  transition: border 0.2s;
  z-index: 1;
  flex-shrink: 0;
  flex: 1 1 0;
  min-width: 120px;
  max-width: 200px;
}

.form-box:hover {
  border-color: #1677ff;
}

.form-box input[type='date'] {
  border: none;
  background: transparent;
  font-size: 15px;
  padding: 4px 0;
  width: 100%;
  cursor: pointer;
  font-family: inherit;
}

.form-box .label {
  font-size: 12px;
  color: #888;
}

.form-box .value {
  margin-top: 4px;
  font-size: 16px;
  font-weight: bold;
  color: #111;
}

.swap-btn {
  background: #1677ff;
  color: white;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  margin-top: 28px;
  cursor: pointer;
  transition: background 0.3s;
  flex-shrink: 0;
}

.swap-btn:hover {
  background: #125edd;
}

.search-btn {
  background: #1677ff;
  color: white;
  padding: 12px 24px;
  font-size: 15px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.2s;
  font-family: inherit;
  flex-shrink: 0;
}

.search-btn:hover {
  background: #125edd;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
