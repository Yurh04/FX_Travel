<template>
  <div class="homepage">
    <h2>旅行·拍</h2>

    <!-- 主功能卡片 -->
    <div class="module-grid">
      <div
          class="module-card"
          v-for="item in modules"
          :key="item.title"
          @click="go(item.path)"
      >
        <img :src="item.img" alt="" />
        <p>{{ item.title }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const go = (path) => router.push(path)

const modules = [
  {
    title: '预订火车票',
    path: '/booking',
    img: 'https://cdn-icons-png.flaticon.com/512/3721/3721814.png'
  },
  {
    title: '历史订单',
    path: '/orders',
    img: 'https://cdn-icons-png.flaticon.com/512/3050/3050525.png'
  },
  {
    title: '火车订餐',
    path: '/meal',
    img: 'https://cdn-icons-png.flaticon.com/512/5973/5973827.png'
  },
  {
    title: '用户登录',
    path: '/login',
    img: 'https://cdn-icons-png.flaticon.com/512/1077/1077063.png'
  }
]
</script>

<style scoped>
.homepage {
  max-width: 960px;
  margin: auto;
  padding: 40px 20px;
  text-align: center;
}
h2 {
  font-weight: bold;
  margin-bottom: 32px;
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 28px;
}

.module-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 18px rgba(0, 0, 0, 0.08);
  padding: 20px;
  cursor: pointer;
  transition: 0.3s ease;
}
.module-card:hover {
  transform: scale(1.06);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.15);
}
.module-card img {
  width: 80px;
  height: 80px;
  margin-bottom: 12px;
}
.module-card p {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
</style>
