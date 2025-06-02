<template>
  <div class="main-layout">
    <!-- 左侧导航栏 -->
    <aside class="sidebar">
      <ul>
        <li :class="{ active: isHotelActive }" @click="goHotel">
          <i class="iconfont">&#xe6b6;</i>
          <span>酒店</span>
        </li>
        <li :class="{ active: isTrainActive }" @click="goTrain">
          <i class="iconfont">&#xe608;</i>
          <span>火车票</span>
        </li>
      </ul>
    </aside>

    <!-- 右侧整体内容区 -->
    <div class="right-content">
      <!-- 顶部导航栏 -->
      <header class="top-nav">
        <div class="nav-left">
          <img class="logo" src="/images/ICON.png" alt="风行旅行" />
          <span class="site-name">风行旅行</span>
        </div>
        <div class="nav-actions">
          <!-- 未登录状态 -->
          <template v-if="!isLoggedIn">
            <button class="nav-btn" @click="go('/auth')">登录 / 注册</button>
          </template>

          <!-- 已登录状态 -->
          <template v-else>
            <div class="user-info" @click="toggleDropdown">
              <img class="avatar" :src="avatarUrl" alt="头像" />
              <span class="nickname">{{ username }}</span>
              <i class="iconfont">&#xe6a7;</i>
              <div v-if="dropdownVisible" class="dropdown">
                <div class="dropdown-item" @click="go('/orders')">我的订单</div>
                <div class="dropdown-item" @click="logout">退出登录</div>
              </div>
            </div>
          </template>

          <button class="nav-btn" @click="go('/contact')">联系客服</button>
        </div>
      </header>

      <!-- Banner 和浮动搜索框 -->
      <div class="banner-wrapper">
        <div class="banner-img"></div>
        <div class="floating-search-box">
          <TopSearchBar />
        </div>
      </div>

      <!-- 主体内容 -->
      <main class="home-page">
        <!-- 推荐模块 -->
        <section class="train-recommend-section">
          <h2 class="section-title">坐火车去旅行</h2>
          <p class="section-subtitle">热门火车旅游线路</p>
          <div class="recommend-cards">
            <!-- 京广高铁 -->
            <div
                class="card highlight"
                @click="goSearch({ fromCity: '北京', toCity: '广州', desc: '天南地北任我游' })"
            >
              <img src="../assets/img_jingguang.jpg" alt="京广高铁" />
              <div class="overlay"><span class="tag">🔥 推荐</span></div>
              <div class="info">
                <h3>京广高铁</h3>
                <p>天南地北任我游</p>
              </div>
            </div>
            <!-- 京沪高铁 -->
            <div
                class="card"
                @click="goSearch({ fromCity: '北京', toCity: '上海', desc: '千里京沪一日还' })"
            >
              <img src="../assets/img_jinghu.jpg" alt="京沪高铁" />
              <div class="info">
                <h3>京沪高铁</h3>
                <p>千里京沪一日还</p>
              </div>
            </div>
            <!-- 宁杭甬高铁 -->
            <div
                class="card"
                @click="goSearch({ fromCity: '南京', toCity: '杭州', desc: '穿越宁波至杭州，体验江南水乡风情' })"
            >
              <img src="../assets/img_ninghangyong.jpg" alt="宁杭甬高铁" />
              <div class="info">
                <h3>宁杭甬高铁</h3>
                <p>穿越宁波至杭州，体验江南水乡风情</p>
              </div>
            </div>
            <!-- 成渝高铁 -->
            <div
                class="card"
                @click="goSearch({ fromCity: '成都', toCity: '重庆', desc: '爱上天府之国，一览山城雾都风光' })"
            >
              <img src="../assets/img_chengyu.jpg" alt="成渝高铁" />
              <div class="info">
                <h3>成渝高铁</h3>
                <p>爱上天府之国，一览山城雾都风光</p>
              </div>
            </div>
          </div>
        </section>

        <!-- 热门路线 -->
        <section class="train-hotline-section">
          <h2 class="section-title">热门路线</h2>
          <div class="city-tabs">
            <!-- “上海”和“北京”按钮始终可见 -->
            <button
                v-for="city in originCities"
                :key="city"
                :class="{ active: city === activeOrigin }"
                @click="selectOrigin(city)"
            >
              {{ city }}
            </button>
          </div>
          <div class="route-table">
            <div class="col" v-for="(dest, index) in hotDestinations" :key="index">
              <button
                  class="route-btn"
                  @click="goSearch({ fromCity: activeOrigin, toCity: dest.name })"
              >
                <span>{{ activeOrigin }} → {{ dest.name }}</span>
                <span class="price">
                  ¥{{ dest.price !== null ? dest.price : '—' }} 起
                </span>
              </button>
            </div>
          </div>
        </section>

        <!-- 服务保障 -->
        <section class="train-service-section">
          <div class="service-item" v-for="(item, i) in services" :key="i">
            <img :src="item.icon" alt="" />
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import TopSearchBar from '../components/TopSearchBar.vue'
import { searchByDepartureTime } from '../api/train'

const route = useRoute()
const router = useRouter()

const currentPath = computed(() => route.path)
const isHotelActive = computed(() => currentPath.value.includes('hotel'))
const isTrainActive = computed(() => currentPath.value.includes('train'))

const go = (path) => router.push(path)
const goHotel = () => go('/')
const goTrain = () => go('/train')

// 点击推荐卡片 或 热门路线按钮，跳转到 train-result 并传递 fromCity、toCity 参数
const goSearch = (item) => {
  router.push({
    path: '/train-result',
    query: {
      fromCity: item.fromCity,
      toCity: item.toCity
    }
  })
}

const dropdownVisible = ref(false)
const toggleDropdown = () => (dropdownVisible.value = !dropdownVisible.value)

const username = ref('未登录')
const isLoggedIn = ref(false)
const avatarUrl = ref('../assets/default-avatar.png')

const logout = () => {
  localStorage.removeItem('username')
  localStorage.removeItem('avatar')
  isLoggedIn.value = false
  username.value = '未登录'
  avatarUrl.value = '/images/default-avatar.png'
  dropdownVisible.value = false
  ElMessage.success('已退出登录')
  go('/auth')
}

onMounted(() => {
  const name = localStorage.getItem('username')
  const avatar = localStorage.getItem('avatar')
  if (name) {
    username.value = name
    isLoggedIn.value = true
    if (avatar) avatarUrl.value = avatar
  }
  // 默认选中第一个出发地
  selectOrigin(originCities[0])
})

/** 服务保障数据（示例） */
const services = ref([
  { icon: '../assets/icon_service.png', title: '安心服务', desc: '7×24 小时可预订' },
  { icon: '../assets/icon_price.png', title: '放心价格', desc: '专享携程优惠券礼品卡' },
  { icon: '../assets/icon_refund.png', title: '退改无忧', desc: '开车前25分钟可退票/改签' }
])

/** 热门路线模块：可选出发城市 */
const originCities = ['上海', '北京']
const activeOrigin = ref(originCities[0])

/** 热门到达地列表：示例数据 */
const hotDestinations = ref([
  { name: '广州', price: null },
  { name: '杭州', price: null },
  { name: '南京', price: null },
  { name: '成都', price: null }
])

/** 当 activeOrigin 改变时，从后端请求每条热门路线最低票价 */
const fetchPrices = async () => {
  const promises = hotDestinations.value.map(async (dest) => {
    try {
      const res = await searchByDepartureTime({
        departureStation: activeOrigin.value,
        arrivalStation: dest.name,
        departureDate: new Date().toISOString().split('T')[0] // 默认今日
      })
      const list = res.data || []
      dest.price = list.length > 0 ? list[0].price : null
    } catch {
      dest.price = null
    }
  })
  await Promise.all(promises)
}

const selectOrigin = (city) => {
  activeOrigin.value = city
  fetchPrices()
}

watch(activeOrigin, () => {
  fetchPrices()
})
</script>

<style scoped>
/* 简洁可见的 Sidebar 样式 */
.sidebar {
  width: 200px;
  background: #ffffff;
  border-right: 1px solid #eaeaea;
  padding-top: 20px;
  position: fixed;
  top: 0;
  bottom: 0;
  z-index: 10;
}

.sidebar ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.sidebar li {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  margin: 4px 0;
  font-size: 16px;
  color: #333333;
  cursor: pointer;
  transition: background 0.2s;
}

.sidebar li .iconfont {
  font-size: 20px;
  margin-right: 10px;
  color: #333333;
}

.sidebar li:hover {
  background: #f5f5f5;
}

.sidebar li.active {
  background: #e6f6ff;
  color: #1677ff;
}

.sidebar li.active .iconfont {
  color: #1677ff;
}

/* 确保右侧内容不被 Sidebar 遮挡 */
.right-content {
  margin-left: 200px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  padding: 0 32px;
  background: #fff;
  border-bottom: 1px solid #eaeaea;
  position: sticky;
  top: 0;
  z-index: 20;
}

.nav-left {
  display: flex;
  align-items: center;
}
.logo {
  height: 40px;
  width: auto;
  margin-right: 12px;
}
.site-name {
  font-size: 20px;
  font-weight: bold;
  color: #1677ff;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-btn {
  background: #1677ff;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s;
}
.nav-btn:hover {
  background: #409cff;
}

.user-info {
  display: flex;
  align-items: center;
  position: relative;
  cursor: pointer;
}
.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 8px;
}
.nickname {
  font-size: 14px;
  color: #333333;
  margin-right: 4px;
}
.nav-actions .iconfont {
  font-size: 12px;
  color: #666666;
}

.dropdown {
  position: absolute;
  top: 48px;
  right: 0;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  width: 140px;
}
.dropdown-item {
  padding: 10px 16px;
  font-size: 14px;
  color: #333333;
  cursor: pointer;
  transition: background 0.2s;
}
.dropdown-item:hover {
  background: #f3f6fa;
}

/* Banner 区 */
.banner-wrapper {
  position: relative;
  width: 100%;
  height: 300px;
  background: url('../assets/train.png') center/cover no-repeat;
  //background-size: cover;
  //background-position: center;
}

.banner-img {
  width: 100%;
  height: 100%;
  opacity: 0;
}

.floating-search-box {
  position: absolute;
  left: 50%;
  bottom: -40px;
  transform: translateX(-50%);
  background: #fff;
  padding: 20px 32px;
  border-radius: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  width: 90%;
  max-width: 960px;
  z-index: 10;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 主体内容 */
.home-page {
  margin-top: 60px;
  padding: 60px 32px 32px;
}

/* 推荐模块 */
.train-recommend-section {
  text-align: center;
  margin-bottom: 60px;
}
.section-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333333;
}
.section-subtitle {
  font-size: 16px;
  color: #666666;
  margin-bottom: 32px;
}

.recommend-cards {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 24px;
}
.card {
  position: relative;
  width: 260px;
  border-radius: 16px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}
.card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
}
.card .overlay {
  position: absolute;
  top: 12px;
  left: 12px;
  background: #ffb400;
  color: #fff;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}
.card .info {
  padding: 16px;
  text-align: left;
}
.card .info h3 {
  font-size: 18px;
  margin: 0 0 8px;
  color: #333333;
}
.card .info p {
  font-size: 14px;
  color: #666666;
  margin: 0;
}
.card:hover {
  transform: translateY(-6px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

/* 热门路线模块 */
.train-hotline-section {
  text-align: center;
  margin-bottom: 60px;
}
.city-tabs {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 24px;
}
.city-tabs button {
  padding: 8px 20px;
  font-size: 15px;
  color: #333333;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 24px;
  cursor: pointer;
  transition: background 0.3s, color 0.3s;
}
.city-tabs button.active {
  background: #1677ff;
  color: #fff;
  border-color: #1677ff;
}
.city-tabs button:hover:not(.active) {
  background: #f3f6fa;
}

.route-table {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 24px;
}
.col {
  display: flex;
  flex-direction: column;
}
.route-btn {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 14px 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  width: 280px;
  font-size: 15px;
  color: #333333;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  border: none;
}
.route-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}
.route-btn .price {
  color: #ffb400;
  font-weight: 600;
}

/* 服务保障模块 */
.train-service-section {
  display: flex;
  justify-content: center;
  gap: 80px;
  padding: 60px 0;
  background: #fff;
  border-top: 1px solid #eee;
}
.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 180px;
}
.service-item img {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: #f3f6fa;
  padding: 12px;
  margin-bottom: 16px;
}
.service-item h4 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #333333;
}
.service-item p {
  font-size: 14px;
  color: #666666;
  text-align: center;
  margin: 0;
}
</style>
