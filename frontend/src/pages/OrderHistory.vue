<template>
  <div class="order-history">
    <h2>🧾 我的历史订单</h2>

    <!-- 🔍 筛选工具条 -->
    <div class="filters">
      <input v-model="keyword" placeholder="搜索车次/站点/餐品" />
      <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" />
      <select v-model="selectedStatus">
        <option value="">全部状态</option>
        <option value="COMPLETED">已完成</option>
        <option value="CANCELLED">已取消</option>
        <option value="PENDING">处理中</option>
      </select>
      <button @click="fetchOrders">查询</button>
    </div>

    <div v-if="loading" class="loading">订单加载中...</div>
    <div v-else-if="filteredTickets.length === 0 && filteredMeals.length === 0" class="empty">暂无订单记录</div>

    <!-- 🎫 车票订单 -->
    <div v-if="filteredTickets.length" class="section">
      <h3>车票订单</h3>
      <ul class="order-list">
        <li v-for="order in filteredTickets" :key="order.id" class="order-card">
          <div class="info">
            <p><strong>车次：</strong>{{ order.train.trainNumber }}</p>
            <p><strong>出发：</strong>{{ order.train.fromStation }}</p>
            <p><strong>到达：</strong>{{ order.train.toStation }}</p>
            <p><strong>出发时间：</strong>{{ formatTime(order.train.departureTime) }}</p>
            <p><strong>状态：</strong>{{ formatStatus(order.status) }}</p>
          </div>
        </li>
      </ul>
    </div>

    <!-- 🍱 订餐订单 -->
    <div v-if="filteredMeals.length" class="section">
      <h3>订餐订单</h3>
      <ul class="order-list">
        <li v-for="meal in filteredMeals" :key="meal.id" class="order-card">
          <div class="info">
            <p><strong>车次：</strong>{{ meal.trainNumber }}</p>
            <p><strong>餐品：</strong>{{ meal.items?.join(', ') }}</p>
            <p><strong>金额：</strong>￥{{ meal.total }}</p>
            <p><strong>状态：</strong>{{ formatStatus(meal.status) }}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const userStore = useUserStore()
const router = useRouter()

const userId = userStore.userInfo?.id

const ticketOrders = ref([])
const mealOrders = ref([])
const loading = ref(false)

const keyword = ref('')
const selectedStatus = ref('')
const dateRange = ref([])

const formatTime = (dt) => new Date(dt).toLocaleString()
const formatStatus = (s) => ({
  COMPLETED: '已完成',
  CANCELLED: '已取消',
  PENDING: '处理中',
  IDLE: '未开始'
}[s] || s)

const fetchOrders = async () => {
  if (!userId) {
    ElMessage.warning('请先登录后查看订单')
    return router.push('/login')
  }
  loading.value = true
  try {
    const [ticketRes, mealRes] = await Promise.all([
      fetch(`/api/user/orders?userId=${userId}`).then(r => r.json()),
      fetch(`/api/user/meal-orders?userId=${userId}`).then(r => r.json())
    ])
    ticketOrders.value = ticketRes || []
    mealOrders.value = mealRes || []
  } catch (e) {
    ElMessage.error('订单加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(fetchOrders)

const filteredTickets = computed(() => {
  return ticketOrders.value.filter(o => {
    const matchKeyword = keyword.value === '' || `${o.train.trainNumber} ${o.train.fromStation} ${o.train.toStation}`.includes(keyword.value)
    const matchStatus = !selectedStatus.value || o.status === selectedStatus.value
    const matchDate = !dateRange.value.length || (new Date(o.train.departureTime) >= new Date(dateRange.value[0]) && new Date(o.train.departureTime) <= new Date(dateRange.value[1]))
    return matchKeyword && matchStatus && matchDate
  })
})

const filteredMeals = computed(() => {
  return mealOrders.value.filter(m => {
    const matchKeyword = keyword.value === '' || `${m.trainNumber} ${m.items?.join(',')}`.includes(keyword.value)
    const matchStatus = !selectedStatus.value || m.status === selectedStatus.value
    return matchKeyword && matchStatus
  })
})
</script>
