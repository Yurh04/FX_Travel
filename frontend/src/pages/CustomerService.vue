<template>
  <div class="contact-background">
    <div class="contact-container">
      <!-- 返回按钮 -->
      <button class="back-btn" @click="goBack">← 返回</button>

      <!-- Logo -->
      <img class="logo" src="/images/ICON.png" alt="风行旅行" />

      <!-- 标题 -->
      <h1>风行旅行 · 客服中心</h1>

      <!-- 咨询方式 -->
      <div class="contact-methods">
        <div class="method-item">
          <span class="icon">📞</span>
          <a href="tel:400-123-4567">400-123-4567</a>
        </div>
        <div class="method-item">
          <span class="icon">✉️</span>
          <a href="mailto:support@fengxing.com">support@fengxing.com</a>
        </div>
        <div class="method-item">
          <span class="icon">💬</span>
          <button class="chat-btn">在线聊天</button>
        </div>
      </div>

      <!-- 留言表单 -->
      <div class="message-form">
        <textarea v-model="message" placeholder="请输入您的问题..." rows="4"></textarea>
        <button class="submit-btn" @click="submitMessage">提交留言</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
const router = useRouter()
const message = ref('')

function goBack() {
  router.back()
}

function submitMessage() {
  if (!message.value.trim()) {
    ElMessage.warning('留言不能为空')
    return
  }
  // TODO: 调用 API 提交留言
  console.log('提交留言：', message.value)
  ElMessage.success('留言已提交，客服会尽快回复')
  message.value = ''
}
</script>

<style scoped>
/* 渐变背景 */
.contact-background {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #67c23a, #409eff);
  padding: 20px;
  box-sizing: border-box;
}

/* 主容器 */
.contact-container {
  position: relative;
  width: 100%;
  max-width: 420px;
  background: rgba(255, 255, 255, 0.85);
  border-radius: 12px;
  padding: 30px 20px;
  box-sizing: border-box;
  text-align: center;
}

/* 返回按钮 */
.back-btn {
  position: absolute;
  top: 20px;
  left: 20px;
  background: transparent;
  color: #333;
  border: none;
  font-size: 0.9em;
  cursor: pointer;
  transition: color 0.2s;
}
.back-btn:hover {
  color: #409eff;
}

/* Logo */
.logo {
  width: 100px;
  margin-bottom: 10px;
}

/* 标题 */
h1 {
  font-size: 1.6em;
  margin: 10px 0 25px;
  color: #333;
  letter-spacing: 1px;
}

/* 联系方式区块 */
.contact-methods {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
  margin-bottom: 30px;
}
.method-item {
  flex: 1 1 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 1.05em;
  background: rgba(255, 255, 255, 0.7);
  padding: 10px;
  border-radius: 8px;
  transition: background 0.2s;
}
.method-item:hover {
  background: rgba(255, 255, 255, 0.9);
}
.method-item .icon {
  font-size: 1.4em;
}
.contact-methods a,
.chat-btn {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

/* 在线聊天按钮 */
.chat-btn {
  background: #409eff;
  color: #fff;
  border: none;
  padding: 6px 14px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s, transform 0.1s;
}
.chat-btn:hover {
  background: #3a8ee6;
  transform: translateY(-1px);
}

/* 留言表单 */
.message-form textarea {
  width: 100%;
  padding: 12px;
  font-size: 1em;
  border: 1px solid #ccc;
  border-radius: 6px;
  resize: none;
  box-sizing: border-box;
  margin-bottom: 15px;
  transition: border 0.2s;
}
.message-form textarea:focus {
  border-color: #409eff;
  outline: none;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  background: #67c23a;
  color: #fff;
  border: none;
  padding: 12px 0;
  font-size: 1.1em;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s, transform 0.1s;
}
.submit-btn:hover {
  background: #57a337;
  transform: translateY(-1px);
}

/* 响应式调整 */
@media (max-width: 480px) {
  .contact-container {
    padding: 20px 15px;
  }
  .contact-methods {
    gap: 10px;
  }
  .method-item {
    flex: 1 1 100%;
  }
}
</style>
