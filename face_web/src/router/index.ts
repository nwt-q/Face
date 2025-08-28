import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/face-login',
      name: 'face-login',
      component: () => import('../views/FaceLoginView.vue')
    },
    {
      path: '/lab-booking',
      name: 'lab-booking',
      component: () => import('../views/LabBookingView.vue')
    },

    {
      path: '/booking',
      name: 'BookingView',
      component: () => import('../views/BookingView.vue')
    },
    
    {
      path: '/booking-records',
      name: 'booking-records',
      component: () => import('../views/BookingRecordsView.vue')
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/admin',
      component: () => import('@/views/admin/AdminLayout.vue'),
      meta: {
        requiresAuth: true,
        isAdmin: true
      },
      children: [
        {
          path: '',
          redirect: '/admin/attendance'
        },
        {
          path: 'users',
          name: 'AdminUsers',
          component: () => import('@/views/admin/UserManagement.vue'),
          meta: {
            title: '用户管理',
            requiresAuth: true,
            isAdmin: true
          }
        },
        {
          path: 'labs',
          name: 'AdminLabs',
          component: () => import('@/views/admin/LabManagement.vue'),
          meta: {
            title: '实验室管理',
            requiresAuth: true,
            isAdmin: true
          }
        },
        {
          path: 'bookings',
          name: 'AdminBookings',
          component: () => import('@/views/admin/BookingManagement.vue'),
          meta: {
            title: '预约记录管理',
            requiresAuth: true,
            isAdmin: true
          }
        },
        {
          path: 'attendance',
          name: 'AdminAttendance',
          component: () => import('@/views/admin/AttendanceManagement.vue'),
          meta: {
            title: '考勤记录管理',
            requiresAuth: true,
            isAdmin: true
          }
        },
        {
          path: 'equipment',
          name: 'Equipment',
          component: () => import('@/views/admin/EquipmentManagement.vue'),
          meta: {
            title: '考勤记录管理',
            requiresAuth: true,
            isAdmin: true
          }
        }
      ]
    },
  ],
})

// router.beforeEach(async (to, from, next) => {
//   const userStore = useUserStore()
//   const isAuthenticated = userStore.isAuthenticated

//   if (to.meta.requiresAuth && !isAuthenticated) {
//     next('/login')
//     return
//   }

//   if (to.meta.isAdmin && !userStore.isAdmin) {
//     next('/lab-booking')
//     return
//   }

//   next()
// })

export default router
