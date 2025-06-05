// src/api/register.js

import axios from 'axios'

// 创建 axios 实例（带 session 支持）
const request = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true // 必须设置以支持 HttpSession 登录态
})

// -------------------- 🔐 AuthController 接口 --------------------

// 1. 注册账号
export const register = (data) => {
    return request.post('/auth/register', data)
}

// 2. 验证邮箱验证码
export const verifyEmail = (data) => {
    // data: { email: "...", code: "..." }
    return request.post('/auth/verify', data)
}

// 3. 登录
export const login = (data) => {
    // data: { email: "...", password: "..." }
    return request.post('/auth/login', data)
}

// 4. 登出
export const logout = () => {
    return request.post('/auth/logout')
}

// 5. 忘记密码
export const forgotPassword = (email) => {
    return request.post('/auth/forgotPassword', { email })
}

// 6. 通过验证码重置密码
export const resetByVerificationCode = (data) => {
    // data: { email: "...", code: "...", password: "newPassword" }
    return request.post('/auth/resetPassword/ByVerificationCode', data)
}

// 7. 通过旧密码重置密码
export const resetByOldPassword = (data) => {
    // data: { email: "...", oldPassword: "...", newPassword: "..." }
    return request.post('/auth/resetPassword/ByOldPassword', data)
}

// -------------------- 👤 UserController 接口 --------------------

// 1. 获取当前用户信息
export const getCurrentUser = () => {
    return request.get('/user/userdata')
}

// 2. 更新当前用户信息
export const updateUser = (data) => {
    // data: { username: "...", gender: "MALE" | "FEMALE" | "OTHER" }
    return request.put('/user/userdata/update', data)
}

// 3. 授予管理员权限（仅管理员可用）
export const grantAdminRole = (userId) => {
    return request.put('/user/grant', { userId })
}

/// <script setup>
// import { ref } from 'vue'
// import { login } from '@/api/register'
//
// const email = ref('')
// const password = ref('')
//
// const handleLogin = async () => {
//   try {
//     const res = await login({ email: email.value, password: password.value })
//     console.log('登录成功', res)
//   } catch (err) {
//     console.error('登录失败', err.response?.data?.error || '未知错误')
//   }
// }
// </script>