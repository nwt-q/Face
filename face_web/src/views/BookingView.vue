<template>
    <div class="booking-container">
        <div class="booking-wrapper">
            <h2 class="booking-title">实验室预约详情</h2>
            <el-form :model="bookingForm" label-width="100px">
                <el-form-item label="实验室名称" class="form-item">
                    <el-input v-model="bookingForm.labName" disabled />
                </el-form-item>
                <el-form-item label="预约时间段">
                    <el-tag v-for="(slot, index) in bookingForm.selectedSlots" :key="index" type="info"
                        style="margin-right: 8px; margin-bottom: 8px">
                        {{ slot }} <!-- 显示time属性或整个对象 -->
                    </el-tag>
                </el-form-item>
                <el-form-item label="是否借还设备">
                    <el-checkbox v-model="bookingForm.isBorrowReturn">需要借还设备</el-checkbox>
                </el-form-item>
                <el-form-item label="设备选择" v-if="bookingForm.isBorrowReturn && equipmentList.length > 0">
                    <el-select v-model="bookingForm.selectedEquipment" placeholder="请选择设备" multiple>
                        <el-option v-for="equipment in equipmentList" :key="equipment.id" :label="equipment.name"
                            :value="equipment.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="借还选择" v-if="bookingForm.isBorrowReturn">
                    <el-radio-group v-model="bookingForm.borrowReturn">
                        <el-radio label="borrow">借出</el-radio>
                        <el-radio label="return">归还</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="预约到达日期">
                    <el-date-picker v-model="bookingForm.arrivalDate" type="date" placeholder="选择到达日期" />
                </el-form-item>
                <el-form-item label="备注原因">
                    <el-input v-model="bookingForm.reason" type="textarea" placeholder="请输入备注原因" />
                </el-form-item>
                <div class="form-footer">
                    <el-button @click="cancelBooking">取消</el-button>
                    <el-button type="primary" @click="confirmBooking">确认预约</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { LabService } from '@/services/labService'

const route = useRoute()
const router = useRouter()

const deleteBooking = (bookingId: number) => {
    const existingData = localStorage.getItem('bookings')
    if (existingData) {
        const bookings = JSON.parse(existingData)
        const filtered = bookings.filter((b: any) => b.id !== bookingId)
        localStorage.setItem('bookings', JSON.stringify(filtered))
    }
}



const bookingForm = ref({
    id: route.query.id || 1,
    labName: route.query.labName || '',
    selectedSlots: Array.isArray(route.query.selectedSlots) 
        ? route.query.selectedSlots 
        : (route.query.selectedSlots ? [route.query.selectedSlots] : []),
    reason: '',
    isBorrowReturn: false,
    selectedEquipment: [],
    borrowReturn: 'borrow',
    arrivalDate: ''
})

// 模拟设备列表数据
const equipmentList = ref([
    { id: 'e1', name: '显微镜' },
    { id: 'e2', name: '离心机' },
    { id: 'e3', name: '培养箱' }
])

const IniteDatas = () => {
    LabService.getEquipment().then((res: any) => {
        equipmentList.value = res
    })
}
IniteDatas();

const confirmBooking = () => {
    // TODO: 实现预约确认逻辑
    // 提交预约信息到本地json文件
    console.log('确认预约:', bookingForm.value)
    LabService.appointment(bookingForm.value).then((res: any) => {
        console.log(res)
        router.push('/booking-records')
    })
    // 预约成功后跳转到预约记录页面
    //router.push('/booking-records')

    // 生成唯一ID和时间戳
    const bookingData = {
        ...bookingForm.value,
        id: Date.now(),
        createTime: new Date().toISOString()
    }

    // 从本地存储读取已有数据
    const existingData = localStorage.getItem('bookings')
    const bookings = existingData ? JSON.parse(existingData) : []

    // 添加新预约
    bookings.push(bookingData)
    localStorage.setItem('bookings', JSON.stringify(bookings))
    // 跳转预约记录页面
    router.push('/booking-records')
}

const cancelBooking = () => {
    // TODO: 实现取消预约逻辑
    console.log('取消预约')

    router.push('/booking-records')
}
</script>

<style scoped>
.booking-container {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f5f7fa;
    padding: 20px;
}

.booking-wrapper {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 600px;
    padding: 30px;
}

.booking-title {
    text-align: center;
    margin-bottom: 30px;
    color: #303133;
}

.booking-form {
    .form-item {
        margin-bottom: 22px;

        :deep(.el-form-item__label) {
            font-weight: 500;
            color: #606266;
        }
    }
}

.form-footer {
    margin-top: 35px;
    display: flex;
    justify-content: center;
    gap: 20px;
}

@media (max-width: 720px) {
    .booking-container {
        padding: 10px;
        align-items: flex-start;

    }

    .booking-wrapper {
        padding: 15px;
        margin-top: 200px;
        border-radius: 6px;
    }

    .booking-title {
        font-size: 1.4rem;
        margin-bottom: 20px;
    }

    .booking-form {
        .form-item {
            margin-bottom: 20px;

            :deep(.el-form-item__label) {
                font-size: 14px;
                line-height: 1.5;
            }

            :deep(.el-input),
            :deep(.el-select),
            :deep(.el-date-editor) {
                --el-input-font-size: 14px;

                .el-input__wrapper {
                    padding: 8px 12px;
                }
            }

            :deep(.el-textarea) {
                --el-input-font-size: 14px;
                min-height: 80px;
            }
        }
    }

    .form-footer {
        margin-top: 25px;

        .el-button {
            height: 42px;
            font-size: 14px;
        }
    }
}
</style>