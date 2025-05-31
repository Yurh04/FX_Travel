<template>
  <div class="register-page">
    <div class="register-card">
      <h2 class="title">注册账号</h2>
      <input v-model="email" class="input" placeholder="邮箱" />
      <input v-model="username" class="input" placeholder="用户名" />
      <input v-model="password" type="password" class="input" placeholder="密码" />
      <input v-model="confirmPassword" type="password" class="input" placeholder="确认密码" />
      <button class="register-btn" @click="handleRegister">注 册</button>
      <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>
      <p v-if="successMsg" class="success-msg">{{ successMsg }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { register } from '../api/register' // 请确认路径正确

const email = ref('')
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const errorMsg = ref('')
const successMsg = ref('')

const handleRegister = async () => {
  errorMsg.value = ''
  successMsg.value = ''

  if (password.value !== confirmPassword.value) {
    errorMsg.value = '两次密码不一致'
    return
  }

  try {
    const res = await register({
      email: email.value,
      userName: username.value,
      password: password.value,
      gender: 'OTHER',
      role: 'USER'
    })
    successMsg.value = '注册成功，请前往邮箱验证'
  } catch (err) {
    errorMsg.value = err?.response?.data?.error || '注册失败'
  }
}
</script>

<style scoped>
.register-page {
  height: 100vh;
  background: linear-gradient(to bottom, #f0faff, #ffffff);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: Arial;
}
.register-card {
  width: 400px;
  background: white;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  text-align: center;
}
.title {
  margin-bottom: 24px;
  font-size: 22px;
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
.register-btn {
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
.register-btn:hover {
  background: #125ccc;
}
.error-msg {
  color: red;
  font-size: 13px;
  margin-top: 10px;
}
.success-msg {
  color: green;
  font-size: 13px;
  margin-top: 10px;
}
</style>
