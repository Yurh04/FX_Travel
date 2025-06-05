<template>
  <div class="passenger-form">
    <h3 class="title">乘客信息</h3>

    <!-- 添加乘客按钮 -->
    <button class="add-btn" @click="addPassenger">
      ➕ 新增乘客
    </button>

    <!-- 添加联系人 -->
    <div class="contact-form">
      <label>添加联系人 <span class="required">*</span></label>
      <div class="contact-input">
        <select v-model="countryCode" class="select">
          <option value="+86">+86</option>
          <option value="+852">+852</option>
          <option value="+1">+1</option>
        </select>
        <input
            v-model="phone"
            type="tel"
            placeholder="请输入手机号"
            class="input"
        />
      </div>
      <label class="checkbox">
        <input type="checkbox" v-model="syncPhone" />
        是否用此手机号接收订单信息
      </label>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const phone = ref('')
const countryCode = ref('+86')
const syncPhone = ref(true)

const addPassenger = () => {
  alert('点击新增乘客按钮，可打开弹窗或跳转添加页')
}
</script>

<style scoped>
.passenger-form {
  background-color: #fff;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06);
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 12px;
}

.add-btn {
  background-color: #1677ff;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 16px;
}

.contact-form label {
  font-size: 14px;
  display: block;
  margin-bottom: 6px;
}

.required {
  color: red;
}

.contact-input {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.select {
  width: 80px;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.input {
  flex: 1;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.checkbox {
  font-size: 13px;
  color: #555;
}
</style>
