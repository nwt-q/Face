<template>
  <div class="lab-booking-container">
    <div class="booking-header">
      <h2>实验室预约</h2>
      <button class="view-records-btn" @click="viewBookingRecords">查看预约记录</button>
    </div>

    <div class="lab-list">
      <div v-for="lab in labs" :key="lab.id" class="lab-card">
        <div class="lab-info">
          <h3>{{ lab.name }}</h3>
          <p>{{ lab.description }}</p>
          <div class="lab-status">
            <span class="status-indicator" :class="{ available: lab.available }"></span>
            {{ lab.available ? '空闲' : '使用中' }}
          </div>
        </div>

        <!-- <div class="time-slots">
          <h4>可用时间段</h4>
          <div class="time-slot-grid">
            <button
              v-for="slot in lab.timeSlots"
              :key="slot.id"
              class="time-slot-btn"
              :class="{ selected: selectedSlots.includes(slot.id) }"
              @click="toggleTimeSlot(slot.id)"
              :disabled="!slot.available"
            >
              {{ slot.time }}
            </button>
          </div>
        </div> -->
        <!-- <div class="time-slot-grid">
          <button v-for="time in lab.timeSlots" :key="time" class="time-slot-btn"
            :class="{ selected: selectedSlots.some(s => s.labId === labs.id && s.time === time) }"
            @click="toggleTimeSlot(labs.id, time)">
            {{ time }}
          </button>
        </div> -->


        <div class="time-slot-grid">
  <button 
    v-for="time in lab.timeSlots" 
    :key="time" 
    class="time-slot-btn"
    :class="{ selected: selectedSlots.some(s => s.labId === lab.id && s.time === time) }"
    @click="toggleTimeSlot(lab.id, time)"
  >
    {{ time }}
  </button>
</div>



        <button class="book-btn" @click="bookLab(lab)" :disabled="!canBook(lab.id)">
          预约
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { getUserLab } from '../api/Labs'

const router = useRouter()

const selectedSlots = ref<{ labId: string, time: string }[]>([])

// 模拟实验室数据
// const labs = ref([])

const labs = ref<Lab[]>([])

interface Lab {
  id: string
  name: string
  description: string
  available: boolean
  timeSlots: string[]  
}

const IniteData = () => {
  getUserLab().then((res: any) => {
    
      labs.value = res
    
  })
}
IniteData()
const toggleTimeSlot = (labId: string, time: string) => {
  const index = selectedSlots.value.findIndex(s => s.labId === labId && s.time === time)
  if (index === -1) {
    selectedSlots.value.push({ labId, time })
  } else {
    selectedSlots.value.splice(index, 1)
  }
}

const canBook = (labId: string) => {
  return selectedSlots.value.some(s => s.labId === labId)
}

// const canBook = (labId: string) => {
//   const lab = labs.value.find(l => l.id === labId)
//   return lab && selectedSlots.value.some(slotId =>
//     lab.timeSlots.some(slot => slot.id === slotId && slot.available)
//   )
// }

const bookLab = (lab: any) => {
  // TODO: 实现预约逻辑
  console.log('预约实验室', lab, selectedSlots.value)
  router.push({
    path: '/booking',
    query: {
        labId: lab.id,
        labName: lab.name,
        selectedSlots: JSON.stringify(selectedSlots.value)  // 序列化对象数组
    }
})
  
}

const viewBookingRecords = () => {
  router.push('/booking-records')
}


</script>

<style scoped>
.lab-booking-container {
  min-height: 100vh;
  min-width: 100vw;
  margin: 0 auto;
  padding: 2rem;
}

.booking-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.booking-header h2 {
  color: #333;
  margin: 0;
}

.view-records-btn {
  padding: 0.5rem 1rem;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.view-records-btn:hover {
  background-color: #1e88e5;
}

.lab-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.lab-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
}

.lab-info h3 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.lab-info p {
  color: #666;
  margin: 0 0 1rem 0;
}

.lab-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.status-indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #ff5252;
}

.status-indicator.available {
  background-color: #4CAF50;
}

.time-slots {
  margin-bottom: 1rem;
}

.time-slots h4 {
  color: #333;
  margin: 0 0 1rem 0;
}

.time-slot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 0.5rem;
}

.time-slot-btn {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.time-slot-btn:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
  color: #999;
}

.time-slot-btn.selected {
  background-color: #e3f2fd;
  border-color: #2196F3;
  color: #2196F3;
}

.book-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

.book-btn:hover {
  background-color: #45a049;
}

.book-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>