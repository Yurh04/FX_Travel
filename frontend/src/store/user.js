// src/store/user.js
import { defineStore } from 'pinia'
import {
    login as apiLogin,
    logout as apiLogout,
    getCurrentUser as apiGetCurrentUser,
} from '../api/register'

export const useUserStore = defineStore('user', {
    state: () => ({
        // 登录状态
        isLoggedIn: false,
        // 当前用户信息（从后端 /user/userdata 拉取）
        userInfo: null,
        // 可选：保存登录过程中出现的错误消息
        errorMsg: '',
    }),

    actions: {
        /**
         * 1. 登录：调用后端 /auth/login 接口，登录成功后拉取当前用户信息
         * @param {Object} payload { email: string, password: string }
         */
        async login(payload) {
            try {
                // 调用后端 /auth/login（HttpSession + Cookie）
                await apiLogin({
                    email: payload.email,
                    password: payload.password,
                })

                // 登录成功后，从后端 /user/userdata 获取用户信息
                const res = await apiGetCurrentUser()
                // 假设后端返回格式为 { code: 0, data: { id, name, ... } }
                this.userInfo = res.data
                this.isLoggedIn = true
                this.errorMsg = ''
                return true
            } catch (err) {
                // 捕获后端返回的错误信息
                this.errorMsg = err.response?.data?.error || '登录失败'
                this.isLoggedIn = false
                this.userInfo = null
                return false
            }
        },

        /**
         * 2. 退出登录：调用后端 /auth/logout，然后清空本地状态
         */
        async logout() {
            try {
                await apiLogout()
            } catch (err) {
                // 即使后端出错，也继续清除本地状态
                console.warn('后端登出失败，但本地状态仍会清空：', err)
            }
            this.isLoggedIn = false
            this.userInfo = null
            this.errorMsg = ''
        },

        /**
         * 3. 刷新/初始化时调用：尝试拉取当前用户信息，若成功则标记已登录，否则重置状态
         */
        async fetchCurrentUser() {
            try {
                const res = await apiGetCurrentUser()
                this.userInfo = res.data
                this.isLoggedIn = true
                this.errorMsg = ''
            } catch (err) {
                // 取不到用户信息，就认为未登录
                this.isLoggedIn = false
                this.userInfo = null
            }
        },
    },

    getters: {
        // 方便在组件里直接取用户名
        username: (state) => (state.userInfo ? state.userInfo.name : ''),
    },
})
