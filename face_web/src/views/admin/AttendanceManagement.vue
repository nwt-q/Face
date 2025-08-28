<template>
  <div class="attendance-management">
    <div class="page-header">
      <h3>考勤记录管理</h3>
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
        <!-- <el-form-item label="考勤状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="正常" value="normal" />
            <el-option label="迟到" value="late" />
            <el-option label="早退" value="early" />
            <el-option label="缺勤" value="absent" />
          </el-select>
        </el-form-item> -->
        <el-form-item label="考勤日期">
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

    <div class="statistics-cards">
      <el-row :gutter="16">
        <el-col :span="6">
          <el-card class="statistic-card">
            <template #header>
              <div class="card-header">
                <span>总考勤次数</span>
              </div>
            </template>
            <div class="statistic-value">{{ statistics.total }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <template #header>
              <div class="card-header">
                <span>正常考勤率</span>
              </div>
            </template>
            <div class="statistic-value">{{ statistics.normalRate }}%</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <template #header>
              <div class="card-header">
                <span>异常考勤数</span>
              </div>
            </template>
            <div class="statistic-value">{{ statistics.abnormal }}</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <template #header>
              <div class="card-header">
                <span>缺勤次数</span>
              </div>
            </template>
            <div class="statistic-value">{{ statistics.absent }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-card class="table-card">
      <el-table :data="attendanceList" border style="width: 100%">
        <el-table-column prop="id" label="记录编号" width="120" />
        <el-table-column prop="userName" label="用户名" />
        <el-table-column prop="labName" label="实验室" />
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="timeSlot" label="时间段" width="120" />
        <el-table-column prop="checkInTime" label="签到时间" width="160" />
        <el-table-column prop="checkOutTime" label="签退时间" width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleViewDetails(row)"
            >
              详情
            </el-button>
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

    <!-- 考勤详情对话框 -->
    <el-dialog
      v-model="detailsDialogVisible"
      title="考勤详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="记录编号">{{ selectedAttendance.id }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ selectedAttendance.userName }}</el-descriptions-item>
        <el-descriptions-item label="实验室">{{ selectedAttendance.labName }}</el-descriptions-item>
        <el-descriptions-item label="日期">{{ selectedAttendance.date }}</el-descriptions-item>
        <el-descriptions-item label="时间段">{{ selectedAttendance.timeSlot }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(selectedAttendance.status)">
            {{ getStatusText(selectedAttendance.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="签到时间">{{ selectedAttendance.checkInTime }}</el-descriptions-item>
        <el-descriptions-item label="签退时间">{{ selectedAttendance.checkOutTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ selectedAttendance.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'


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

// 统计数据
const statistics = reactive({
  total: 1000,
  normalRate: 95.5,
  abnormal: 45,
  absent: 10
})

// 考勤记录列表
const attendanceList = ref([
  {
    id: 'AT20240120001',
    userName: '张三',
    labName: '计算机网络实验室',
    date: '2024-01-20',
    timeSlot: '14:00-15:00',
    checkInTime: '2024-01-20 13:55:00',
    checkOutTime: '2024-01-20 15:05:00',
    status: 'normal',
    remark: ''
  }
])

// 选中的考勤记录
const selectedAttendance = reactive({
  id: '',
  userName: '',
  labName: '',
  date: '',
  timeSlot: '',
  checkInTime: '',
  checkOutTime: '',
  status: '',
  remark: ''
})

// 对话框相关
const detailsDialogVisible = ref(false)

// 状态相关
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    normal: 'success',
    late: 'warning',
    early: 'warning',
    absent: 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    normal: '正常',
    late: '迟到',
    early: '早退',
    absent: '缺勤'
  }
  return statusMap[status] || status
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

// 查看详情
const handleViewDetails = (row: any) => {
  Object.assign(selectedAttendance, row)
  detailsDialogVisible.value = true
}

// 导出记录
const handleExport = () => {
  // TODO: 实现导出逻辑
  //ElMessage.success('导出成功')
}
</script>

<style scoped>
.attendance-management {
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

.statistics-cards {
  margin-bottom: 16px;
}

.statistic-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
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