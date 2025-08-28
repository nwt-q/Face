<template>
<div class="book-container">
  <div class="booking-records-container">
    <div class="records-header">
      <h2>预约记录</h2>
      <div class="filter-controls">
        <select v-model="filterStatus" class="filter-select">
          <option value="all">全部状态</option>
          <option value="upcoming">即将到来</option>
          <option value="completed">已完成</option>
          <option value="cancelled">已取消</option>
        </select>
        <button class="new-booking-btn" @click="goToBooking">新建预约</button>
      </div>
    </div>

    <div class="records-list">
      <div v-for="record in filteredRecords" :key="record.id" class="record-card">
        <div class="record-header">
          <h3>{{ record.labName }}</h3>
          <span class="record-status" :class="record.status">
            {{ getStatusText(record.status) }}
          </span>
        </div>

        <div class="record-details">
          <div class="detail-item">
            <span class="label">预约时间</span>
            <span>{{ record.bookingTime }}</span>
          </div>
          <div class="detail-item">
            <span class="label">使用时段</span>
            <span>{{ record.timeSlot }}</span>
          </div>
          <div class="detail-item">
            <span class="label">预约日期</span>
            <span>{{ record.date }}</span>
          </div>
          <div class="detail-item">
            <span class="label">预约原因</span>
            <span>{{ record.booktest }}</span>
          </div>
        </div>

        <div class="record-actions">
          <button
            v-if="record.status === 'upcoming'"
            class="cancel-btn"
            @click="cancelBooking(record.id)"
          >
            取消预约
          </button>
          <button
            v-if="record.status === 'upcoming'"
            class="check-in-btn"
            @click="checkIn(record.id)"
          >
            待签到
          </button>
        </div>
      </div>

      <div v-if="filteredRecords.length === 0" class="no-records">
        <p>暂无预约记录</p>
      </div>
    </div>
  </div>
</div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const filterStatus = ref('all')

// const bookingRecords = ref(
//   JSON.parse(localStorage.getItem('bookings') || '[]')
// )
// 模拟预约记录数据
const bookingRecords = ref([
  {
    id: '1',
    labName: '实验室 A',
    status: 'upcoming',
    booktest: "暂无",
    bookingTime: '2024-01-20 10:30',
    timeSlot: '14:00-15:00',
    date: '2024-01-25'
  },
  {
    id: '2',
    labName: '实验室 B',
    status: 'completed',
    booktest: "暂无",
    bookingTime: '2024-01-15 09:00',
    timeSlot: '10:00-11:00',
    date: '2024-01-18'
  },
  {
    id: '3',
    labName: '实验室 A',
    status: 'cancelled',
    booktest: "暂无",
    bookingTime: '2024-01-10 14:20',
    timeSlot: '16:00-17:00',
    date: '2024-01-12'
  }
])

const filteredRecords = computed(() => {
  if (filterStatus.value === 'all') return bookingRecords.value
  return bookingRecords.value.filter((record: { status: string }) => record.status === filterStatus.value)
})

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    upcoming: '即将到来',
    completed: '已完成',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

const cancelBooking = (id: string) => {
  // TODO: 实现取消预约逻辑
  console.log('取消预约', id)

  // 找到要取消的预约记录
  const recordIndex = bookingRecords.value.findIndex((record: { id: string }) => record.id === id)

  if (recordIndex !== -1) {
    // 更新记录状态为已取消
    bookingRecords.value[recordIndex].status = 'cancelled'
  }
}

const deleteRecord = (id: number) => {
  const filtered = bookingRecords.value.filter((b: any) => b.id !== id)
  localStorage.setItem('bookings', JSON.stringify(filtered))
  bookingRecords.value = filtered
}

const checkIn = (id: string) => {
  // TODO: 实现签到逻辑
  console.log('签到', id)

}

const goToBooking = () => {
  router.push('/lab-booking')
}
</script>

<style scoped>
.book-container{
  margin: 0 auto;
  width: 100%;
  display: inline - block;
  min-height: 100vh;
  min-width: 100vw;
  padding: 2rem;
  /* 由于使用了 display: inline-block，此处不需要 vertical-align */ 
}
.booking-records-container {
  max-width: 1000px;
  min-height: auto;
  width: 100%;
  margin: 0 auto;
  padding: 2rem;
}

.records-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.records-header h2 {
  color: #333;
  margin: 0;
}

.filter-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.filter-select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
}

.new-booking-btn {
  padding: 0.5rem 1rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.new-booking-btn:hover {
  background-color: #45a049;
}

.records-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin: auto 0;
}

.record-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.record-header h3 {
  color: #333;
  margin: 0;
}

.record-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.9rem;
}

.record-status.upcoming {
  background-color: #e3f2fd;
  color: #2196F3;
}

.record-status.completed {
  background-color: #e8f5e9;
  color: #4CAF50;
}

.record-status.cancelled {
  background-color: #ffebee;
  color: #f44336;
}

.record-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.detail-item .label {
  color: #666;
  font-size: 0.9rem;
}

.record-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.cancel-btn,
.check-in-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s;
}

.cancel-btn {
  background-color: #f44336;
  color: white;
}

.cancel-btn:hover {
  background-color: #e53935;
}

.check-in-btn {
  background-color: #2196F3;
  color: white;
}

.check-in-btn:hover {
  background-color: #1e88e5;
}

.no-records {
  text-align: center;
  padding: 3rem;
  color: #666;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>