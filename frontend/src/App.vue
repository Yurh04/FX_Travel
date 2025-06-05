<template>
  <router-view />
</template>

<script setup>
// App.vue
import { onMounted } from 'vue'
import { useUserStore } from './store/user.js'

onMounted(() => {
  useUserStore().restoreSession()
})

</script>

<style scoped>
nav {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin: 20px 0;
  font-size: 18px;
}
</style>
