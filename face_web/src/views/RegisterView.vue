<template>
  <div class="register-container">
    <div class="register-box">
      <h2>用户注册</h2>
      <div class="register-form">
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
        <div class="form-group">
          <label for="confirmPassword">确认密码</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            placeholder="请再次输入密码"
          />
        </div>
        <div class="form-group">
          <label for="email">邮箱</label>
          <input
            type="email"
            id="email"
            v-model="email"
            placeholder="请输入邮箱"
          />
        </div>
        <div class="face-capture">
          <h3>人脸采集</h3>
          <div class="camera-container" v-if="!capturedImage">
            <div class="camera-container" v-if="!capturedImage">
              <video ref="videoRef" autoplay playsinline></video>
            </div>
          </div>
          <div class="preview-container" v-else>
            <img :src="capturedImage" alt="captured face" />
          </div>
          <button class="capture-btn" @click="startFaceCapture">
            {{ capturedImage ? '重新采集' : '开始采集' }}
          </button>
        </div>
        <button class="register-btn" @click="handleRegister">注册</button>
        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const email = ref('')
const capturedImage = ref('')
const videoRef = ref<HTMLVideoElement | null>(null)
let stream: MediaStream | null = null

const startFaceCapture = async ()=> {
  // TODO: 实现人脸采集逻辑
  console.log('开始人脸采集')
  try {
    if (capturedImage.value) {
      // 如果已经有捕获的图像，则重置
      capturedImage.value = ''
      if (stream) {
        stream.getTracks().forEach(track => track.stop())
        stream = null
      }
      return
    }

    // 请求摄像头权限并获取视频流
    stream = await navigator.mediaDevices.getUserMedia({
      video: {
        width: 640,
        height: 480,
        facingMode: 'user' // 使用前置摄像头
      }
    })

    // 将视频流设置到 video 元素
    if (videoRef.value) {
      videoRef.value.srcObject = stream
    }

    // 3秒后自动拍照
    setTimeout(() => {
      if (videoRef.value && stream) {
        // 创建 canvas 元素
        const canvas = document.createElement('canvas')
        canvas.width = videoRef.value.videoWidth
        canvas.height = videoRef.value.videoHeight
        
        // 将视频帧绘制到 canvas
        const ctx = canvas.getContext('2d')
        if (ctx) {
          ctx.drawImage(videoRef.value, 0, 0, canvas.width, canvas.height)
          
          // 将 canvas 转换为图片 URL
          capturedImage.value = canvas.toDataURL('image/jpeg')

          // 发送 capturedImage 到后端
          // 例如，使用 axios 发送 POST 请求
          
          // 停止视频流
          stream?.getTracks().forEach(track => track.stop())

          stream = null
        }
      }
    }, 3000)
  } catch (error) {
    console.error('访问摄像头失败:', error)
    alert('无法访问摄像头，请确保已授予摄像头权限。')
  }
}

const handleRegister = () => {
  // TODO: 实现注册逻辑
  console.log('注册', {
    username: username.value,
    password: password.value,
    email: email.value,
    capturedImage: capturedImage.value
  })
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  padding: 2rem 0;
}

.register-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.register-box h2 {
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
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #4CAF50;
}

.face-capture {
  margin: 2rem 0;
  text-align: center;
}

.face-capture h3 {
  color: #333;
  margin-bottom: 1rem;
}

.camera-container,
.preview-container {
  width: 100%;
  height: 200px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  margin-bottom: 1rem;
  overflow: hidden;
}

.camera-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

.preview-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.capture-btn {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  margin-bottom: 1rem;
}

.capture-btn:hover {
  background-color: #1e88e5;
}

.register-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-bottom: 1rem;
}

.register-btn:hover {
  background-color: #45a049;
}

.login-link {
  text-align: center;
  color: #666;
}

.login-link a {
  color: #4CAF50;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>