<template>
  <div class="hotel-app">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <ul>
        <li class="active" @click.prevent>
          <i class="iconfont">&#xe6b6;</i>酒店
        </li>
        <li
          :class="{ active: currentMenu === 'train' }"
          @click="goTrain"
        >
          <i class="iconfont">&#xe608;</i>火车票
        </li>
      </ul>
    </aside>
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 预订酒店模块 -->
      <section class="booking-section">
        <div class="booking-bg"></div> <!-- 新增背景层 -->
        <h2>预订酒店</h2>
        <form class="booking-form" @submit.prevent="searchHotels">
          <div class="form-row">
            <label>
              目的地/酒店名称
              <input v-model="searchCity" placeholder="请输入城市或酒店" />
            </label>
            <label>
              入住
              <input type="date" v-model="checkIn" />
            </label>
            <span class="nights">- 1晚 -</span>
            <label>
              退房
              <input type="date" v-model="checkOut" />
            </label>
            <button class="search-btn" type="submit">搜索</button>
          </div>
        </form>
      </section>

      <!-- 推荐模块 -->
      <section class="banner-section">
        <img class="banner-img" src="/images/hotel_IMG.jpeg" alt="酒店推荐" />
      </section>

      <!-- 酒店推荐 -->
      <section class="recommend-section">
        <div class="recommend-title">
          <span>酒店</span><span class="highlight">推荐</span>
        </div>
        <div class="city-tabs">
          <button
            v-for="city in tabCities"
            :key="city"
            :class="{active: city === currentCity}"
            @click="setCity(city)">
            {{ city }}
          </button>
        </div>
        <div class="hotel-list">
          <div
            v-for="hotel in filteredHotels"
            :key="hotel.id"
            class="hotel-card">
            <img :src="hotel.img" class="hotel-img" />
            <div class="hotel-info">
              <div class="hotel-name">{{ hotel.name }}</div>
              <div class="stars">
                <i v-for="i in hotel.stars" :key="i" class="iconfont">&#xe60a;</i>
              </div>
              <div class="hotel-price">￥{{ hotel.price }} 起</div>
            </div>
          </div>
        </div>
      </section>
    </main>
    <!-- 右侧榜单推荐 -->
    <aside class="rightbar">
      <div class="rank-title">黄山酒店口碑榜</div>
      <div class="rank-list">
        <div class="rank-item" v-for="item in rankHotels" :key="item.id">
          <img :src="item.img" class="rank-img" />
          <div class="rank-info">
            <div class="rank-name">{{ item.name }}</div>
            <div class="rank-price">￥{{ item.price }}起</div>
          </div>
        </div>
      </div>
      <img class="right-pic" src="https://dimg04.c-ctrip.com/images/700g0v000000n2h6vE7AB.jpg" />
    </aside>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const currentMenu = ref(route.path.startsWith('/train') ? 'train' : 'hotel')


function goTrain() {
  currentMenu.value = 'train'
  if (route.path !== '/homepage') router.push('/homepage')
}

const searchCity = ref('上海')
const checkIn = ref(new Date().toISOString().split('T')[0])
const checkOut = ref(new Date(Date.now() + 86400000).toISOString().split('T')[0])

const tabCities = ['上海', '北京', '广州', '三亚']
const currentCity = ref('上海')
const setCity = city => (currentCity.value = city)

const hotels = ref([
  {
    id: 1,
    city: '上海',
    name: '上海吉臣维景酒店',
    stars: 4,
    price: 398,
    img: 'https://dimg04.c-ctrip.com/images/0202p120008x8whkt260C_R_400_400_R5_Q70_D.jpg'
  },
  {
    id: 2,
    city: '上海',
    name: '上海浦东国际机场铂爱国际酒店',
    stars: 5,
    price: 588,
    img: 'https://dimg04.c-ctrip.com/images/0202r120008x8u4gdF9E7_R_400_400_R5_Q70_D.jpg'
  },
  {
    id: 3,
    city: '上海',
    name: '上海徐家汇禧玥酒店',
    stars: 3,
    price: 299,
    img: 'https://dimg04.c-ctrip.com/images/0202m120008x8v1jvA8E2_R_400_400_R5_Q70_D.jpg'
  },
  {
    id: 4,
    city: '北京',
    name: '北京王府井希尔顿酒店',
    stars: 5,
    price: 999,
    img: 'https://dimg03.c-ctrip.com/images/0204j120008cf7ifwA39A_R_400_400_R5_Q70_D.jpg'
  },
  {
    id: 5,
    city: '广州',
    name: '广州天河希尔顿酒店',
    stars: 5,
    price: 799,
    img: 'https://dimg03.c-ctrip.com/images/0203n120008cf5cbxCBA2_R_400_400_R5_Q70_D.jpg'
  },
  {
    id: 6,
    city: '三亚',
    name: '三亚中心皇冠假日酒店',
    stars: 5,
    price: 1190,
    img: 'https://dimg03.c-ctrip.com/images/0201j120008cb9uqx0EDC_R_400_400_R5_Q70_D.jpg'
  }
])
const filteredHotels = computed(() => hotels.value.filter(h => h.city === currentCity.value))

function searchHotels() {
  // 这里可以调用后端接口获取酒店数据
  alert(`已搜索：${searchCity.value}（${checkIn.value} - ${checkOut.value}）`)
}

const rankHotels = [
  {
    id: 1,
    name: '黄山悦榕庄',
    price: 2484,
    img: 'https://dimg03.c-ctrip.com/images/0201g120008r3u8g6E6C0_R_130_130_R5_Q70_D.jpg'
  },
  {
    id: 2,
    name: '黄山雨润涵月楼酒店',
    price: 1892,
    img: 'https://dimg03.c-ctrip.com/images/0201g120008r3u8g6E6C0_R_130_130_R5_Q70_D.jpg'
  },
  {
    id: 3,
    name: '黄山昱城皇冠假日酒店',
    price: 498,
    img: 'https://dimg03.c-ctrip.com/images/0201g120008r3u8g6E6C0_R_130_130_R5_Q70_D.jpg'
  }
]
</script>

<style scoped>
@import url('//at.alicdn.com/t/font_2738890_1x2k9b0b1c6.css'); /* 示例iconfont库 */

.hotel-app {
  display: flex;
  background: #f5f7fa;
  min-height: 100vh;
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
  padding: 0;
  margin: 0;
}
.sidebar li {
  display: flex;
  align-items: center;
  padding: 14px 26px;
  margin-bottom: 8px;
  font-size: 16px;
  color: #222;
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
  padding: 24px 28px 24px 32px;
}

.booking-section {
  position: relative;
  background: linear-gradient(rgba(255,255,255,0.9), rgba(255,255,255,0.9)),
              url('/images/hotel_IMG.jpeg'); /* 本地图片路径 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 40px 24px;
  margin-bottom: 20px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(22, 119, 255, 0.1);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .booking-section {
    background-image: url('@/assets/images/hotel-bg-mobile.jpg'); /* 移动端专用图片 */
  }
}

/* 可选：如果使用独立背景层 */
.booking-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url(https://pic.c-ctrip.com/platform/h5/hotel/bg-hotel-search.png) center/cover;
  z-index: 1;
  opacity: 0.1; /* 调整透明度 */
}

/* 调整表单内容层级 */
.booking-section h2,
.booking-form {
  position: relative; /* 提升层级 */
  z-index: 2; /* 确保内容在背景之上 */
}

/* 优化现有样式 */
.booking-section {
  padding: 40px 24px; /* 增加内边距 */
  margin-bottom: 20px;
  border-radius: 16px; /* 增大圆角 */
  box-shadow: 0 8px 24px rgba(22, 119, 255, 0.1); /* 增强阴影 */
}

.booking-form .form-row {
  background: rgba(255,255,255,0.9); /* 表单半透明白底 */
  padding: 20px;
  border-radius: 12px;
  backdrop-filter: blur(4px); /* 毛玻璃效果 */
}

.booking-form label {
  display: flex;
  flex-direction: column;
  font-size: 15px;
  color: #666;
}
.booking-form input[type="date"], .booking-form input[type="text"], .booking-form input {
  border: 1px solid #e6e6e6;
  border-radius: 5px;
  padding: 6px 10px;
  margin-top: 6px;
  font-size: 15px;
  background: #f9f9f9;
  width: 130px;
}
.booking-form .nights {
  color: #999;
  font-size: 14px;
}
.search-btn {
  background: #1677ff;
  border: none;
  color: #fff;
  padding: 10px 28px;
  font-size: 16px;
  border-radius: 6px;
  cursor: pointer;
  margin-left: 10px;
  transition: background .2s;
}
.search-btn:hover {
  background: #145ecb;
}
.banner-section {
  margin: 20px 0;
}
.banner-img {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 8px #e6f6ff;
}

.recommend-section {
  margin-top: 12px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px #e6f6ff;
  padding: 18px 18px 18px 18px;
}
.recommend-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
}
.recommend-title .highlight {
  color: #ff8800;
  font-weight: 700;
}
.city-tabs {
  display: flex;
  gap: 16px;
  margin-bottom: 18px;
}
.city-tabs button {
  background: #f5f7fa;
  border: none;
  border-radius: 16px;
  color: #222;
  font-size: 15px;
  padding: 6px 20px;
  cursor: pointer;
  transition: background .2s;
}
.city-tabs button.active, .city-tabs button:hover {
  background: #1677ff;
  color: #fff;
}
.hotel-list {
  display: flex;
  gap: 22px;
  overflow-x: auto;
  padding-bottom: 10px;
}
.hotel-card {
  width: 200px;
  background: #f9f9f9;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px #e6f6ff;
  flex-shrink: 0;
}
.hotel-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
.hotel-info {
  padding: 10px 12px 14px 12px;
}
.hotel-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}
.stars {
  color: #fbbf24;
  margin-bottom: 5px;
}
.hotel-price {
  color: #1677ff;
  font-weight: 700;
  font-size: 15px;
}
.rightbar {
  width: 270px;
  padding: 34px 14px 0 14px;
}
.rank-title {
  background: #1677ff;
  color: #fff;
  padding: 10px 18px;
  border-radius: 8px 8px 0 0;
  font-size: 16px;
  font-weight: 700;
}
.rank-list {
  background: #fff;
  border-radius: 0 0 10px 10px;
  box-shadow: 0 2px 8px #e6f6ff;
  padding: 14px 0 14px 0;
  margin-bottom: 18px;
}
.rank-item {
  display: flex;
  align-items: center;
  margin-bottom: 13px;
}
.rank-img {
  width: 46px;
  height: 46px;
  border-radius: 7px;
  margin-right: 10px;
  object-fit: cover;
}
.rank-info {
  flex: 1;
}
.rank-name {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 3px;
}
.rank-price {
  color: #1677ff;
  font-size: 13px;
}
.right-pic {
  width: 100%;
  border-radius: 13px;
  margin-top: 8px;
  object-fit: cover;
}
@media (max-width: 1100px) {
  .rightbar { display: none; }
}
@media (max-width: 900px) {
  .sidebar { display: none; }
  .main-content { padding-left: 10px; padding-right: 10px; }
}
</style>