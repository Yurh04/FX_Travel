<template>
  <div class="login-page">
    <div class="overlay">
      <div class="login-box">
        <h2>用户登录</h2>
        <input v-model="username" placeholder="用户名" />
        <input v-model="password" type="password" placeholder="密码" />
        <button @click="handleLogin">登录</button>
        <p v-if="error" class="error">{{ error }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const error = ref('')

const handleLogin = () => {
  if (!username.value || !password.value) {
    error.value = '用户名或密码不能为空'
    return
  }

  if (username.value === 'user' && password.value === '123456') {
    localStorage.setItem('user', JSON.stringify({ username: username.value }))
    router.push('/')
  } else {
    error.value = '账号或密码错误'
  }
}
</script>

<style scoped>
.login-page {
  background-image: url('https://pic.616pic.com/bg_w1180/00/13/22/ZX0SfqX169.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 100vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay {
  background-color: rgba(0, 0, 0, 0.4); /* 背景图加暗 */
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  background: white;
  padding: 30px;
  width: 320px;
  border-radius: 12px;
  box-shadow: 0 2px 24px rgba(0, 0, 0, 0.4);
  text-align: center;
}

.login-box input {
  width: calc(100% - 0px); /* ✅ 可省略 padding */
  margin-bottom: 16px;
  padding: 12px 14px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 16px;
  box-sizing: border-box;
}


.login-box button {
  width: 100%;
  padding: 10px;
  background: #2b8cff;
  border: none;
  color: white;
  font-weight: bold;
  border-radius: 6px;
  cursor: pointer;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>
