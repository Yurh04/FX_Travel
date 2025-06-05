<!-- 文件：src/components/LoginNotice.vue -->
<template>
  <button class="btn-login" @click="goAuth">登录 / 注册</button>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goAuth = () => {
  router.push({ name: 'Authentication' })
}
</script>

<style scoped>
.btn-login {
  padding: 8px 24px;
  font-size: 14px;
  font-weight: 500;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.2s ease;
}
.btn-login:hover {
  background-color: #3a8ee6;
}
</style>
