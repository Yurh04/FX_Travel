<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="title">账号密码登录</h2>
      <input v-model="username" class="input" placeholder="手机号/用户名/邮箱" />
      <input v-model="password" type="password" class="input" placeholder="登录密码" />
      <button class="login-btn" @click="handleLogin">登 录</button>
      <div class="link-row">
        <a href="#">忘记密码</a>
        <a href="#">免费注册</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '../store/user.js'

const userStore = useUserStore()

const handleLogin = async () => {
  try {
    const res = await login({ email: username.value, password: password.value })
    userStore.login(res) // 存入状态和 localStorage
    router.push('/train')
  } catch (err) {
    errorMsg.value = err?.response?.data?.error || '登录失败'
  }
}

</script>

<style scoped>
.login-page {
  height: 100vh;
  background: linear-gradient(to bottom, #e6f0fc, #ffffff);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: Arial;
}
.login-card {
  width: 360px;
  background: white;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  text-align: center;
}
.title {
  margin-bottom: 24px;
  font-size: 20px;
  font-weight: bold;
  color: #333;
}
.input {
  width: 100%;
  padding: 10px 14px;
  margin-bottom: 14px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
}
.login-btn {
  width: 100%;
  background: #1677ff;
  color: white;
  font-size: 16px;
  padding: 10px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: 0.2s;
}
.login-btn:hover {
  background: #125ccc;
}
.link-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  margin-top: 14px;
  color: #666;
}
.link-row a {
  color: #409eff;
  text-decoration: none;
}
.link-row a:hover {
  text-decoration: underline;
}
</style>
