// vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8080,   // 默认端口，可不改
    proxy: {
      // 任何以 /api 开头的请求都会被转发到 http://localhost:8080
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 如果后端接口本身没有 /api 前缀，就把它去掉
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
