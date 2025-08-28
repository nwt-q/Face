<template>
  <div class="lab-management">
    <div class="page-header">
      <h3>实验室管理</h3>
      <el-button type="primary" @click="handleAddLab">
        <el-icon>
          <Plus />
        </el-icon>
        添加实验室
      </el-button>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="实验室名称">
          <el-input v-model="searchForm.name" placeholder="请输入实验室名称" clearable />
        </el-form-item>
        <!-- <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="空闲" value="available" />
            <el-option label="使用中" value="occupied" />
            <el-option label="维护中" value="maintenance" />
          </el-select>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="labList" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="实验室名称" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="capacity" label="容量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="equipment" label="设备清单" show-overflow-tooltip /> -->
        <el-table-column prop="createdAt" label="创建时间" width="160" />
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button-group>
              <el-button type="primary" size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button type="warning" size="small" @click="handleMaintenance(row)">
                维护
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
          :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 实验室表单对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增实验室' : '编辑实验室'" width="600px">
      <el-form :model="labForm" :rules="labFormRules" label-width="100px" ref="labFormRef">
        <el-form-item label="实验室名称" prop="name">
          <el-input v-model="labForm.name" placeholder="请输入实验室名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="labForm.description" type="textarea" :rows="3" placeholder="请输入实验室描述" />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input-number v-model="labForm.capacity" :min="1" :max="200" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="labForm.status" placeholder="请选择状态">
            <el-option label="空闲" value="available" />
            <el-option label="使用中" value="occupied" />
            <el-option label="维护中" value="maintenance" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备清单" prop="equipment">
          <el-input v-model="labForm.equipment" placeholder="请输入设备清单，多个设备用逗号分隔" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import request from '../../api/base'
import { getLabs, deleteLab,updateLab, addLab,searchLabsByName} from '../../api/Labs'
import { onMounted } from 'vue'
import { ElMessage,ElMessageBox } from 'element-plus'


// 添加表单引用
const labFormRef = ref()


// 搜索表单
const searchForm = reactive({
  name: '',
  status: ''
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 实验室列表数据
const labList = ref([
  {
    id: 1,
    name: '计算机网络实验室',
    description: '用于计算机网络课程实验',
    capacity: 30,
    status: 'available',
    equipment: '路由器,交换机,网线,工作站',
    createTime: '2024-01-20 10:00:00'
  }
])

// 表单相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
// const labFormRef = ref<FormInstance>()
const labForm = reactive({
  id: 0,
  name: '',
  description: '',
  capacity: 30,
  status: 'available',
  equipment: ''
})

// 维护记录相关
const maintenanceDialogVisible = ref(false)
// const maintenanceFormRef = ref<FormInstance>()
const maintenanceForm = reactive({
  type: 'routine',
  description: '',
  estimatedTime: ''
})

// 表单验证规则
const labFormRules = {
  name: [
    { required: true, message: '请输入实验室名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' }
  ],
  capacity: [
    { required: true, message: '请输入容量', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  equipment: [
    { required: true, message: '请输入设备清单', trigger: 'blur' }
  ]
}

const maintenanceFormRules = {
  type: [
    { required: true, message: '请选择维护类型', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入维护说明', trigger: 'blur' }
  ],
  estimatedTime: [
    { required: true, message: '请选择预计完成时间', trigger: 'change' }
  ]
}


onMounted(() => {
  fetchLabList()
})

// 获取页面数据
const fetchLabList = () => {
    getLabs().then((res: any) => {
    labList.value = res;
    console.log(res)
  })
}

// 状态相关
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    available: 'success',
    occupied: 'warning',
    maintenance: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    available: '空闲',
    occupied: '使用中',
    maintenance: '维护中'
  }
  return statusMap[status] || status
}


// 搜索处理
const handleSearch = () => {
  // TODO: 实现搜索逻辑
  console.log('搜索条件：', searchForm.name)
  searchLabsByName(searchForm.name).then((res: any) => {
    labList.value = [res];
    console.log(res)
  })
  console.log('搜索条件：', searchForm)
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.status = ''
  fetchLabList()
}

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  console.log('当前页：', currentPage.value)
  handleSearch()
}

// 添加实验室
const handleAddLab = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  // 重置表单
  Object.assign(labForm, {
    name: '',
    description: '',
    capacity: 30,
    status: 'available',
    equipment: ''
  })
}

// 编辑实验室
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  console.log('编辑实验室：', row)
  // 填充表单数据
  Object.assign(labForm, row)
}

// 维护实验室
const handleMaintenance = (row: any) => {
  row.status = 'maintenance'
  maintenanceDialogVisible.value = true
  // 更新状态
  updateLab(row.id, row)
  console.log('维护实验室：', row)
}

// 删除实验室
const handleDelete = (row: any) => {
  ElMessageBox.confirm(
    '确认要删除该实验室吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    await deleteLab(row.id)
    ElMessage.success('删除成功')
    fetchLabList()
  }).catch(() => {
    // 取消删除
  })
}

// 提交实验室表单
const handleSubmit = async () => {
  // if (!labFormRef.value) return

  // await labFormRef.value.validate((valid: boolean) => {
  //   if (valid) {
  //     // TODO: 实现提交逻辑
  //     console.log('表单数据：', labForm)
  //     ElMessage.success(dialogType.value === 'add' ? '添加成功' : '编辑成功')
  //     dialogVisible.value = false
  //   }
  // })
  if (!labFormRef.value) return

try {
  // 执行表单验证
  await labFormRef.value.validate()
  
  if (dialogType.value === 'add') {
    // 新增实验室
    await addLab(labForm)
    ElMessage.success('添加成功')
  } else {
    // 编辑实验室
    await updateLab(labForm.id, labForm)
    ElMessage.success('编辑成功')
  }
  
  // 刷新列表并关闭对话框
  dialogVisible.value = false
  fetchLabList()
} catch (error) {
  console.error('操作失败:', error)
  ElMessage.error('操作失败，请重试')
}
}

// 提交维护记录
const handleMaintenanceSubmit = async () => {
  // if (!maintenanceFormRef.value) return

  // await maintenanceFormRef.value.validate((valid) => {
  //   if (valid) {
  //     // TODO: 实现维护记录提交逻辑
  //     console.log('维护记录：', maintenanceForm)
  //     ElMessage.success('维护记录已提交')
  //     maintenanceDialogVisible.value = false
  //   }
  // })
  if (!labFormRef.value) return
  
  try {
    await labFormRef.value.validate()
    const url = dialogType.value === 'add' ? '/labs' : `/labs/${labForm.id}`
    const method = dialogType.value === 'add' ? 'post' : 'put'
    
    await request({
      url,
      method,
      data: labForm
    })
    
    ElMessage.success(dialogType.value === 'add' ? '添加成功' : '编辑成功')
    dialogVisible.value = false
    fetchLabList()
  } catch (error) {
    console.error('表单提交失败:', error)
  }
}
</script>

<style scoped>
.lab-management {
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