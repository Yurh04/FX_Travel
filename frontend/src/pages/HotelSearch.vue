<template>
  <div class="hotel-app">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <ul>
        <li @click="goHotelHome">
          <i class="iconfont">&#xe6b6;</i>酒店
        </li>
        <li @click="goTrainHome">
          <i class="iconfont">&#xe608;</i>火车票
        </li>
      </ul>
    </aside>

    <!-- 主内容区域 -->
    <div class="content-wrapper">
      <!-- 顶部导航栏 -->
      <header class="top-nav">
        <div class="logo-container" @click.stop="goHotelHome">
          <img class="logo" src="/images/ICON.png" alt="风行旅行">
          <span class="site-name">风行旅行</span>
        </div>
        <nav class="nav-links">
          <a @click.prevent="goLogin">登录</a>
          <a @click.prevent="goRegister">注册</a>
          <a @click.prevent="goOrders">我的订单</a>
          <a @click.prevent="goAbout">关于我们</a>
        </nav>
      </header>

      <!-- 主体内容与筛选并排 -->
      <div class="content-body">
        <!-- 高级搜索筛选区域 -->
        <aside class="filter-panel">
          <h3>筛选</h3>
          <el-form label-position="top" class="filter-form">
            <el-form-item label="入住 - 离店">
              <el-date-picker
                v-model="filters.dateRange"
                type="daterange"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD"
                unlink-panels
              />
            </el-form-item>
            <el-form-item label="价格 (元)">
              <el-slider
                v-model="filters.price"
                range
                :min="0"
                :max="2000"
              />
              <div class="price-value">{{ filters.price[0] }} - {{ filters.price[1] }}</div>
            </el-form-item>
            <el-form-item label="星级">
              <el-checkbox-group v-model="filters.stars">
                <el-checkbox v-for="s in [5,4,3,2,1]" :key="s" :label="s">{{ s }}星</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-form-item label="设施">
              <el-checkbox-group v-model="filters.amenities">
                <el-checkbox label="免费WiFi">免费WiFi</el-checkbox>
                <el-checkbox label="游泳池">游泳池</el-checkbox>
                <el-checkbox label="健身房">健身房</el-checkbox>
                <el-checkbox label="停车场">停车场</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-button type="primary" @click="applyFilters">应用筛选</el-button>
          </el-form>
        </aside>

        <!-- 酒店列表区域 -->
        <section class="hotel-list">
          <el-row :gutter="20">
            <el-col :span="8" v-for="hotel in filteredList" :key="hotel.id">
              <el-card class="hotel-card" shadow="hover" @click.native="goDetail(hotel.id)">
                <img :src="hotel.img" class="card-img" />
                <div class="card-body">
                  <h4 class="hotel-name">{{ hotel.name }}</h4>
                  <div class="hotel-info">
                    <span class="stars">{{ hotel.stars }}星</span>
                    <span class="price">￥{{ hotel.price }} 起</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="pageSize"
            v-model:current-page="currentPage"
            @current-change="fetchList"
            class="pagination"
          />
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const goHotelHome = () => router.push({ name: 'HotelHome' })
const goTrainHome = () => router.push({ name: 'HomePage' })
const goLogin = () => router.push({ name: 'LoginPage' })
const goRegister = () => router.push({ name: 'LoginPage', query: { mode: 'register' } })
const goOrders = () => router.push({ name: 'OrderHistory' })
const goAbout = () => router.push({ path: '/about' })

const filters = ref({ dateRange: [], price: [0,2000], stars: [], amenities: [] })
const list = ref([])
const total = ref(0)
const pageSize = 9
const currentPage = ref(1)

const fetchList = (page = 1) => {
  currentPage.value = page
  // Todo: 接口调用
}

onMounted(() => fetchList())
const applyFilters = () => fetchList(1)
const filteredList = computed(() => list.value)
const goDetail = id => router.push({ name: 'HotelDetail', params: { id } })
</script>

<style scoped>
/* 基础布局 */
.hotel-app {
  display: flex;
  height: 100vh;
  overflow: hidden;
}
/* 侧边栏固定 */
.sidebar {
  position: fixed;
  top: 0;
  bottom: 0;
  width: 160px;
  background: #fff;
  border-right: 1px solid #ececec;
  padding-top: 20px;
}
.sidebar ul { list-style: none; margin: 0; padding: 0; }
.sidebar li {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  cursor: pointer;
  transition: background 0.2s;
}
.sidebar li:hover { background: #f0f4ff; }

/* 主内容区域 */
.content-wrapper {
  margin-left: 160px;
  display: flex;
  flex-direction: column;
  width: calc(100% - 160px);
  height: 100%;
}

/* 顶部导航固定 */
.top-nav {
  position: fixed;
  top: 0;
  left: 160px;
  right: 0;
  height: 60px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  z-index: 10;
}
.logo-container {
  display: flex;
  align-items: center;
}
.logo {
  height: 40px;
}
.site-name {
  margin-left: 8px;
  font-family: 'Pacifico', cursive;
  font-size: 24px;
  color: #1677ff;
}
.nav-links a {
  margin-left: 24px;
  color: #333;
  font-weight: 500;
}
.nav-links a:hover { color: #1677ff; }

/* 主体内容与筛选 */
.content-body {
  display: flex;
  margin-top: 60px;
  height: calc(100% - 60px);
  overflow: hidden;
}
.filter-panel {
  width: 240px;
  background: #fff;
  padding: 20px;
  border-right: 1px solid #ececec;
  overflow-y: auto;
}
.filter-panel h3 {
  margin-bottom: 16px;
  font-size: 18px;
  color: #333;
}
.filter-form .el-form-item { margin-bottom: 16px; }
.price-value { margin-top: 8px; color: #1677ff; }

/* 列表区域 */
.hotel-list {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f7fa;
}
.hotel-card {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
}
.card-img {
  width: 100%;
  height: 160px;
  object-fit: cover;
}
.card-body { padding: 12px; }
.hotel-name { font-size: 16px; margin-bottom: 8px; }
.hotel-info { display: flex; justify-content: space-between; }
.price { color: #e91e63; font-weight: bold; }

/* 分页居中 */
.pagination { text-align: center; margin-top: 20px; }
</style>
