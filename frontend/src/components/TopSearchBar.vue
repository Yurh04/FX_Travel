<template>
  <div class="train-search-bar">
    <!-- 城市选择 -->
    <div class="city-row">
      <div class="input-group">
        <label>出发城市</label>
        <input v-model="fromCity" placeholder="出发城市" />
      </div>
      <button class="swap-btn" @click="swapCity">⇄</button>
      <div class="input-group">
        <label>到达城市</label>
        <input v-model="toCity" placeholder="到达城市" />
      </div>
    </div>

    <!-- 日期选择 -->
    <div class="input-group">
      <label>出发日期</label>
      <input type="date" v-model="localDate" />
    </div>

    <!-- 可选返程 -->
    <div v-if="showReturn" class="input-group return-box">
      <label>返程日期</label>
      <div class="input-with-clear">
        <input type="date" v-model="returnDate" />
        <button class="remove-return" @click="removeReturn">×</button>
      </div>
    </div>
    <button v-else class="add-return" @click="showReturn = true">
      + 添加返程
    </button>

    <!-- 搜索 -->
    <button class="search-btn" @click="onSearchClick">搜索</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const fromCity = ref('上海')
const toCity = ref('北京')
const localDate = ref(new Date().toISOString().split('T')[0])
const showReturn = ref(false)
const returnDate = ref('')

const swapCity = () => {
  const temp = fromCity.value
  fromCity.value = toCity.value
  toCity.value = temp
}

const removeReturn = () => {
  showReturn.value = false
  returnDate.value = ''
}

const onSearchClick = () => {
  if (!fromCity.value || !toCity.value) {
    alert('请输入出发和到达城市')
    return
  }

  router.push({
    path: '/train/result',
    query: {
      from: fromCity.value,
      to: toCity.value,
      date: localDate.value,
      return: showReturn.value ? returnDate.value : ''
    }
  })
}
</script>

<style scoped>
.train-search-bar {
  background: white;
  padding: 16px 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
  max-width: 1000px;
  margin: 0 auto;
  font-family: "PingFang SC", "Helvetica Neue", Arial, sans-serif;
}

.city-row {
  display: flex;
  align-items: center;
  gap: 16px;
}

.input-group {
  display: flex;
  flex-direction: column;
  min-width: 160px;
}

.input-group input {
  padding: 10px 14px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 10px;
  width: 160px;
  box-sizing: border-box;
  transition: border-color 0.2s;
}

input[type="date"]:focus,
.input-group input:focus {
  border-color: #409eff;
  outline: none;
}

.swap-btn {
  background: #1677ff;
  color: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  font-size: 18px;
  line-height: 1px;
  text-align: center;
  cursor: pointer;
}
.swap-btn:hover {
  background: #005cbc;
}

.input-with-clear {
  position: relative;
  display: flex;
  align-items: center;
}
.input-with-clear input {
  padding-right: 32px;
}
.remove-return {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  background: transparent;
  border: none;
  font-size: 16px;
  color: #999;
  cursor: pointer;
}
.remove-return:hover {
  color: #ff4d4f;
}

.add-return {
  background: #e6f6ff;
  color: #1677ff;
  border: none;
  padding: 8px 14px;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
}
.add-return:hover {
  background: #d0ebff;
}

.search-btn {
  background: #1677ff;
  color: white;
  border: none;
  padding: 10px 24px;
  font-size: 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.2s;
}
.search-btn:hover {
  background: #005cbc;
}

@media (max-width: 768px) {
  .train-search-bar {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
