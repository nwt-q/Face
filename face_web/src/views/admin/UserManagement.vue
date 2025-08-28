<template>
  <div class="user-management">
    <div class="page-header">
      <h3>用户管理</h3>
      <el-button type="primary" @click="handleAddUser">
        <el-icon><Plus /></el-icon>添加用户
      </el-button>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <!-- <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="active" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table 
    :data="userList" 
    style="width: 100%"
    :row-class-name="checkStatus" 
  >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="real_name" label="姓名" />
        <el-table-column prop="password" label="密码" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />

         <el-table-column prop="status" label="状态">
      <template #default="{ row }">
        <el-tag 
          v-if="row.status !== 'x'" 
          :type="row.status === 'active' ? 'success' : 'danger'"
        >
          {{ row.status === 'active' ? '正常' : '禁用' }}
        </el-tag>
        <span v-else>已隐藏</span>  <!-- 隐藏状态提示 -->
      </template>
    </el-table-column>

        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button-group>
              <el-button type="primary" size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加用户' : '编辑用户'"
      width="500px"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" />
        </el-form-item>
        <el-form-item label="姓名" prop="real_name">
          <el-input v-model="userForm.real_name" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="userForm.status" placeholder="请选择状态">
            <el-option label="正常" value="active" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { LabService } from '@/services/labService'

// 搜索表单
const searchForm = reactive({
  username: '',
  status: ''
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 用户列表数据
const userList = ref([
  {
    id: 1,
    username: 'admin',
    active: '管理员',
    email: 'admin@example.com',
    phone: '13800138000',
    status: 'active',
    createTime: '2024-01-20 10:00:00'
  },
])

// 表单相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const userFormRef = ref<FormInstance>()
const userForm = reactive({
  username: '',
  real_name: '',
  email: '',
  phone: '',
  status: 'active'
})

// 表单验证规则
const userFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

onMounted(() => {
  initData() 
})

// 初始化数据
const initData = () => {

   LabService.getUsers().then((res: any) => {
    userList.value = res
    // for(let i = 0; i < res.length; i++) {
    //   console.log(userList.value[i].real_name)
    // }
    // total.value = res.data.length
  })
}

// 搜索处理
const handleSearch = () => {
  // TODO: 实现搜索逻辑
  console.log('搜索条件：', searchForm)
}

const resetSearch = () => {
  searchForm.username = ''
  searchForm.status = ''
  handleSearch()
}


// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  handleSearch()
}

// 添加用户
const handleAddUser = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  // 重置表单
  Object.assign(userForm, {
    username: '',
    name: '',
    email: '',
    phone: '',
    status: 'active'
  })
  LabService.addUser(userForm).then((res: any) => {
    console.log(res)

  })
}

// 编辑用户
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  // 填充表单数据
  LabService.updateUser(row).then((res: any) => {
    console.log(res) 
  })
  Object.assign(userForm, row)
}

// 删除用户
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    '确认要删除该用户吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    // TODO: 实现删除逻辑
    row.status = 'x'
    LabService.updateUser(row).then((res: any) => {
    console.log(res) 
    initData()
  })
    ElMessage.success('删除成功')
  }).catch(() => {
    // 取消删除

  })
}
// 在 setup 中添加
const checkStatus = ( row : any) => {
  return row.status === 'x' ? 'hide-row' : ''
}

// 提交表单
const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 实现提交逻辑
      console.log('表单数据：', userForm)
      ElMessage.success(dialogType.value === 'add' ? '添加成功' : '编辑成功')
      dialogVisible.value = false
    }
  })
}
</script>

<style scoped>
:deep(.el-table .hide-row) {
  display: none;
}
.user-management {
  height: 100%;
  width: 88%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.page-header h3 {
  margin: 0;
  font-size: 20px;
}

.search-card {
  margin-bottom: 16px;
}

.search-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.table-card {
  margin-bottom: 16px;
}

.pagination-container {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>