<!-- 文件：src/components/LoginNotice.vue -->
<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { storeToRefs } from 'pinia' // 新增

const router = useRouter()
const userStore = useUserStore()

// 使用 storeToRefs 解构保持响应性
const { loggedIn, username } = storeToRefs(userStore)

const goAuth = () => {
  router.push({ name: 'Authentication' })
}

const handleLogout = async () => {
  try {
    await userStore.logout()
  } catch (error) {
    console.error('登出失败:', error)
  }
}
</script>

<template>
  <div class="user-auth-container">
    <template v-if="!loggedIn">
      <button class="btn-login" @click="goAuth">登录 / 注册</button>
    </template>
    <template v-else>
      <div class="welcome-user" @click="handleLogout" title="点击退出登录">
        <span class="welcome-text">欢迎，</span>
        <span class="username">{{ username }}</span>
        <span class="logout-hint">(退出)</span>
      </div>
    </template>
  </div>
</template>

<style scoped>
.user-auth-container {
  display: inline-block;
}

.btn-login {
  padding: 8px 24px;
  font-size: 14px;
  font-weight: 500;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-login:hover {
  background-color: #3a8ee6;
}

.welcome-user {
  padding: 8px 16px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.welcome-user:hover {
  background-color: #f5f5f5;
}

.welcome-text {
  color: #666;
}

.username {
  font-weight: 500;
  color: #409eff;
}

.logout-hint {
  font-size: 12px;
  color: #999;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.welcome-user:hover .logout-hint {
  opacity: 1;
}
</style>