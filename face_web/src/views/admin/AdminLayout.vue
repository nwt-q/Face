<template>
  <el-container class="admin-layout">
   
    <el-aside width="200px">
       <!-- 这里加一个图标 -->
       <div class="sidebar-header">
        <el-icon class="system-icon"><HomeFilled /></el-icon>
        <span class="system-title">实验室管理系统</span>
      </div>
      <!-- <el-menu :default-active="currentPath" class="admin-menu" @select="handleMenuSelect" background-color="#304156"
        text-color="#fff" active-text-color="#409EFF">
      </el-menu> -->
      <el-menu :default-active="currentPath" class="admin-menu" @select="handleMenuSelect" background-color="#304156"
        text-color="#fff" active-text-color="#409EFF">
        <el-menu-item-group title="系统管理">
          <el-menu-item index="/admin/attendance">
            <el-icon>
              <List />
            </el-icon>
            <span>考勤记录管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <el-icon>
              <User />
            </el-icon>
            用户管理
          </el-menu-item>
        </el-menu-item-group>

        <el-menu-item-group title="实验室管理">

          <el-menu-item index="/admin/labs">
            <el-icon>
              <House />
            </el-icon>
            <span>实验室管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/equipment">
            <el-icon>
              <Plus />
            </el-icon>
            <span> 设备管理 </span>
          </el-menu-item>
          <el-menu-item index="/admin/bookings">
            <el-icon>
              <Calendar />
            </el-icon>
            <span>预约记录管理</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header height="60px">
        <div class="header-left">
          <h2>实验室管理系统</h2>
        </div>

        <div class="header-right">
          <el-dropdown>
            <div class="admin-dropdown">
              管理员
              <el-icon>
                <CaretBottom />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'


const router = useRouter()
const route = useRoute()

const currentPath = computed(() => route.path)

const handleMenuSelect = (index: string) => {
  router.push(index)
}

const handleLogout = () => {
  // TODO: 实现退出登录逻辑
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #304156;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);


}

.system-icon {
  font-size: 24px;
  color: #409EFF;
  margin-right: 8px;
}

.system-title {
  font-size: 16px;
  font-weight: 500;
  color: #fff;
}

.admin-layout {
  height: 100vh;
  min-width: 100%;
}

.el-aside {
  background-color: #304156;
}

.admin-menu {
  height: 100%;
  border-right: none;
}

.admin-menu :deep(.el-menu-item-group__title) {
  padding: 15px 0 5px 20px;
  color: #a8abb2;
  font-size: 12px;
}

.admin-menu :deep(.el-menu-item) {
  height: 48px;
  line-height: 48px;
  margin: 4px 0;
  padding-left: 20px !important;
}

.admin-menu :deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.1);
}

.admin-menu :deep(.el-menu-item.is-active) {
  background-color: rgba(64, 158, 255, 0.1);
}

.admin-menu :deep(.el-icon) {
  margin-right: 12px;
  font-size: 18px;
}

.admin-layout {
  height: 100vh;
  min-width: 100%;
}

.el-main{
  height: 100%;
  width: 100%;
  min-width: 100vw;
  min-height: 100vh;
  overflow: auto; /* 允许主内容区域滚动 */

}

.el-aside {
  background-color: #304156;
  overflow: hidden; /* 防止内容溢出导致滚动 */
}

.admin-menu {
  height: 100%;
  border-right: none;
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  width: 100% !important;
}

.header-left h2 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  justify-content: flex-end; /* 将元素靠右对齐 */
  padding-right: 20px; /* 添加右侧内边距 */
}

.admin-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #333;
  margin-left: 10px; /* 添加左侧间距 */
}

.admin-dropdown .el-icon {
  margin-left: 5px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>