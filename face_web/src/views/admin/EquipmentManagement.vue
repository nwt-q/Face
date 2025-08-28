<template>
  <div class="equipment-management">
    <div class="page-header">
      <h3>设备管理</h3>
      <el-button type="primary" @click="handleAddEquipment">
        <el-icon>
          <Plus />
        </el-icon>添加设备
      </el-button>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="设备名称">
          <el-input v-model="searchForm.name" placeholder="请输入设备名称" clearable />
        </el-form-item>
        <!-- <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="可用" value="available" />
            <el-option label="借用中" value="borrowed" />
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
      <el-table :data="equipmentList" style="width: 100%" row-key="id">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="labName" label="实验室" />
        <el-table-column prop="name" label="设备名" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastBorrowerName" label="借用人" />
        <el-table-column prop="createdAt" label="借用日期" width="160" />
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
        <el-table-column type="expand" width="60">
          <template #default="{ row }">
            <el-table :data="row.borrowRecords">
              <el-table-column prop="Labname" label="所属实验室" />
              <el-table-column prop="date" label="借用日期" width="160" />
              <el-table-column prop="name" label="借用人" />
              <el-table-column prop="createdAt" label="归还日期" width="160" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="row.status === '已归还' ? 'success' : 'warning'">
                    {{ row.status }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
          :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '新增设备' : '编辑设备'" width="600px">
      <el-form :model="equipmentForm" :rules="equipmentFormRules" label-width="120px" ref="formRef">
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="equipmentForm.name" placeholder="请输入设备名称" />
        </el-form-item>
        <!-- <el-form-item label="设备名" prop="name">
          <el-input v-model="equipmentForm.name" placeholder="请输入设备型号" />
        </el-form-item> -->
        <el-form-item label="设备状态" prop="status">
          <el-select v-model="equipmentForm.status" placeholder="请选择状态">
            <el-option label="可用" value="available" />
            <el-option label="借用中" value="borrowed" />
            <el-option label="维护中" value="maintenance" />
          </el-select>
        </el-form-item>
        <el-form-item label="采购日期" prop="purchaseDate">
          <el-date-picker v-model="equipmentForm.purchaseDate" type="date" placeholder="选择采购日期"
            value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { LabService } from '@/services/labService'
import { onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 搜索表单
const searchForm = reactive({
  name: '',
  status: ''
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const formRef = ref()
// 设备列表数据
const equipmentList = ref([
  {
    id: 1,
    name: '示波器',
    model: 'DSO-X 2002A',
    status: 'available',
    borrower: '',
    borrowDate: '',
    borrowRecords: [
      {
        date: '2024-03-01',
        borrower: '张三',
        returnDate: '2024-03-05',
        status: '已归还'
      }
    ]
  }
])

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    // 这里调用新增/编辑接口
    if(dialogType.value === 'add'){
      LabService.addEquipment(equipmentForm).then((res: any) => {
        console.log(res)
        initData() 
        // 刷新表格数据
      })
    } else {
      LabService.updateEquipment(equipmentForm).then((res: any) => {
        console.log(res)
        initData() 
        // 刷新表格数据
      })
    }
    ElMessage.success(dialogType.value === 'add' ? '添加成功' : '编辑成功')
    dialogVisible.value = false
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}



// 状态相关
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    available: 'success',
    borrowed: 'warning',
    maintenance: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    available: '可用',
    borrowed: '借用中',
    maintenance: '维护中'
  }
  return statusMap[status] || status
}

// 表单相关
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const equipmentForm = reactive({
  id: null,
  name: '',
  model: '',
  status: 'available',
  borrower: '',
  purchaseDate: ''
})

// 维护记录相关
const maintenanceDialogVisible = ref(false)
const maintenanceForm = reactive({
  type: 'routine',
  description: '',
  estimatedTime: ''
})

// 表单验证规则
const equipmentFormRules = {
  name: [
    { required: true, message: '请输入设备名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在2到50个字符', trigger: 'blur' }
  ],
  model: [
    { required: true, message: '请输入设备型号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择设备状态', trigger: 'change' }
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
  initData()
})

const initData = () => {
  LabService.getEquipment().then((res: any) => {
    console.log(res)
    equipmentList.value = res
  })
}

// 交互处理方法
const handleSearch = () => {
  console.log('搜索条件：', searchForm)
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.status = ''
  initData()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  handleSearch()
}

const handleAddEquipment = () => {
  dialogType.value = 'add'
  dialogVisible.value = true
  Object.assign(equipmentForm, {
    name: '',
    model: '',
    status: 'available',
    borrower: '',
    purchaseDate: ''
  })
}

const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  dialogVisible.value = true
  Object.assign(equipmentForm, row)
}

const handleMaintenance = (row: any) => {
  // maintenanceDialogVisible.value = true
  // maintenanceForm.type = 'routine'
  // maintenanceForm.description = ''
  // maintenanceForm.estimatedTime = ''
  if(row.status === 'borrowed'){
    ElMessage.warning('设备正在借用中，无法维护')
    return 
  }
  row.status = 'maintenance'
  LabService.updateEquipment(row).then((res: any) => {
    console.log(res)
  })
}

// 完善删除功能
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认删除该设备？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    // 这里调用删除接口
    LabService.deleteEquipment(row.id).then((res: any) => {
      console.log(res) 
      initData() 
    })
    ElMessage.success('删除成功')
    initData()
  } catch {
    // 取消删除
  }
}

</script>

<style scoped>
.equipment-management {
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