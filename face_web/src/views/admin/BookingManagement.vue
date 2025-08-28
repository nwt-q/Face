<template>
  <div class="booking-management">
    <div class="page-header">
      <h3>预约记录管理</h3>
      <el-button type="primary" @click="handleExport">
        <el-icon><Download /></el-icon>导出记录
      </el-button>
    </div>

    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <!-- <el-form-item label="实验室">
          <el-select v-model="searchForm.labId" placeholder="请选择实验室" clearable>
            <el-option
              v-for="lab in labOptions"
              :key="lab.id"
              :label="lab.name"
              :value="lab.id"
            />
            
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item label="预约状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待审核" value="pending" />
            <el-option label="已通过" value="approved" />
            <el-option label="已拒绝" value="rejected" />
            <el-option label="已取消" value="cancelled" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </el-form-item> -->
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="bookingList" border style="width: 100%">
        <el-table-column prop="id" label="预约编号" width="100" />
        <el-table-column prop="labName" label="实验室" />
        <el-table-column prop="userName" label="预约人" />
        <el-table-column prop="createdAt" label="预约日期" width="120" />
        <el-table-column prop="arrivalDate" label="达到日期" width="120" />
        <el-table-column prop="reason" label="用途" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="timeSlots" label="申请时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button
                v-if="row.status === 'pending'"
                type="success"
                size="small"
                @click="handleApprove(row)"
              >
                通过
              </el-button>
              <el-button
                v-if="row.status === 'pending'"
                type="danger"
                size="small"
                @click="handleReject(row)"
              >
                拒绝
              </el-button>
              <el-button
                type="primary"
                size="small"
                @click="handleViewDetails(row)"
              >
                详情
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

    <!-- 预约详情对话框 -->
    <el-dialog
      v-model="detailsDialogVisible"
      title="预约详情"
      width="600px"
    >
      <el-descriptions :column="2">
        <el-descriptions-item label="预约编号">{{ selectedBooking.id }}</el-descriptions-item>
        <el-descriptions-item label="实验室">{{ selectedBooking.labName }}</el-descriptions-item>
        <el-descriptions-item label="预约人">{{ selectedBooking.userName }}</el-descriptions-item>
        <!-- <el-descriptions-item label="联系方式">{{ selectedBooking.contact }}</el-descriptions-item> -->
        <!-- <el-descriptions-item label="预约日期">{{ selectedBooking.bookingDate }}</el-descriptions-item> -->
        <!-- <el-descriptions-item label="时间段">{{ selectedBooking.timeSlot }}</el-descriptions-item> -->
        <el-descriptions-item label="用途" :span="2">{{ selectedBooking.reason }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ selectedBooking.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(selectedBooking.status)">
            {{ getStatusText(selectedBooking.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item
          v-if="selectedBooking.rejectReason"
          label="拒绝原因"
          :span="2"
        >
          {{ selectedBooking.rejectReason }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 拒绝原因对话框 -->
    <el-dialog
      v-model="rejectDialogVisible"
      title="拒绝预约"
      width="500px"
    >
      <el-form
        ref="rejectFormRef"
        :model="rejectForm"
        :rules="rejectFormRules"
        label-width="80px"
      >
        <el-form-item label="拒绝原因" prop="reason">
          <el-input
            v-model="rejectForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRejectSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { onMounted } from 'vue'
import { Download } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { LabService } from '@/services/labService'

// 搜索表单
const searchForm = reactive({
  labId: '',
  status: '',
  dateRange: []
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 实验室选项
const labOptions = ref([
  { id: '1', name: '计算机网络实验室' },
  { id: '2', name: '人工智能实验室' }
])

// 预约记录列表
const bookingList = ref([
  {
    id: 'BK20240120001',
    labName: '计算机网络实验室',
    userName: '张三',
    bookingDate: '2024-01-25',
    timeSlot: '14:00-15:00',
    reason: '网络协议实验',
    status: 'pending',
    createTime: '2024-01-20 10:30:00',
    contact: '13800138000'
  }
])

// 选中的预约记录
const selectedBooking = reactive({
  id: '',
  labName: '',
  userName: '',
  bookingDate: '',
  timeSlot: '',
  reason: '',
  status: '',
  createdAt: '',
  contact: '',
  rejectReason: ''
})

// 对话框相关
const detailsDialogVisible = ref(false)
const rejectDialogVisible = ref(false)
const rejectFormRef = ref<FormInstance>()
const rejectForm = reactive({
  reason: '',
})

// 表单验证规则
const rejectFormRules = {
  reason: [
    { required: true, message: '请输入拒绝原因', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
  ]
}



// 状态相关
const getStatusType = (status: string) => {
  // DEFAULT 'pending' COMMENT '状态: pending-待确认, confirmed-已确认, completed-已完成, canceled-已取消'
  const statusMap: Record<string, string> = {
    pending: 'warning',
    confirmed: 'success',
    rejected: 'danger',
    canceled: 'info',
    completed: 'success'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: '待审核',
    confirmed: '已通过',
    rejected: '已拒绝',
    canceled: '已取消',
    completed: '已完成'
  }
  return statusMap[status] || status
}
onMounted(() => {
  initData() 
  console.log('初始化数据')
})

// 初始化数据
const initData = () => {
  // TODO: 实现初始化数据逻辑
  LabService.getBookings().then((res: any) => {
    bookingList.value = res
    console.log('初始化数据' + res)
  })
  // 获取实验室列表
  LabService.getLabsName().then((res: any) => {
    labOptions.value = res
    console.log('获取实验室列表' + res)
  })
  console.log('初始化数据')
}

// 搜索处理
const handleSearch = () => {
  // TODO: 实现搜索逻辑
  console.log('搜索条件：', searchForm)
}

const resetSearch = () => {
  searchForm.labId = ''
  searchForm.status = ''
  searchForm.dateRange = []
  initData();
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

// 查看详情
const handleViewDetails = (row: any) => {
  Object.assign(selectedBooking, row)
  detailsDialogVisible.value = true
}

// 通过预约
const handleApprove = (row: any) => {
  ElMessageBox.confirm(
    '确认通过该预约申请吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info',
    }
  ).then(() => {
    // TODO: 实现通过逻辑
    //DEFAULT 'pending' COMMENT '状态: pending-待确认, confirmed-已确认, completed-已完成, canceled-已取消'
    row.status = 'confirmed'
    LabService.updateBooking(row).then((res: any) => {
      console.log('通过预约' + res)
      initData() 
    })
    ElMessage.success('已通过预约申请')
  }).catch(() => {
    // 取消操作
  })
}

// 拒绝预约
const handleReject = (row: any) => {
  Object.assign(selectedBooking, row)
  rejectDialogVisible.value = true
  rejectForm.reason = ''
  row.status = 'rejected'
  LabService.updateBooking(row).then((res: any) => {
    console.log('拒绝预约' + res)
    initData()
  })
}

// 提交拒绝
const handleRejectSubmit = async () => {
  if (!rejectFormRef.value) return

  await rejectFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 实现拒绝逻辑
      // row.status = 'confirmed'
      // LabService.updateBooking(row).then((res: any) => {
      //   console.log('通过预约' + res)
      //   initData() 
      // })
      console.log('拒绝原因：', rejectForm.reason)
      ElMessage.success('已拒绝预约申请')
      rejectDialogVisible.value = false
    }
  })
}

// 导出记录
const handleExport = () => {
  // TODO: 实现导出逻辑
  ElMessage.success('导出成功')
}
</script>

<style scoped>
.booking-management {
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