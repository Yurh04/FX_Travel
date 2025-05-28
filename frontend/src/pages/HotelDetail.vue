<template>
  <div class="hotel-detail">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click.native="goHome">首页</el-breadcrumb-item>
      <el-breadcrumb-item>酒店详情</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="detail-card" v-if="hotel">
      <el-row :gutter="20">
        <el-col :span="12">
          <img :src="hotel.img" class="detail-img" />
        </el-col>
        <el-col :span="12">
          <h2>{{ hotel.name }}</h2>
          <div class="stars">
            <i v-for="i in hotel.stars" :key="i" class="iconfont">&#xe60a;</i>
            <span>{{ hotel.stars }}星</span>
          </div>
          <p class="address">{{ hotel.address }}</p>
          <p class="price">￥{{ hotel.price }} 起</p>
          <el-button type="primary" @click="goBooking">立即预订</el-button>
        </el-col>
      </el-row>
      <div class="description">
        <h3>酒店简介</h3>
        <p>{{ hotel.description }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchHotelDetail } from '../api/hotel'

const route = useRoute()
const router = useRouter()
const hotel = ref(null)

const goHome = () => router.push('/')
const goBooking = () => router.push({ name: 'BookingForm', params: { bookingId: null, hotelId: hotel.value.id } })

onMounted(async () => {
  const id = route.params.id
  const res = await fetchHotelDetail(id)
  hotel.value = res.data || res
})
</script>

<style scoped>
.hotel-detail { padding: 20px; }
.detail-card { max-width: 900px; margin: 0 auto; }
.detail-img { width: 100%; border-radius: 8px; }
.stars { color: #fbbf24; margin: 10px 0; }
.address { color: #666; margin-bottom: 10px; }
.price { font-size: 24px; color: #e91e63; margin-bottom: 20px; }
.description { margin-top: 24px; }
</style>