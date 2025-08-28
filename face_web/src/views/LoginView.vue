<template>
  <div class="login-container">
    <div class="login-box">
      <h2>登录</h2>
      <div class="login-form">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
            type="text"
            id="username"
            v-model="username"
            placeholder="请输入用户名"
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="password"
            placeholder="请输入密码"
          />
        </div>
        <div>
          <el-checkbox v-model="isAdminLogin">管理员登录</el-checkbox>
        </div>

        <div class="login-buttons">
          <button class="login-btn" @click="handleLogin">
            {{ isAdminLogin ? '管理员登录' : '用户登录' }}
          </button>
        </div>
        <div class="login-buttons">
          <button class="login-btn" @click="handleLogin">登录</button>
            <button class="face-login-btn" @click="handleFaceLogin">
              人脸识别登录
            </button>
        </div>
    
        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

// import admin from 'back/auth'

const router = useRouter()
const username = ref('')
const password = ref('')
const isAdminLogin = ref(false)

const handleLogin = () => {
  // TODO: 实现账号密码登录逻辑
  console.log('登录', username.value, password.value)
  console.log('登录身份:', isAdminLogin.value ? '管理员' : '普通用户')
  // 修改跳转逻辑
  if(isAdminLogin.value) {
    // 跳转到管理员页面
    if(username.value !== 'admin' || password.value !== 'admin') {
      ElMessage.error('用户名或密码错误')
      return ;
    }
    router.push('/admin')
  } else {
    // 跳转到实验室预约页面
    if(username.value !== 'EvLast' || password.value !== '123456') {
      ElMessage.error('用户名或密码错误')
      return ;
    }
    router.push('/lab-booking')
  }
}

const handleFaceLogin = () => {
  // TODO: 跳转到人脸识别登录页面
  router.push('/face-login')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  min-width: 100vw;
  background-color: #f5f5f5;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  width: 100vw;
  max-width: 400px;
}

.login-box h2 {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
}

input {
  width: 95%;
  padding: 0.6rem;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #4CAF50;
}

.login-buttons {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.login-btn,
.face-login-btn {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn {
  background-color: #4CAF50;
  color: white;
}

.login-btn:hover {
  background-color: #45a049;
}

.face-login-btn {
  background-color: #2196F3;
  color: white;
}

.face-login-btn:hover {
  background-color: #1e88e5;
}

.register-link {
  text-align: center;
  color: #666;
}

.register-link a {
  color: #4CAF50;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>