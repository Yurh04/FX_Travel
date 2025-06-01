<template>
  <div class="meal-page">
    <h2>火车订餐</h2>

    <!-- 🔍 输入车次 -->
    <div class="search-box">
      <input v-model="trainId" placeholder="请输入已购票车次号" />
      <button :disabled="loading" @click="getMenu">
        {{ loading ? '加载中...' : '获取菜单' }}
      </button>
    </div>

    <!-- 📋 菜单展示（单选） -->
    <div v-if="menu.length" class="menu-list">
      <div class="meal-card" v-for="item in menu" :key="item.id">
        <img :src="item.image || defaultImg" alt="meal" />
        <div class="info">
          <h4>{{ item.name }}</h4>
          <p>{{ item.description }}</p>
          <p class="price">￥{{ item.price }}</p>
          <label>
            <input type="radio" v-model="selectedItem" :value="item" />
            选择
          </label>
        </div>
      </div>
      <button class="submit-btn" @click="submitOrder">提交订餐</button>
    </div>

    <div v-else class="tip">暂无菜单，请先输入车次</div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'
import { fetchTrainMealList, createTrainMealOrder } from '../api/trainMeal'

// 默认图片
const defaultImg = 'https://cdn-icons-png.flaticon.com/512/2975/2975175.png'

// 状态
const route = useRoute()
const trainId = ref(route.query.trainId || '')
const menu = ref([])
const selectedItem = ref(null)
const loading = ref(false)

// 监听 route 中 trainId 变化
watch(() => route.query.trainId, (newId) => {
  if (newId) trainId.value = newId
})

// 登录用户
const userStore = useUserStore()
const userId = computed(() => userStore.user?.id || null)

// 获取菜单
const getMenu = async () => {
  if (!trainId.value) {
    ElMessage.warning('请输入车次号')
    return
  }

  loading.value = true
  try {
    const result = await fetchTrainMealList(trainId.value)
    menu.value = result
    if (!result.length) ElMessage.info('该列车暂无可订餐食')
  } catch (err) {
    ElMessage.error(err.message)
  } finally {
    loading.value = false
  }
}

// 提交订餐
const submitOrder = async () => {
  if (!selectedItem.value) {
    ElMessage.warning('请选择一项餐品')
    return
  }

  if (!userId.value) {
    ElMessage.error('未登录，无法下单')
    return
  }

  try {
    await createTrainMealOrder({
      userId: userId.value,
      ticketReservationId: 10001, // TODO: 实际应从车票信息中传入
      trainMealId: selectedItem.value.id
    })

    ElMessage.success('订餐成功！')
    selectedItem.value = null
  } catch (err) {
    ElMessage.error(err.message)
  }
}
</script>

<style scoped>
.meal-page {
  max-width: 800px;
  margin: auto;
  padding: 24px;
}

.search-box {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.search-box input {
  flex: 1;
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.search-box button {
  background: #2b8cff;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}
.search-box button:disabled {
  background: #a2c8f4;
  cursor: not-allowed;
}
.search-box button:hover:not(:disabled) {
  background: #1670e0;
}

.menu-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.meal-card {
  display: flex;
  gap: 16px;
  background: #f9f9f9;
  border-radius: 12px;
  padding: 14px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.meal-card img {
  width: 120px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.info h4 {
  margin: 0;
  font-size: 16px;
  color: #333;
}
.info p {
  margin: 4px 0;
  font-size: 13px;
}
.price {
  font-weight: bold;
  color: #e53935;
}
.submit-btn {
  margin-top: 20px;
  padding: 10px 20px;
  background: #2b8cff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}
.submit-btn:hover {
  background: #1670e0;
}
.tip {
  color: #999;
  text-align: center;
  margin-top: 40px;
}
</style>
