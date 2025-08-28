import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    isAuthenticated: false,
    isAdmin: false,
    userInfo: null as any
  }),

  actions: {
    setAuthenticated(value: boolean) {
      this.isAuthenticated = value
    },

    setAdmin(value: boolean) {
      this.isAdmin = value
    },

    setUserInfo(info: any) {
      this.userInfo = info
    },

    logout() {
      this.isAuthenticated = false
      this.isAdmin = false
      this.userInfo = null
    }
  }
})