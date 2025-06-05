<template>
  <div class="hotel-app">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <ul>
        <li :class="{ active: currentMenu === 'hotel' }" @click="goHome">
          <i class="iconfont">&#xe6b6;</i>酒店
        </li>
        <li :class="{ active: currentMenu === 'train' }" @click="goTrain">
          <i class="iconfont">&#xe608;</i>火车票
        </li>
      </ul>
    </aside>

    <!-- 右侧内容区 -->
    <div class="content-wrapper">
      <!-- 顶部导航栏 -->
      <header class="top-nav">
        <div class="logo-section" @click="goHome">
          <img class="logo" src="/images/ICON.png" alt="风行旅行" />
          <span class="site-name">风行旅行</span>
        </div>
        <nav class="nav-links">
          <template v-if="!isLoggedIn">
            <a href="#" @click.prevent="go('/auth')">登录/注册</a>
          </template>
          <template v-else>
            <div class="user-info">
              <img :src="avatarUrl" class="avatar" />
              <span class="nickname">{{ username }}</span>
              <div class="dropdown">
                <a href="#" @click.prevent="go('/orders')">我的订单</a>
                <a href="#" @click.prevent="logout">退出登录</a>
              </div>
            </div>
          </template>
          <a href="#" @click.prevent="go('/messages')" class="message-link">消息中心</a>
          <a href="#" @click.prevent="go('/aboutUs')">关于我们</a>
        </nav>
      </header>

      <!-- 主体内容 -->
      <div class="content-body">
        <main class="main-content">
          <h2 class="page-title">酒店搜索</h2>

          <!-- 搜索 + 高级排序 -->
          <el-card class="search-card" shadow="hover">
            <el-form :inline="true" @submit.native.prevent="onSearch">
              <el-form-item label="目的地/酒店">
                <el-input
                  v-model="params.destination"
                  placeholder="请输入城市或酒店名"
                />
              </el-form-item>

              <el-form-item label="入住">
                <el-date-picker
                  v-model="params.checkInDate"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>

              <el-form-item label="退房">
                <el-date-picker
                  v-model="params.checkOutDate"
                  type="date"
                  placeholder="选择日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>

              <el-form-item label="排序">
                <el-select v-model="sortOption" placeholder="请选择排序">
                  <el-option label="价格 从低到高" value="price_asc" />
                  <el-option label="价格 从高到低" value="price_desc" />
                  <el-option label="评分 从高到低" value="rating_desc" />
                  <el-option label="评分 从低到高" value="rating_asc" />
                </el-select>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="onSearch">搜索</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 搜索结果列表 -->
          <div v-if="hotels.length > 0" class="results">
            <el-row :gutter="20">
              <el-col
                v-for="hotel in sortedHotels"
                :key="hotel.hotelId"
                :xs="24"
                :sm="12"
                :md="8"
                :lg="6"
              >
                <el-card
                  class="hotel-card"
                  shadow="hover"
                  @click="goDetail(hotel.hotelId)"
                >
                  <img :src="hotel.img || defaultImg" class="hotel-thumb" />
                  <div class="hotel-info">
                    <h3 class="hotel-name">{{ hotel.name }}</h3>
                    <div class="hotel-destination">
                      {{ hotel.destination }}
                    </div>
                    <div class="hotel-rating">
                      <i
                        v-for="n in Math.round(hotel.rating)"
                        :key="n"
                        class="iconfont"
                        >&#xe60a;</i
                      >
                      <span>{{ hotel.rating.toFixed(1) }} 星</span>
                    </div>
                    <div class="hotel-price">￥{{ hotel.pricePerNight }} 起</div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <div v-else class="no-results">
            <p>未找到符合条件的酒店</p>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { advancedSearch } from '../api/hotel' // 使用高级搜索接口
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'

// 用户状态管理
const username = ref('未登录')
const isLoggedIn = ref(false)
const avatarUrl = ref('../assets/default-avatar.png')

const logout = () => {
  localStorage.removeItem('username')
  localStorage.removeItem('avatar')
  isLoggedIn.value = false
  username.value = '未登录'
  avatarUrl.value = '/images/default-avatar.png'
  ElMessage.success('已退出登录')
  router.push('/auth')
}

onMounted(() => {
  const name = localStorage.getItem('username')
  const avatar = localStorage.getItem('avatar')
  if (name) {
    username.value = name
    isLoggedIn.value = true
    avatarUrl.value = avatar || '/images/default-avatar.png'
  }
  loadHotels()
})

const router = useRouter()
const route = useRoute()

// 当前菜单高亮
const currentMenu = ref('hotel')
function go(path) {
  router.push(path)
}
function goHome() {
  currentMenu.value = 'hotel'
  router.push({ name: 'HotelHome' })
}
function goTrain() {
  currentMenu.value = 'train'
  router.push({ name: 'train' })
}
function goMessages() {
  currentMenu.value = 'messages'
  router.push({ name: 'MessageCenter' })
}

// 未读消息数量（可选，如果你的项目里有消息逻辑，就写一段 fetchUnreads()）
const unreadCount = ref(0)

// 搜索参数
const params = ref({
  destination: route.query.destination || '',
  checkInDate: route.query.checkInDate || dayjs().format('YYYY-MM-DD'),
  checkOutDate:
    route.query.checkOutDate || dayjs().add(1, 'day').format('YYYY-MM-DD')
})

// 排序选项
const sortOption = ref('') // '', 'price_asc', 'price_desc', 'rating_asc', 'rating_desc'

// 酒店列表
const hotels = ref([])
const defaultImg = '/images/default-hotel.jpg'

// 监听 URL query 变化
watch(
  () => route.query,
  () => {
    params.value.destination = route.query.destination || ''
    params.value.checkInDate =
      route.query.checkInDate || dayjs().format('YYYY-MM-DD')
    params.value.checkOutDate =
      route.query.checkOutDate || dayjs().add(1, 'day').format('YYYY-MM-DD')
    loadHotels()
  }
)

// 初次加载
onMounted(loadHotels)

// 调用后端的 高级搜索（带排序）接口
async function loadHotels() {
  // 如果任一参数为空则不请求
  if (
    !params.value.destination ||
    !params.value.checkInDate ||
    !params.value.checkOutDate
  ) {
    hotels.value = []
    return
  }
  try {
    // 构造给后端的 filters 对象
    const filters = {
      destination: params.value.destination,
      checkInDate: params.value.checkInDate,
      checkOutDate: params.value.checkOutDate
    }
    // 根据 sortOption 添加排序字段
    if (sortOption.value === 'price_asc') {
      filters.sortBy = 'price'
      filters.sortOrder = 'asc'
    } else if (sortOption.value === 'price_desc') {
      filters.sortBy = 'price'
      filters.sortOrder = 'desc'
    } else if (sortOption.value === 'rating_asc') {
      filters.sortBy = 'rating'
      filters.sortOrder = 'asc'
    } else if (sortOption.value === 'rating_desc') {
      filters.sortBy = 'rating'
      filters.sortOrder = 'desc'
    }
    // 调用后端高级搜索接口
    const response = await advancedSearch(filters)
    // 后端返回 should be: response.data = Array<Hotel>
    hotels.value = response.data || []
  } catch (err) {
    console.error('高级搜索酒店失败：', err)
    hotels.value = []
  }
}

// 根据排序选项返回排序后的列表（如果后端已排序可直接用 hotels.value）
const sortedHotels = computed(() => {
  // 假设后端 /api/hotels/search 已经做了排序，这里可以直接 return hotels.value
  // 如果想前端二次排序，可把下面改成手动排序逻辑
  return hotels.value
})

// 点击“搜索”时，只更新 URL query，触发 loadHotels()
function onSearch() {
  router.replace({
    name: 'HotelSearch',
    query: {
      destination: params.value.destination,
      checkInDate: params.value.checkInDate,
      checkOutDate: params.value.checkOutDate
      // sortOption 不放到 query 中，排序由 loadHotels() 读取
    }
  })
}

// 点击进入某个酒店详情
function goDetail(hotelId) {
  router.push({ name: 'HotelDetail', params: { id: hotelId } })
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Pacifico&family=Montserrat:wght@400;600&display=swap');
@import url('//at.alicdn.com/t/font_2738890_1x2k9b0b1c6.css');

.hotel-app {
  display: flex;
  min-height: 100vh;
  background: #f5f7fa;
  font-family: 'Montserrat', Arial, sans-serif;
}

/* 侧边栏 */
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
  position: relative;
  transition: background 0.2s, transform 0.2s;
}
.sidebar li::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: #1677ff;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
}
.sidebar li:hover::after,
.sidebar li.active::after {
  transform: translateX(0);
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
/* 侧边栏小红点 */
.red-dot {
  position: absolute;
  top: 12px;
  right: 16px;
  width: 8px;
  height: 8px;
  background-color: #f56c6c;
  border-radius: 50%;
}

/* 右侧内容区 */
.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 顶部导航 */
.top-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 0 24px;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}
.logo-section {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}
.logo-section:hover {
  transform: scale(1.05);
}
.logo {
  height: 40px;
  width: auto;
}
.site-name {
  margin-left: 10px;
  font-family: 'Pacifico', cursive;
  font-size: 26px;
  color: #1677ff;
  transition: color 0.2s;
}
.logo-region:hover .site-name {
  color: #145ecb;
}
.nav-links a {
  margin-left: 24px;
  color: #333;
  font-weight: 500;
  text-decoration: none;
  position: relative;
  padding-bottom: 5px;
}
.nav-links a::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: #1677ff;
  transition: width 0.3s ease;
}
.nav-links a:hover::after {
  width: 100%;
}
.nav-links a:hover {
  color: #1677ff;
}
.nav-links a:hover { color: #1677ff; }

/* 用户信息样式 */
.user-info {
  position: relative;
  display: flex;
  align-items: center;
  margin-left: 24px;
  cursor: pointer;
}
.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 8px;
}
.nickname {
  font-size: 14px;
  color: #333;
}
.dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  padding: 8px 0;
  min-width: 120px;
  display: none;
  z-index: 10;
}
.user-info:hover .dropdown {
  display: block;
}
.dropdown a {
  display: block;
  padding: 8px 16px;
  color: #333;
  text-decoration: none;
}
.dropdown a:hover {
  background: #f5f7fa;
  color: #1677ff;
}

/* 顶部小红点 */
.red-dot-sm {
  position: absolute;
  top: -2px;
  right: -8px;
  width: 6px;
  height: 6px;
  background-color: #f56c6c;
  border-radius: 50%;
}

/* 主体区域 */
.content-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}
.main-content {
  flex: 1;
  padding: 24px 32px;
  overflow-y: auto;
}

/* 页标题 */
.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 16px;
}

/* 搜索 + 排序 卡片 */
.search-card {
  margin-bottom: 20px;
}
.search-card .el-form-item {
  margin-right: 12px;
}

/* 搜索结果 */
.hotel-card {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  transition: box-shadow 0.2s;
}
.hotel-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.hotel-thumb {
  width: 100%;
  height: 160px;
  object-fit: cover;
}
.hotel-info {
  padding: 12px;
}
.hotel-name {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #333;
}
.hotel-destination {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}
.hotel-rating {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #fbbf24;
  margin-bottom: 8px;
}
.hotel-rating .iconfont {
  margin-right: 4px;
}
.hotel-price {
  font-size: 16px;
  color: #e91e63;
  font-weight: 700;
}

/* 无结果提示 */
.no-results {
  text-align: center;
  color: #999;
  margin-top: 40px;
  font-size: 16px;
}

/* 响应式适配 */
@media (max-width: 900px) {
  .sidebar {
    display: none;
  }
  .main-content {
    padding-left: 10px;
    padding-right: 10px;
  }
}
</style>
