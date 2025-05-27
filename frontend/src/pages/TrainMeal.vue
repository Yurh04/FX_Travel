<template>
  <div class="meal-page">
    <h2>火车订餐</h2>

    <!-- 🔍 输入车次 -->
    <div class="search-box">
      <input v-model="trainId" placeholder="请输入已购票车次号" />
      <button @click="getMenu">获取菜单</button>
    </div>

    <!-- 📋 菜单展示 -->
    <div v-if="menu.length" class="menu-list">
      <div class="meal-card" v-for="item in menu" :key="item.name">
        <img :src="item.img" alt="meal" />
        <div class="info">
          <h4>{{ item.name }}</h4>
          <p>{{ item.description }}</p>
          <p class="price">￥{{ item.price }}</p>
          <label>
            <input type="checkbox" v-model="selectedItems" :value="item.name" />
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
import { ref } from 'vue'
import axios from 'axios'

const trainId = ref('')
const selectedItems = ref([])
const menu = ref([])

// 示例菜单数据（可替换为后端接口）
const exampleMenu = [
  {
    name: '牛肉饭套餐',
    price: 38,
    img: '',
    description: '卤牛肉、米饭、凉拌黄瓜、饮料'
  },
  {
    name: '鸡腿饭套餐',
    price: 32,
    img: '',
    description: '烤鸡腿、米饭、小菜、矿泉水'
  },
  {
    name: '素食便当',
    price: 28,
    img: '',
    description: '素炒三样、豆腐、饭、汤'
  }
]

const getMenu = async () => {
  if (!trainId.value) {
    alert('请输入车次号')
    return
  }

  // ✅ 示例：模拟接口返回菜单
  menu.value = exampleMenu

  // 若使用接口请替换：
  // const res = await axios.get('/api/v1/train/meal/menu', { params: { trainId: trainId.value } })
  // menu.value = res.data
}

const submitOrder = async () => {
  if (selectedItems.value.length === 0) {
    alert('请选择至少一项餐品')
    return
  }

  const res = await axios.post('http://localhost:8080/api/v1/train/meal/order', {
    userId: 'u001',
    trainId: trainId.value,
    items: selectedItems.value
  })

  alert(res.data.message || '订餐成功')
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
}
.tip {
  color: #999;
  text-align: center;
  margin-top: 40px;
}
</style>
