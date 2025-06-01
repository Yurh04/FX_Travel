<template>
  <div class="auth-page">
    <div class="container">
      <!-- 左侧宣传语 -->
      <div class="left-panel">
        <p class="slogan">风行旅途<br />让出行更轻松</p>
      </div>

      <!-- 右侧卡片区域 -->
      <div class="auth-card">
        <!-- 顶部选项卡 -->
        <div class="tab-switch">
          <button :class="{ active: activeTab === 'login' }" @click="activeTab = 'login'">登录</button>
          <button :class="{ active: activeTab === 'register' }" @click="activeTab = 'register'">注册</button>
          <button :class="{ active: activeTab === 'forget' }" @click="activeTab = 'forget'">忘记密码</button>
        </div>

        <!-- 表单组件 -->
        <AuthForm :type="activeTab" @success="handleSuccess" />

        <!-- 成功提示 -->
        <p v-if="message && !message.includes('失败')" class="message">{{ message }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AuthForm from '../components/AuthForm.vue'

const activeTab = ref('login')
const message = ref('')
const router = useRouter()

const handleSuccess = (msg) => {
  message.value = msg
  if (msg.includes('登录成功')) router.push('/train')
  if (msg.includes('注册成功') || msg.includes('重置')) activeTab.value = 'login'
}
</script>

<style scoped>
.auth-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #fef4ff, #e8f0ff);
  font-family: 'PingFang SC', 'HarmonyOS_Regular', 'Microsoft YaHei', 'Segoe UI', sans-serif;
  padding: 40px;
}

.container {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 960px;
  width: 100%;
  gap: 48px;
}

.left-panel {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding-right: 20px;
}

.slogan {
  font-size: 36px;
  font-weight: 800;
  color: #1a1a1a;
  line-height: 1.6;
  letter-spacing: 1px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.auth-card {
  width: 400px;
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.1);
}

.tab-switch {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.tab-switch button {
  flex: 1;
  background: transparent;
  border: none;
  font-size: 16px;
  font-weight: 600;
  padding: 10px;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.3s;
  border-radius: 6px;
}

.tab-switch button.active {
  border-color: #1677ff;
  color: #1677ff;
  font-weight: bold;
}

.message {
  text-align: center;
  margin-top: 12px;
  font-size: 14px;
  color: #4caf50;
}
</style>
