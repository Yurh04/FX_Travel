<template>
  <div class="train-app">
    <!-- 左侧边栏 -->
    <aside class="sidebar">
      <ul>
        <li @click="go('/')" :class="{active: false}">
          <i class="iconfont">&#xe6b6;</i>酒店
        </li>
        <li class="active" @click.prevent>
          <i class="iconfont">&#xe608;</i>火车票
        </li>
      </ul>
    </aside>
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部Banner -->
      <div class="banner">
        <img src="/images/train.png" alt="banner" />
      </div>
      <!-- 功能区 -->
      <div class="function-card">
        <div class="function-grid">
          <div class="function-item" @click="go('/booking')">
            <img src="https://cdn-icons-png.flaticon.com/512/3721/3721814.png" alt="预订火车票" />
            <span>预订火车票</span>
          </div>
          <div class="function-item" @click="go('/orders')">
            <img src="https://cdn-icons-png.flaticon.com/512/3050/3050525.png" alt="历史订单" />
            <span>历史订单</span>
          </div>
          <div class="function-item" @click="go('/meal')">
            <img src="https://cdn-icons-png.flaticon.com/512/5973/5973827.png" alt="火车订餐" />
            <span>火车订餐</span>
          </div>
          <div class="function-item" @click="go('/login')">
            <img src="https://cdn-icons-png.flaticon.com/512/1077/1077063.png" alt="用户登录" />
            <span>用户登录</span>
          </div>
        </div>
      </div>
      <!-- 预订火车票表单 -->
      <div class="train-search-card">
        <div class="tabs">
          <label>
            <input type="radio" value="单程" v-model="tripType" /> 单程
          </label>
          <label>
            <input type="radio" value="往返" v-model="tripType" /> 往返
          </label>
          <label>
            <input type="radio" value="中转" v-model="tripType" /> 中转
          </label>
        </div>
        <div class="search-row">
          <div class="city-input">
            <span>出发城市</span>
            <input v-model="fromCity" />
          </div>
          <span class="arrow">&#8594;</span>
          <div class="city-input">
            <span>到达城市</span>
            <input v-model="toCity" />
          </div>
          <div class="date-input">
            <span>出发日期</span>
            <input type="date" v-model="date" />
            <span class="weekday">{{ weekday }}</span>
          </div>
          <button class="add-return" v-if="tripType==='往返'">+ 添加返程</button>
          <button class="search-btn" @click="search">搜索</button>
        </div>
        <div class="search-options">
          <label>
            <input type="checkbox" v-model="onlyHighSpeed" />
            只搜高铁动车
          </label>
          <span class="right-link">查 火车票订单</span>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const go = (path) => router.push(path)

const tripType = ref('单程')
const fromCity = ref('上海')
const toCity = ref('北京')
const date = ref(new Date().toISOString().split('T')[0])
const onlyHighSpeed = ref(false)

const search = () => {
  alert(`出发：${fromCity.value} 到达：${toCity.value} 日期：${date.value} 类型：${tripType.value}`)
}

const weekdayArr = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六']
const weekday = computed(() => {
  const d = new Date(date.value)
  return weekdayArr[d.getDay()]
})
</script>

<style scoped>
@import url('//at.alicdn.com/t/font_2738890_1x2k9b0b1c6.css');

.train-app {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
  font-family: "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
}
.sidebar {
  width: 160px;
  background: #fff;
  border-right: 1px solid #ececec;
  padding-top: 20px;
}
.sidebar ul {
  list-style: none;
  margin: 0; padding: 0;
}
.sidebar li {
  display: flex;
  align-items: center;
  padding: 14px 26px;
  font-size: 16px;
  color: #222;
  margin-bottom: 8px;
  border-radius: 5px 0 0 5px;
  cursor: pointer;
  transition: background .2s;
}
.sidebar li.active, .sidebar li:hover {
  background: #e6f6ff;
  color: #1677ff;
}
.sidebar .iconfont {
  font-size: 20px;
  margin-right: 12px;
}
.main-content {
  flex: 1;
  min-width: 0;
  padding: 0 28px 24px 32px;
}
.banner img {
  width: 100%;
  height: 130px;
  object-fit: cover;
  border-radius: 8px;
  margin: 20px 0 0 0;
  box-shadow: 0 2px 8px #e6f6ff;
}

/* 新增功能区样式 */
.function-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 8px #e6f6ff;
  margin-top: -40px;
  margin-bottom: 36px;
  padding: 26px 0 10px 0;
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
  position: relative;
  z-index: 2;
}
.function-grid {
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 18px;
}
.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  padding: 10px 14px;
  transition: background 0.2s;
  border-radius: 8px;
}
.function-item:hover {
  background: #e6f6ff;
}
.function-item img {
  width: 54px;
  height: 54px;
  margin-bottom: 7px;
}
.function-item span {
  font-size: 15px;
  color: #222;
  font-weight: 600;
}

/* 火车票表单 */
.train-search-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 8px #e6f6ff;
  padding: 24px 28px 18px 28px;
  margin-top: 24px;
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin-left: auto;
  margin-right: auto;
}
.tabs {
  display: flex;
  gap: 32px;
  margin-bottom: 16px;
  font-size: 16px;
}
.tabs label {
  display: flex;
  align-items: center;
  gap: 4px;
  cursor: pointer;
}
.search-row {
  display: flex;
  align-items: center;
  gap: 18px;
  margin-bottom: 8px;
}
.city-input {
  display: flex;
  flex-direction: column;
  font-size: 14px;
  text-align: left;
}
.city-input input {
  border: none;
  background: #f5f7fa;
  border-radius: 6px;
  padding: 7px 14px;
  font-size: 16px;
  margin-top: 4px;
  width: 110px;
}
.arrow {
  font-size: 22px;
  color: #aaa;
}
.date-input {
  display: flex;
  flex-direction: column;
  font-size: 14px;
  text-align: left;
}
.date-input input[type="date"] {
  border: none;
  background: #f5f7fa;
  border-radius: 6px;
  padding: 7px 14px;
  font-size: 16px;
  margin-top: 4px;
  width: 140px;
}
.weekday {
  font-size: 13px;
  color: #888;
  margin-top: 2px;
}
.add-return {
  background: #e6f6ff;
  color: #1677ff;
  border: none;
  border-radius: 7px;
  font-size: 15px;
  padding: 9px 18px;
  margin-left: 8px;
  cursor: pointer;
}
.search-btn {
  background: #0080ff;
  color: #fff;
  border: none;
  border-radius: 7px;
  font-size: 17px;
  padding: 12px 32px;
  margin-left: 8px;
  cursor: pointer;
  transition: background .2s;
}
.search-btn:hover {
  background: #005cbc;
}
.search-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  color: #666;
  font-size: 14px;
}
.search-options input[type="checkbox"] {
  margin-right: 6px;
}
.search-options .right-link {
  color: #1677ff;
  cursor: pointer;
}
@media (max-width: 1100px) {
  .sidebar {display:none;}
  .main-content {padding:0 0 24px 0;}
  .function-card, .train-search-card {max-width:100%;}
}
</style>