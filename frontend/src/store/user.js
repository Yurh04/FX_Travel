// src/store/user.js
import {defineStore} from 'pinia'
import {getCurrentUser as apiGetCurrentUser, login as apiLogin, logout as apiLogout,} from '../api/register'

export const useUserStore = defineStore('user', {
    state: () => ({
        // 登录状态
        isLoggedIn: false,
        // 当前用户信息（从后端 /user/userdata 拉取）
        userInfo: {
            id: null,
            email: '',
            username: '',
            verified: false,
            gender: '',
            role: ''
        }
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
                await this.fetchCurrentUser()
                console.log("login success")
                return true
            } catch (err) {
                // 捕获后端返回的错误信息
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
                console.log("logout success")
            } catch (err) {
                // 即使后端出错，也继续清除本地状态
                console.warn('后端登出失败，但本地状态仍会清空：', err)
            }
            this.isLoggedIn = false
            this.userInfo = null
        },

        /**
         * 3. 刷新/初始化时调用：尝试拉取当前用户信息，若成功则标记已登录，否则重置状态
         */
        async fetchCurrentUser() {
            try {
                this.userInfo = (await apiGetCurrentUser()).data
                this.isLoggedIn = true
                console.log("fetchCurrentUser")
                console.log(this.userInfo)
            } catch (err) {
                // 取不到用户信息，就认为未登录
                this.isLoggedIn = false
                this.userInfo = null
            }
        },
    },

    getters: {
        // 获取当前登录状态
        loggedIn: (state) => state.isLoggedIn,

        // 获取当前用户信息
        currentUser: (state) => state.userInfo,

        // 获取用户名（假设userInfo中有username字段）
        username: (state) => state.userInfo?.username || '',

        // 获取用户邮箱（假设userInfo中有email字段）
        email: (state) => state.userInfo?.email || '',
    },
})
