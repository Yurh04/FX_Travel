<template>
  <div class="main-layout">
    <!-- 左侧导航栏 -->
    <aside class="sidebar">
      <ul>
        <li :class="{ active: isHotelActive }" @click="goHotel">
          <i class="iconfont">&#xe6b6;</i>酒店
        </li>
        <li :class="{ active: isTrainActive }" @click="goTrain">
          <i class="iconfont">&#xe608;</i>火车票
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
            <span class="nav-btn" @click="go('/auth')">登录 / 注册</span>
          </template>

          <!-- 已登录状态 -->
          <template v-else>
            <div class="user-info" @click="toggleDropdown">
              <img class="avatar" :src="avatarUrl" alt="头像" />
              <span class="nickname">{{ username }}</span>
              <i class="iconfont">&#xe6a7;</i>
              <div v-if="dropdownVisible" class="dropdown">
                <div @click="go('/orders')">我的订单</div>
                <div @click="logout">退出登录</div>
              </div>
            </div>
          </template>

          <span class="nav-btn" @click="go('/contact')">联系客服</span>
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
            <div class="card highlight">
              <img src="../assets/img.png" alt="京广高铁" />
              <div class="overlay"><span class="tag">🔥 推荐</span></div>
              <div class="info">
                <h3>京广高铁</h3>
                <p>天南地北任我游</p>
              </div>
            </div>
            <div class="card" v-for="(item, index) in otherRecs" :key="index">
              <img :src="item.img" :alt="item.title" />
              <div class="info">
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>
              </div>
            </div>
          </div>
        </section>

        <!-- 热门路线 -->
        <section class="train-hotline-section">
          <h2 class="section-title">热门路线</h2>
          <div class="city-tabs">
            <button v-for="city in hotCities" :key="city" :class="{ active: city === activeCity }" @click="activeCity = city">{{ city }}</button>
          </div>
          <div class="route-table">
            <div class="col">
              <div class="route" v-for="(item, i) in leftRoutes" :key="i">
                <span>{{ item.name }}</span>
                <span class="price">¥{{ item.price }} 起</span>
              </div>
            </div>
            <div class="col">
              <div class="route" v-for="(item, i) in rightRoutes" :key="i">
                <span>{{ item.name }}</span>
                <span class="price">¥{{ item.price }} 起</span>
              </div>
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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import TopSearchBar from '../components/TopSearchBar.vue'

const route = useRoute()
const router = useRouter()

const currentPath = computed(() => route.path)
const isHotelActive = computed(() => currentPath.value.includes('hotel'))
const isTrainActive = computed(() => currentPath.value.includes('train'))

const go = (path) => router.push(path)
const goHotel = () => go('/')
const goTrain = () => go('/train')

const dropdownVisible = ref(false)
const toggleDropdown = () => dropdownVisible.value = !dropdownVisible.value

const username = ref('未登录')
const isLoggedIn = ref(false)
const avatarUrl = ref('/images/default-avatar.png') // 默认头像路径

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
})

const hotCities = ['上海', '北京', '杭州', '广州', '南京', '天津', '武汉', '西安']
const activeCity = ref('上海')

const leftRoutes = ref([
  { name: '上海到北京火车票', price: 553.0 },
  { name: '上海到杭州火车票', price: 73.0 },
  { name: '上海到南京火车票', price: 134.5 },
  { name: '上海到苏州火车票', price: 29.5 },
  { name: '上海到济南火车票', price: 398.5 },
  { name: '上海到无锡火车票', price: 49.5 }
])

const rightRoutes = ref([
  { name: '上海到常州火车票', price: 69.5 },
  { name: '上海到合肥火车票', price: 195.0 },
  { name: '上海到天津火车票', price: 508.5 }
])

const otherRecs = ref([
  { title: '京沪高铁', desc: '千里京沪一日还', img: '/images/img.jpg' },
  { title: '宁杭甬', desc: '最舒服的高铁线路', img: '/images/img.jpg' },
  { title: '成渝高铁', desc: '爱上天府之国', img: '/images/img.jpg' }
])

const services = ref([
  { icon: '/images/icon_service.png', title: '安心服务', desc: '7x24小时可预订' },
  { icon: '/images/icon_price.png', title: '放心价格', desc: '专享携程优惠券礼品卡等' },
  { icon: '/images/icon_refund.png', title: '退改无忧', desc: '开车前25分钟可退票/改签' }
])
</script>



<style scoped>
@import url('//at.alicdn.com/t/font_2738890_1x2k9b0b1c6.css');
@import url('https://fonts.googleapis.com/css2?family=Pacifico&display=swap');

.main-layout {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #f3f6fa, #ffffff);
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
  transition: background 0.2s;
}
.sidebar li.active,
.sidebar li:hover {
  background: #e6f6ff;
  color: #1677ff;
}
.sidebar .iconfont {
  font-size: 20px;
  margin-right: 12px;
}

.right-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.banner-wrapper {
  position: relative;
  width: 100%;
  height: 280px;
  background-image: url('../assets/train.png');
  background-size: cover;
  background-position: center;
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
  border-radius: 16px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  width: 90%;
  max-width: 960px;
  z-index: 10;
  display: flex;
  justify-content: center;
  align-items: center;
}

main.home-page {
  margin-top: 60px;
}

.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 64px;
  padding: 0 32px;
  background: #fff;
  border-bottom: 1px solid #eee;
  position: relative;
  z-index: 10;
}
.nav-left {
  display: flex;
  align-items: center;
}
.logo {
  height: 40px;
  width: auto;
  object-fit: contain;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 24px;
}

.nav-actions .user-info {
  display: flex;
  align-items: center;
  position: relative;
  cursor: pointer;
}
.nav-actions .avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 8px;
}
.nav-actions .nickname {
  font-size: 14px;
  margin-right: 4px;
}
.nav-actions .iconfont {
  font-size: 12px;
}

.nav-actions .dropdown {
  position: absolute;
  top: 40px;
  right: 0;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  border-radius: 6px;
  overflow: hidden;
  font-size: 14px;
  width: 120px;
}
.nav-actions .dropdown div {
  padding: 10px 16px;
  cursor: pointer;
}
.nav-actions .dropdown div:hover {
  background-color: #f5f5f5;
}

.nav-actions .nav-btn {
  font-size: 14px;
  cursor: pointer;
  color: #333;
}
.nav-actions .nav-btn:hover {
  color: #1677ff;
}

.train-recommend-section {
  text-align: center;
  margin-top: 40px;
}
.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}
.section-subtitle {
  font-size: 14px;
  color: #999;
  margin-bottom: 24px;
}
.recommend-cards {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;
}
.recommend-cards .card {
  width: 240px;
  border-radius: 12px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
}
.recommend-cards .card:hover {
  transform: scale(1.03);
  box-shadow: 0 6px 12px rgba(0,0,0,0.1);
}
.recommend-cards .card img {
  width: 100%;
  height: 140px;
  object-fit: cover;
}
.recommend-cards .info {
  padding: 12px;
  text-align: left;
}
.recommend-cards .info h3 {
  font-size: 16px;
  margin: 0;
}
.recommend-cards .info p {
  font-size: 12px;
  color: #666;
}
.recommend-cards .highlight .overlay {
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(255,0,0,0.75);
  color: #fff;
  font-size: 12px;
  padding: 4px 8px;
  border-bottom-right-radius: 8px;
}

.train-hotline-section {
  margin: 48px 0;
  text-align: center;
}
.city-tabs {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}
.city-tabs button {
  padding: 6px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
}
.city-tabs button.active {
  background: #1677ff;
  color: #fff;
  border-color: #1677ff;
}
.route-table {
  display: flex;
  justify-content: center;
  gap: 32px;
  margin-top: 16px;
}
.route-table .col {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.route-table .route {
  display: flex;
  justify-content: space-between;
  background: #fff;
  padding: 10px 16px;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  width: 240px;
  font-size: 14px;
  transition: transform 0.3s;
}
.route-table .route:hover {
  transform: scale(1.02);
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}
.route-table .price {
  color: #f60;
  font-weight: bold;
}

.train-service-section {
  display: flex;
  justify-content: center;
  gap: 80px;
  padding: 48px 0;
  text-align: center;
  flex-wrap: wrap;
}
.service-item img {
  width: 48px;
  border-radius: 50%;
  background: #f0f0f0;
  padding: 8px;
  margin-bottom: 12px;
}
.service-item h4 {
  font-size: 16px;
  margin-bottom: 4px;
}
.service-item p {
  font-size: 12px;
  color: #666;
}

@media (max-width: 768px) {
  .train-service-section {
    flex-direction: column;
    gap: 32px;
  }
  .route-table {
    flex-direction: column;
    align-items: center;
  }
  .floating-search-box {
    width: 95%;
    padding: 16px;
  }
  .nav-actions {
    flex-direction: column;
    gap: 12px;
    position: static;
  }
}
</style>