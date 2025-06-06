<!-- 文件：src/components/LoginNotice.vue -->
<template>
  <button class="login-btn" @click="$emit('login')">
    登录
  </button>
</template>

<script setup>
</script>

<style scoped>
.login-btn {
  background: linear-gradient(to right, #409eff, #66b1ff);
  color: #fff;
  border: none;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
}
.login-btn:hover {
  background: linear-gradient(to right, #3a8ee6, #5caceb);
}
</style>
