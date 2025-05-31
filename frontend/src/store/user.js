import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        userInfo: null,
        isLoggedIn: false
    }),
    actions: {
        login(user) {
            this.userInfo = user
            this.isLoggedIn = true
            localStorage.setItem('user', JSON.stringify(user))
        },
        logout() {
            this.userInfo = null
            this.isLoggedIn = false
            localStorage.removeItem('user')
        },
        restoreSession() {
            const saved = localStorage.getItem('user')
            if (saved) {
                this.userInfo = JSON.parse(saved)
                this.isLoggedIn = true
            }
        }
    }
})
