<template>
  <div class="train-list">
    <transition name="fade" mode="out-in">
      <div v-if="trains.length === 0" class="no-result">
        🚄 暂无匹配的车次
      </div>
    </transition>

    <div class="train-list-wrapper">
      <TrainCard
          v-for="train in trains"
          :key="train.trainId"
          :train="train"
          class="train-item"
      />
    </div>
  </div>
</template>

<script setup>
import TrainCard from './TrainCard.vue'

defineProps({
  trains: {
    type: Array,
    default: () => []
  }
})
</script>

<style scoped>
.train-list {
  width: 100%;
}

.train-list-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.train-item {
  transition: transform 0.2s ease;
}
.train-item:hover {
  transform: translateY(-2px);
}

.no-result {
  text-align: center;
  padding: 48px 16px;
  color: #999;
  font-size: 16px;
  background-color: #fdfdfd;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
  margin-top: 16px;
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.4s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
