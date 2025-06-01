<template>
  <div class="login-notice">
    <div class="notice-text">
      <i class="iconfont">&#xe600;</i>登录
    </div>
    <button class="login-button" @click="goLogin">去登录</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goLogin = () => {
  // 这里可以替换为弹窗方式或跳转到登录页
  router.push('/login')
}
</script>

<style scoped>
.login-notice {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #e6f4ff;
  border: 1px solid #91caff;
  border-radius: 8px;
  padding: 12px 16px;
  font-size: 14px;
  margin-bottom: 20px;
}

.notice-text {
  color: #1677ff;
  display: flex;
  align-items: center;
  gap: 8px;
}

.login-button {
  background-color: #1677ff;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 6px 12px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-button:hover {
  background-color: #0958d9;
}
</style>
