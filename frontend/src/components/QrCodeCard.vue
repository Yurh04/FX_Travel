<template>
  <div class="qr-card">
    <div class="left">
      <div class="label">支付金额</div>
      <div class="price">¥{{ price }}</div>
    </div>
    <div class="right">
      <div class="label">请扫码完成预订</div>
      <div class="fake-qrcode"></div>
      <div class="note">本页面为演示用途，二维码为模拟展示</div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  price: {
    type: Number,
    default: 120
  }
})
</script>

<style scoped>
.qr-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f5faff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  flex-wrap: wrap;
  gap: 16px;
}

.left .label {
  color: #999;
  font-size: 14px;
}

.left .price {
  font-size: 26px;
  font-weight: bold;
  color: #ff4d4f;
}

.right {
  text-align: center;
}

.right .label {
  font-size: 14px;
  color: #555;
  margin-bottom: 6px;
}

.fake-qrcode {
  width: 100px;
  height: 100px;
  background: repeating-linear-gradient(
      45deg,
      #ccc,
      #ccc 10px,
      #eee 10px,
      #eee 20px
  );
  margin: 0 auto 6px;
  border-radius: 4px;
}

.note {
  font-size: 12px;
  color: #999;
}
</style>
