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
        <div class="logo-section">
          <img class="logo" src="/images/ICON.png" alt="风行旅行" />
          <span class="site-name">风行旅行</span>
        </div>
        <nav class="nav-links">
          <a href="#" @click.prevent="go('/login')">登录</a>
          <a href="#" @click.prevent="go('/register')">注册</a>
          <a href="#" @click.prevent="go('/orders')">我的订单</a>
          <a href="#" @click.prevent="go('/about')">关于我们</a>
        </nav>
      </header>

      <!-- 主体内容 -->
      <main class="home-page">
        <div class="banner-section">
          <div class="banner-img"></div>
          <div class="search-bar-box">
            <TopSearchBar />
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import TopSearchBar from '../components/TopSearchBar.vue'
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const currentPath = computed(() => route.path)

const isHotelActive = computed(() => currentPath.value.includes('hotel'))
const isTrainActive = computed(() => currentPath.value.includes('train'))

const go = (path) => {
  router.push(path)
}

const goHotel = () => {
  go('/')
}

const goTrain = () => {
  go('/train')
}
</script>

<style scoped>
@import url('//at.alicdn.com/t/font_2738890_1x2k9b0b1c6.css');
@import url('https://fonts.googleapis.com/css2?family=Pacifico&display=swap');

.main-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f6f9fc;
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

.top-nav {
  height: 60px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #ececec;
}
.logo-section {
  display: flex;
  align-items: center;
}
.logo {
  height: 36px;
}
.site-name {
  font-family: 'Pacifico', cursive;
  font-size: 24px;
  margin-left: 10px;
  color: #1677ff;
}
.nav-links a {
  margin-left: 24px;
  font-size: 14px;
  color: #444;
  text-decoration: none;
}
.nav-links a:hover {
  color: #1677ff;
}

.right-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.home-page {
  padding: 24px 32px;
  flex: 1;
  overflow-y: auto;
}

.banner-section {
  position: relative;
  height: 280px;
  background: url('../assets/train.png') no-repeat center/cover;
  border-radius: 12px;
  overflow: visible; /* 允许内容溢出可见 */
  margin-bottom: 64px; /* 增加底部留白，让 search bar 不被遮住 */
}

.search-bar-box {
  position: absolute;
  bottom: -80px; /* 让下半部分露出在图片外 */
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 20px 24px;
  z-index: 10;
  width: auto;
  max-width: 90%;
}

</style>