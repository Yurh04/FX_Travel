<template>
  <div class="booking-form-page">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item @click.native="goHome">首页</el-breadcrumb-item>
      <el-breadcrumb-item>填写订单</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card class="booking-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="酒店名称">
          <span>{{ hotel.name }}</span>
        </el-form-item>
        <el-form-item label="房型">
          <el-select v-model="form.roomTypeId" placeholder="请选择房型">
            <el-option
              v-for="rt in hotel.roomTypes"
              :key="rt.roomTypeId"
              :label="rt.name"
              :value="rt.roomTypeId" />
          </el-select>
        </el-form-item>
        <el-form-item label="入住 - 离店" prop="dates">
          <el-date-picker
            v-model="form.dates"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="入住人姓名" prop="guestName">
          <el-input v-model="form.guestName" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitBooking">提交订单</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import { fetchHotelDetail, createBooking } from '../api/hotel'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const hotel = reactive({ name: '', roomTypes: [] })

const form = reactive({
  roomTypeId: '',
  dates: [],
  guestName: '',
  phone: ''
})
const rules = {
  roomTypeId: [{ required: true, message: '请选择房型', trigger: 'change' }],
  dates: [{ type: 'array', required: true, message: '请选择日期范围', trigger: 'change' }],
  guestName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const goHome = () => router.push('/')

onMounted(async () => {
  // 编辑或新建
  const id = route.query.hotelId || route.params.bookingId && (await getBooking()).hotelId
  const res = await fetchHotelDetail(id)
  Object.assign(hotel, res.data || res)
})

const submitBooking = () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    const [checkIn, checkOut] = form.dates
    const payload = {
      userId: 'U1001',
      hotelId: route.query.hotelId || hotel.id,
      roomTypeId: form.roomTypeId,
      checkInDate: checkIn,
      checkOutDate: checkOut
    }
    await createBooking(payload)
    router.push('/orders')
  })
}
</script>

<style scoped>
.booking-form-page { padding: 20px; }
.booking-card { max-width: 600px; margin: 0 auto; }
</style>
