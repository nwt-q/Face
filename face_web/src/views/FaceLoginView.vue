<template>
  <div class="face-login-container">
    <div class="face-login-box">
      <h2>人脸识别登录</h2>
      <div class="camera-section">
        <!-- <div class="camera-container" v-if="!processing">
          <div class="camera-placeholder">
            <span>正在打开摄像头...</span>
          </div>
        </div> -->
        <!-- <button class="control-btn retry-btn" @click="retryCapture" :disabled="loading" v-if="processing || loading">
          {{ loading ? '初始化中...' : '重新识别' }}
        </button> -->

        <div class="camera-container" v-if="!capturedImage">
          <div class="placeholder-container" v-if="!capturedImage">
            <video ref="videoRef" autoplay playsinline></video>
          </div>
        </div>
        <div class="processing-container" v-else>
          <div class="processing-indicator">
            <span>正在识别...</span>
          </div>
          <div>
            <canvas ref="canvasRef" style="display: none;"></canvas> <!-- 隐藏的画布 -->
            <img :src="imageUrl || ''" alt="Canvas 生成图片">
          </div>
        </div>
        <div>
          <canvas ref="canvasRef" style="display: none;"></canvas> <!-- 隐藏的画布 -->
          <img :src="imageUrl || ''" alt="Canvas 生成图片">
        </div>
        <div class="camera-controls">
          <button class="control-btn retry-btn" @click="retryCapture" v-if="processing">
            重新识别
          </button>
          <button class="control-btn back-btn" @click="backToLogin">
            返回登录
          </button>
          <button class="control-btn back-btn" @click="captureImage">
            确定
          </button>
        </div>
      </div>
      <div class="face-login-tips">
        <p>请将面部对准摄像头，保持画面稳定</p>
        <p>系统会自动进行人脸识别</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getface } from '@/api/face'
import { ElMessage } from 'element-plus'
import { onMounted, onBeforeUnmount } from 'vue'
const imageUrl = ref<string | null>(null);

const router = useRouter()
const processing = ref(false)

const capturedImage = ref('')
const videoRef = ref<HTMLVideoElement | null>(null)
let stream: MediaStream | null = null


const retryCapture = () => {
  processing.value = false
  // TODO: 重新开始人脸识别

}

const backToLogin = () => {
  router.push('/login')
}

const recognitionTimeout = ref<number>()




onBeforeUnmount(() => {
  if (stream) {
    stream.getTracks().forEach(track => track.stop())
  }
  clearTimeout(recognitionTimeout.value)
})


// TODO: 在组件挂载时初始化摄像头和人脸识别

const startFaceCapture = async () => {
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
    //     {
    //     "result": [
    //         "66620250419_231907",  # 识别出的人名/ID（对应data/mask目录下的图片名）
    //         [
    //             299,  # 人脸框左上角x坐标
    //             180,  # 人脸框左上角y坐标
    //             482,  # 人脸框右下角x坐标
    //             393   # 人脸框右下角y坐标
    //         ],
    //         0,      # 预留字段（可能用于标识其他信息）
    //         1.0     # 相似度得分（范围0-1，1表示完全匹配）
    //     ]
    // }
    // 将视频流设置到 video 元素
    if (videoRef.value) {

      // 释放 canvas 内存（可选）
      // canvas.remove();
      videoRef.value.srcObject = stream;
    }
  } catch (error) {
    console.error('访问摄像头失败:', error)
    alert('无法访问摄像头，请确保已授予摄像头权限。')
  }
}

const captureImage = () => {
  // 确保视频元素已加载
  if (!videoRef.value) return;

  // 创建 canvas 元素并设置宽高与视频一致
  const canvas = document.createElement('canvas');
  const video = videoRef.value;
  canvas.width = video.videoWidth; // 视频原始宽度
  canvas.height = video.videoHeight; // 视频原始高度

  // 获取画布上下文
  const ctx = canvas.getContext('2d');
  if (!ctx) return;
  setTimeout(() => {
    // 将视频当前帧绘制到 canvas 上
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
  })
  // 将视频当前帧绘制到 canvas 上
  ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

  // 转为图片 URL（支持 PNG/JPEG 格式）
  // imageUrl.value = canvas.toDataURL('image/png'); // PNG 格式
  imageUrl.value = canvas.toDataURL('image/jpeg', 1); // JPEG 格式，质量 80%
  // 将base64图片字符串转换为Blob对象
  console.log("---------------------")
  console.log(imageUrl.value)

  const base64Data = imageUrl.value.split(',')[1];
  const byteCharacters = atob(base64Data);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }

  const byteArray = new Uint8Array(byteNumbers);

  const blob = new Blob([byteArray], { type: 'image/jpeg' });

  // 创建File对象
  const imageFile = new File([blob], 'face.jpg', { type: 'image/jpeg' });

  if (imageFile == null || imageFile == undefined) {
    console.log('图片为空')
    return
  }

  console.log(imageFile)

  const res = getface(imageFile);
  res.then((res) => {
    console.log(res) 
    try {
      if (res.result != null && res.result[3] >= 0.5) {
        console.log('识别成功')
        ElMessage.error('人脸识别成功')
        router.push('/lab-booking')
      } else {
        ElMessage.error('人脸识别失败')
      };
    } catch (error) {
      console.log('识别失败:', error)
      ElMessage.error('人脸识别失败')
    } 
  })
  // console.log(res)
}

// 页面加载开始识别
startFaceCapture()
// 组件生命周期管理
onMounted(startFaceCapture)

</script>

<style scoped>
.face-login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  min-width: 100vw;
  background-color: #f5f5f5;
}

.face-login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.face-login-box h2 {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
}

.camera-section {
  margin-bottom: 2rem;
}

.camera-container,
.processing-container {
  width: 100%;
  height: 300px;
  border: 2px solid #ddd;
  border-radius: 8px;
  margin-bottom: 1rem;
  overflow: hidden;
  position: relative;
}

.camera-placeholder,
.processing-indicator {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.05);
  color: #666;
}

.camera-controls {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 1rem;
}

.control-btn {
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.retry-btn {
  background-color: #2196F3;
  color: white;
}

.retry-btn:hover {
  background-color: #1e88e5;
}

.back-btn {
  background-color: #757575;
  color: white;
}

.back-btn:hover {
  background-color: #616161;
}

.face-login-tips {
  text-align: center;
  color: #666;
}

.face-login-tips p {
  margin: 0.5rem 0;
  font-size: 0.9rem;
}
</style>