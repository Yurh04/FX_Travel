<!-- src/pages/TrainMealPayment.vue -->
<template>
  <div class="payment-page">
    <div class="payment-card">
      <h2>餐食订单支付</h2>

      <!-- 订单信息展示 -->
      <div class="info">
        <p><strong>订单编号：</strong>{{ orderNumber }}</p>
        <p><strong>车次：</strong>{{ trainInfo.trainNumber }}</p>
        <p><strong>餐品名称：</strong>{{ mealInfo.name }}</p>
        <p><strong>餐品描述：</strong>{{ mealInfo.description }}</p>
        <p><strong>单价：</strong>{{ mealInfo.price }} 元</p>
        <p><strong>数量：</strong>1</p>
        <p v-if="remainingTime > 0" class="time-remaining">
          <strong>剩余支付时间：</strong>
          <span :class="{ 'text-danger': remainingTime < 30 }">
            {{ formatTime(remainingTime) }}
          </span>
        </p>
      </div>

      <hr />

      <div class="payment-actions">
        <p class="total">应付金额：<span>{{ totalPrice }} 元</span></p>
        <div class="buttons">
          <button
              class="pay-btn"
              :disabled="payLoading"
              @click="handlePayment"
          >
            {{ payLoading ? '支付中...' : '立即支付' }}
          </button>
          <button
              class="cancel-btn"
              :disabled="cancelLoading"
              @click="handleCancel"
          >
            {{ cancelLoading ? '取消中...' : '放弃支付' }}
          </button>
        </div>
        <div v-if="statusMessage" class="status-message">
          <span v-if="isProcessing" class="loading-icon">⏳</span>
          {{ statusMessage }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 订单数据
const orderNumber = ref('ML' + Date.now().toString().slice(-8))
const trainInfo = ref({
  trainNumber: route.query.trainNumber || 'G101',
  fromStation: route.query.from || '北京',
  toStation: route.query.to || '上海'
})
const mealInfo = ref({
  mealId: route.query.mealId || '',
  orderNumber: route.query.orderNumber || '',
  name: route.query.mealName || '暂无名称',
  description: route.query.mealDesc || '暂无描述',
  price: route.query.mealPrice || 0
})

// 状态控制
const payLoading = ref(false)
const cancelLoading = ref(false)
const remainingTime = ref(300) // 5分钟支付时限
const statusMessage = ref('')
const isProcessing = ref(false)
let countdownInterval = null

// 生命周期
onMounted(() => {
  startCountdown()
})

onUnmounted(() => {
  stopCountdown()
})

// 倒计时控制
function startCountdown() {
  stopCountdown()
  countdownInterval = setInterval(() => {
    if (remainingTime.value > 0) {
      remainingTime.value--
    } else {
      handlePaymentFailure('支付超时')
    }
  }, 1000)
}

function stopCountdown() {
  if (countdownInterval) {
    clearInterval(countdownInterval)
    countdownInterval = null
  }
}

// 支付处理
async function handlePayment() {
  payLoading.value = true
  statusMessage.value = '正在处理支付...'
  isProcessing.value = true

  try {
    // 这里替换为实际的支付API调用
    // const res = await mealPaymentApi(orderNumber.value, mealInfo.value.id)
    await new Promise(resolve => setTimeout(resolve, 1500)) // 模拟API延迟

    handlePaymentSuccess()
  } catch (error) {
    handlePaymentFailure('支付失败: ' + (error.message || '未知错误'))
  } finally {
    payLoading.value = false
    isProcessing.value = false
  }
}

// 取消处理
async function handleCancel() {
  cancelLoading.value = true
  statusMessage.value = '正在取消订单...'

  try {
    // 这里替换为实际的取消API调用
    // await cancelMealOrder(orderNumber.value)
    await new Promise(resolve => setTimeout(resolve, 800)) // 模拟API延迟

    router.push({ name: 'HotelHome' })
  } catch (error) {
    statusMessage.value = '取消失败: ' + (error.message || '未知错误')
  } finally {
    cancelLoading.value = false
  }
}

// 支付成功处理
function handlePaymentSuccess() {
  stopCountdown()
  statusMessage.value = '支付成功！即将返回...'
  setTimeout(() => {
    router.push({
      name: 'MealOrderSuccess',
      query: {
        orderNumber: orderNumber.value,
        mealName: mealInfo.value.name
      }
    })
  }, 1500)
}

// 支付失败处理
function handlePaymentFailure(message) {
  stopCountdown()
  statusMessage.value = message
  setTimeout(() => {
    router.push({ name: 'HotelHome' })
  }, 2000)
}

// 格式化时间
function formatTime(seconds) {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}分${secs}秒`
}
</script>

<style scoped>
.payment-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background: #f5f7fa;
  padding: 20px;
}

.payment-card {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.info p {
  margin: 10px 0;
  font-size: 15px;
  line-height: 1.5;
}

.time-remaining {
  margin-top: 15px;
  color: #666;
}

.text-danger {
  color: #f56c6c;
  font-weight: bold;
}

.payment-actions {
  margin-top: 25px;
}

.total {
  font-size: 18px;
  margin-bottom: 20px;
  text-align: right;
}

.total span {
  color: #409eff;
  font-weight: bold;
  font-size: 20px;
}

.buttons {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.pay-btn, .cancel-btn {
  flex: 1;
  padding: 12px;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s;
}

.pay-btn {
  background: #409eff;
  color: white;
  border: none;
}

.pay-btn:hover:not(:disabled) {
  background: #3080e0;
}

.pay-btn:disabled {
  background: #a0c9ff;
  cursor: not-allowed;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
  border: 1px solid #ddd;
}

.cancel-btn:hover:not(:disabled) {
  background: #e5e5e5;
}

.cancel-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.status-message {
  margin-top: 15px;
  color: #666;
  font-size: 14px;
  text-align: center;
  min-height: 20px;
}

.loading-icon {
  display: inline-block;
  margin-right: 5px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

hr {
  border: none;
  border-top: 1px solid #eee;
  margin: 20px 0;
}
</style>