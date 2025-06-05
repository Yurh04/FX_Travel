<!-- MealOrder.vue -->
<template>
  <div class="meal-page">
    <h2 class="page-title">火车订餐</h2>

    <!-- 🔍 输入车次 -->
    <div class="search-box">
      <input
          v-model="trainId"
          placeholder="请输入已购票车次号"
          @keyup.enter="getMenu"
      />
      <button :disabled="loading" @click="getMenu">
        {{ loading ? '加载中...' : '获取菜单' }}
      </button>
    </div>

    <!-- 📋 菜单展示（单选） -->
    <div v-if="menu.length" class="menu-list">
      <div class="meal-card" v-for="item in menu" :key="item.id">
        <img :src="item.image || defaultImg" alt="meal" />
        <div class="info">
          <h4 class="meal-name">{{ item.name }}</h4>
          <p class="meal-desc">{{ item.description }}</p>
          <p class="price">￥{{ item.price.toFixed(2) }}</p>
          <label class="select-label">
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
import {
  fetchTrainMealList,
  createTrainMealOrder
} from '../api/trainMeal'

// 默认图片
const defaultImg = 'https://cdn-icons-png.flaticon.com/512/2975/2975175.png'

// 状态
const route = useRoute()
const trainId = ref(route.query.trainId || '')
const menu = ref([])
const selectedItem = ref(null)
const loading = ref(false)

// 监听 route 中 trainId 变化
watch(
    () => route.query.trainId,
    (newId) => {
      if (newId) trainId.value = newId
    }
)

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
    menu.value = Array.isArray(result) ? result : []
    if (!menu.value.length) {
      ElMessage.info('该列车暂无可订餐食')
    }
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
  max-width: 900px;
  margin: 40px auto;
  padding: 0 24px;
  background: #fdfdfd;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.page-title {
  text-align: center;
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 24px 0;
}

.search-box {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 32px;
}

.search-box input {
  width: 300px;
  padding: 10px 12px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
  outline: none;
  transition: border-color 0.2s;
}
.search-box input:focus {
  border-color: #409cff;
}

.search-box button {
  background: #409cff;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
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
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.meal-card {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}
.meal-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.meal-card img {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.info {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meal-name {
  font-size: 18px;
  font-weight: 500;
  color: #333;
  margin: 0;
}

.meal-desc {
  font-size: 14px;
  color: #666;
  margin: 0;
  line-height: 1.4;
}

.price {
  font-size: 16px;
  font-weight: 600;
  color: #e53935;
}

.select-label {
  margin-top: 8px;
  font-size: 14px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 6px;
}

.submit-btn {
  display: block;
  margin: 0 auto 32px;
  padding: 12px 28px;
  background: #409cff;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}
.submit-btn:hover {
  background: #1670e0;
}

.tip {
  color: #999;
  text-align: center;
  font-size: 16px;
  margin: 60px 0;
}
</style>
