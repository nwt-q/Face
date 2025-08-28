import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 导入Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 样式重置表
import "normalize.css";


// 导入Element Plus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 注册Element Plus
app.use(ElementPlus)
app.use(createPinia())

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)

app.mount('#app')
