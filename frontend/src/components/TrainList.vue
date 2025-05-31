<template>
  <div class="train-list">
    <div v-if="trains.length === 0" class="no-result">暂无匹配的车次</div>
    <TrainCard v-for="train in trains" :key="train.trainId" :train="train" />
  </div>
</template>

<script setup>
import TrainCard from './TrainCard.vue'
defineProps({
  trains: Array
})
</script>

<style scoped>
.train-list {
  width: 100%;
}
.no-result {
  text-align: center;
  padding: 24px;
  color: #999;
  font-size: 15px;
}
</style>
